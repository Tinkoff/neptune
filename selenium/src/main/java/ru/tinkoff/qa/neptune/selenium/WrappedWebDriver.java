package ru.tinkoff.qa.neptune.selenium;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.selenium.*;
import org.openqa.selenium.net.NetworkUtils;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.server.SeleniumServer;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;

import java.net.URL;

import static com.browserup.bup.proxy.CaptureType.*;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.openqa.selenium.Proxy.ProxyType.MANUAL;
import static org.openqa.selenium.net.PortProber.findFreePort;
import static ru.tinkoff.qa.neptune.core.api.utils.ConstructorUtil.findSuitableConstructor;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FOR_PAGE_LOADED_DURATION;

public class WrappedWebDriver implements WrapsDriver, ContextRefreshable {

    private final static String DEFAULT_LOCAL_HOST = "http://localhost:%s/wd/hub";
    private static SeleniumServer server;
    private static boolean serverStarted;
    private static URL serverUrl;

    private final SupportedWebDrivers supportedWebDriver;
    private final BrowserUpProxy proxy;
    private WebDriver driver;
    private boolean isWebDriverInstalled;

    public WrappedWebDriver(SupportedWebDrivers supportedWebDriver) {
        this.supportedWebDriver = supportedWebDriver;

        if (ofNullable(USE_BROWSER_PROXY.get()).orElse(false)) {
            proxy = new BrowserUpProxyServer();
            proxy.setTrustAllServers(true);
        } else {
            proxy = null;
        }
    }

    private static synchronized void initServerLocally() {
        if (nonNull(server) && serverStarted) {
            return;
        }
        serverStarted = false;
        var standAloneConfig = new StandaloneConfiguration();
        var serverPort = findFreePort();
        standAloneConfig.port = serverPort;
        server = new SeleniumServer(standAloneConfig);
        try {
            serverStarted = server.boot();
            serverUrl = new URL(format(DEFAULT_LOCAL_HOST, serverPort));
        } catch (Throwable e) {
            serverStarted = false;
            server = null;
            throw new RuntimeException(e);
        }
    }

    private static synchronized void shutDownServerLocally() {
        if (server == null || serverStarted) {
            return;
        }

        try {
            server.stop();
            server = null;
            serverStarted = false;
        } catch (Throwable ignored) {
        }
    }

    private void initDriverIfNecessary() {
        driver = ofNullable(driver).orElseGet(() -> {
            Object[] parameters;
            Object[] arguments = supportedWebDriver.get();

            if (proxy != null) {
                MutableCapabilities capabilities = (MutableCapabilities) stream(arguments)
                        .filter(arg -> MutableCapabilities.class.isAssignableFrom(arg.getClass()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Browser mutable capabilities not found"));

                if (!capabilities.asMap().containsKey(CapabilityType.PROXY)) {
                    proxy.start();

                    Proxy seleniumProxy = new Proxy();
                    seleniumProxy.setProxyType(MANUAL);

                    String hostIp = new NetworkUtils().getIp4NonLoopbackAddressOfThisMachine().getHostAddress();

                    seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
                    seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());
                    seleniumProxy.setFtpProxy(hostIp + ":" + proxy.getPort());

                    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
                    capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                    capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

                    for (var i = 0; i < arguments.length; i++) {
                        if (MutableCapabilities.class.isAssignableFrom(arguments[i].getClass())) {
                            arguments[i] = capabilities;
                        }
                    }
                }
            }

            if (supportedWebDriver.requiresRemoteUrl() && supportedWebDriver.getRemoteURL() == null) {
                initServerLocally();
                parameters = ArrayUtils.addAll(new Object[]{serverUrl}, arguments);
            } else {
                parameters = arguments;
            }

            try {
                var c = findSuitableConstructor(supportedWebDriver.getWebDriverClass(),
                        parameters);

                if (!isWebDriverInstalled) {
                    ofNullable(supportedWebDriver.getWebDriverManager())
                            .ifPresent(WebDriverManager::setup);
                    isWebDriverInstalled = true;
                }

                var enhancer = new Enhancer();
                enhancer.setSuperclass(supportedWebDriver.getWebDriverClass());
                enhancer.setCallback(new WebDriverMethodInterceptor());

                var driver = (WebDriver) enhancer.create(c.getParameterTypes(), parameters);

                ofNullable(proxy).ifPresent(browserUpProxy -> {
                    if (browserUpProxy.isStarted()) {
                        browserUpProxy.enableHarCaptureTypes(REQUEST_HEADERS, REQUEST_CONTENT,
                                RESPONSE_HEADERS, RESPONSE_CONTENT);

                        browserUpProxy.newHar();
                    }
                });

                ofNullable(BASE_WEB_DRIVER_URL_PROPERTY.get())
                        .ifPresent(url -> driver.get(url.toString()));
                if (FORCE_WINDOW_MAXIMIZING_ON_START.get()) {
                    driver.manage().window().maximize();
                }

                driver.manage().timeouts().pageLoadTimeout(WAITING_FOR_PAGE_LOADED_DURATION.get().toMillis(),
                        MILLISECONDS);
                return driver;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void refreshContext() {
        if (driver == null) {
            return;
        }

        boolean isAlive;
        try {
            driver.getCurrentUrl();
            isAlive = true;
        } catch (WebDriverException e) {
            isAlive = false;
        }

        if (!isAlive) {
            driver = null;
            return;
        }

        if (!KEEP_WEB_DRIVER_SESSION_OPENED.get()) {
            driver.quit();
            driver = null;
            return;
        }

        if (CLEAR_WEB_DRIVER_COOKIES.get()) {
            driver.manage().deleteAllCookies();
        }

        if (GET_BACK_TO_BASE_URL.get()) {
            ofNullable(BASE_WEB_DRIVER_URL_PROPERTY.get()).ifPresent(url ->
                    driver.get(url.toString()));
        }
    }

    @Override
    public WebDriver getWrappedDriver() {
        initDriverIfNecessary();
        return driver;
    }

    public BrowserUpProxy getProxy() {
        return proxy;
    }

    public void shutDown() {
        ofNullable(driver).ifPresent(webDriver -> {
            ofNullable(proxy).ifPresent(browserUpProxy -> {
                if (browserUpProxy.isStarted()) {
                    browserUpProxy.abort();
                }
            });

            try {
                webDriver.quit();
            } catch (Throwable ignored) {
            }
        });

        shutDownServerLocally();
    }
}
