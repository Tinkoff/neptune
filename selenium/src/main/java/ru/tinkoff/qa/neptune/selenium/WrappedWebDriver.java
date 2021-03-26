package ru.tinkoff.qa.neptune.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.remote.server.SeleniumServer;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.selenium.authentication.AuthenticationPerformer;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;

import java.net.InetSocketAddress;
import java.net.URL;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.openqa.selenium.Proxy.ProxyType.MANUAL;
import static org.openqa.selenium.net.PortProber.findFreePort;
import static ru.tinkoff.qa.neptune.core.api.utils.ConstructorUtil.findSuitableConstructor;
import static ru.tinkoff.qa.neptune.selenium.BrowserProxy.getCurrentProxy;
import static ru.tinkoff.qa.neptune.selenium.content.management.ContentManagementCommand.getCurrentCommand;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.*;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.PROXY_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FOR_PAGE_LOADED_DURATION;

public class WrappedWebDriver implements WrapsDriver, ContextRefreshable {

    private final static String DEFAULT_LOCAL_HOST = "http://localhost:%s/wd/hub";
    private static SeleniumServer server;
    private static boolean serverStarted;
    private static URL serverUrl;

    private final SupportedWebDrivers supportedWebDriver;
    private WebDriver driver;
    private boolean isWebDriverInstalled;
    private final AuthenticationPerformer authenticationPerformer = new AuthenticationPerformer();

    public WrappedWebDriver(SupportedWebDrivers supportedWebDriver) {
        this.supportedWebDriver = supportedWebDriver;
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

    private synchronized boolean isNewSession() {
        if (isAlive()) {
            return false;
        }

        Object[] parameters;
        Object[] arguments = supportedWebDriver.get();

        if (USE_BROWSER_PROXY.get()) {
            var currentProxy = getCurrentProxy();
            var browserUpProxy = currentProxy.createProxy();
            MutableCapabilities capabilities = (MutableCapabilities) stream(arguments)
                    .filter(arg -> MutableCapabilities.class.isAssignableFrom(arg.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Browser mutable capabilities not found"));

            Proxy seleniumProxy = new Proxy();
            seleniumProxy.setProxyType(MANUAL);

            Object proxyCapability = capabilities.asMap().get(CapabilityType.PROXY);

            if (proxyCapability != null
                    && Proxy.class.isAssignableFrom(proxyCapability.getClass())
                    && ((Proxy) proxyCapability).getProxyType().equals(MANUAL)) {
                Proxy existingSeleniumProxy = (Proxy) proxyCapability;
                String[] proxyUrl = existingSeleniumProxy.getHttpProxy().split(":");

                browserUpProxy.setChainedProxy(new InetSocketAddress(proxyUrl[0], Integer.parseInt(proxyUrl[1])));
            } else {
                ofNullable(PROXY_URL_PROPERTY.get()).ifPresent(proxyUrl ->
                        browserUpProxy.setChainedProxy(new InetSocketAddress(proxyUrl.getHost(), proxyUrl.getPort())));
            }

            currentProxy.start();

            seleniumProxy.setHttpProxy(currentProxy.getHostIP() + ":" + currentProxy.getLocalPort());
            seleniumProxy.setSslProxy(currentProxy.getHostIP() + ":" + currentProxy.getLocalPort());

            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
            capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
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

            ofNullable(BASE_WEB_DRIVER_URL_PROPERTY.get())
                    .ifPresent(url -> driver.get(url.toString()));
            if (FORCE_WINDOW_MAXIMIZING_ON_START.get()) {
                driver.manage().window().maximize();
            }

            driver.manage().timeouts().pageLoadTimeout(WAITING_FOR_PAGE_LOADED_DURATION.get().toMillis(),
                    MILLISECONDS);

            authenticationPerformer.performAuthentication(driver, true);

            this.driver = driver;
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAlive() {
        if (driver == null) {
            return false;
        }

        try {
            driver.getCurrentUrl();
            return true;
        } catch (UnreachableBrowserException | NoSuchSessionException e1) {
            return false;
        } catch (WebDriverException e2) {
            return true;
        } catch (Exception e) {
            var cause = e.getCause();
            while (nonNull(cause)) {
                var causeClass = cause.getClass();
                if (causeClass.equals(UnreachableBrowserException.class) || causeClass.equals(NoSuchSessionException.class)) {
                    return false;
                }
                if (causeClass.equals(WebDriverException.class)) {
                    return true;
                }
                cause = cause.getCause();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void refreshContext() {
        boolean isAlive = isAlive();

        if (!isAlive || !KEEP_WEB_DRIVER_SESSION_OPENED.get()) {
            if (isAlive) {
                driver.quit();
            }
            driver = null;

            ofNullable(getCurrentProxy()).ifPresent(BrowserProxy::stop);
            return;
        }

        ofNullable(getCurrentProxy()).ifPresent(BrowserProxy::newHar);
    }

    @Override
    public synchronized WebDriver getWrappedDriver() {
        if (!isNewSession()) {
            authenticationPerformer.performAuthentication(driver, false);
        }

        driver.getWindowHandles();
        ofNullable(getCurrentCommand()).ifPresent(
                contentManagementCommand -> contentManagementCommand
                        .get()
                        .accept(driver)
        );

        return driver;
    }

    public void shutDown() {
        ofNullable(driver).ifPresent(webDriver -> {
            ofNullable(getCurrentProxy()).ifPresent(BrowserProxy::stop);

            try {
                webDriver.quit();
            } catch (Throwable ignored) {
            }
        });

        shutDownServerLocally();
    }
}
