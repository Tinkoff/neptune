package ru.tinkoff.qa.neptune.selenium;

import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.harreader.model.Har;
import org.openqa.selenium.net.NetworkUtils;

import static java.util.Optional.ofNullable;
import static org.openqa.selenium.net.PortProber.findFreePort;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;

/**
 * Wraps local proxy-server of a browser instance
 */
public final class BrowserProxy {

    private final static ThreadLocal<BrowserProxy> PROXIES = new ThreadLocal<>();

    private final String hostIP;
    private Integer localPort;
    private BrowserUpProxyServer proxy;

    private BrowserProxy() {
        hostIP = new NetworkUtils().getIp4NonLoopbackAddressOfThisMachine().getHostAddress();
        PROXIES.set(this);
    }

    /**
     * Gets an instance of {@link BrowserProxy} that is supposed to be used in current thread
     *
     * @return an instance of {@link BrowserProxy} when invocation of the 'get' method of
     * {@link ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties#USE_BROWSER_PROXY}
     * returns {@code true}. Otherwise it returns {@code null}
     */
    public static BrowserProxy getCurrentProxy() {
        if (USE_BROWSER_PROXY.get()) {
            return ofNullable(PROXIES.get()).orElseGet(() -> {
                var proxy = new BrowserProxy();
                PROXIES.set(proxy);
                return proxy;
            });
        }
        return null;
    }

    /**
     * Returns non loopback IP4 address of this machine
     *
     * @return non loopback IP4 address of this machine
     */
    public String getHostIP() {
        return hostIP;
    }

    /**
     * Returns port of the wrapped proxy-server
     *
     * @return port of the wrapped proxy-server
     */
    public int getLocalPort() {
        return localPort;
    }

    /**
     * Is the wrapped proxy-server started or not
     *
     * @return is wrapped proxy-server started or not
     */
    public synchronized boolean isStarted() {
        return proxy != null && proxy.isStarted();
    }

    /**
     * Creates a new proxy server if it is not created or returns one that is existing already
     *
     * @return an instance of {@link BrowserUpProxyServer}
     */
    public synchronized BrowserUpProxyServer createProxy() {
        proxy = ofNullable(proxy).orElseGet(() -> {
            var proxy = new BrowserUpProxyServer();
            proxy.setTrustAllServers(true);
            proxy.enableHarCaptureTypes(CaptureType.values());
            proxy.newHar();
            localPort = findFreePort();
            return proxy;
        });
        return proxy;
    }

    /**
     * Starts wrapped proxy server if it is not started
     */
    synchronized void start() {
        createProxy();
        if (!proxy.isStarted()) {
            proxy.newHar();
            proxy.start(localPort);
        }
    }

    /**
     * Stops wrapped proxy server if it is started
     */
    synchronized void stop() {
        if (isStarted()) {
            proxy.stop();
            proxy = null;
            localPort = null;
        }
    }

    /**
     * Drops old har-file and sets a new one instead.
     */
    void newHar() {
        proxy.newHar();
    }

    /**
     * Returns actual HAR
     *
     * @return actual HAR
     */
    public Har getHar() {
        return proxy.getHar();
    }
}
