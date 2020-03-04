package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.String.format;

public final class BrowserProxyHostProperty implements PropertySupplier<InetAddress> {

    /**
     * This property is used to specify the host on which the {@link net.lightbody.bmp.BrowserMobProxy}
     * proxy server will start. Do not forget to declare the {@code browser.proxy.port} along with
     * the current property, otherwise an attempt to initialize the {@link org.openqa.selenium.WebDriver} will
     * throw the {@link IllegalArgumentException}.
     */
    private static final String BROWSER_PROXY_HOST = "browser.proxy.host";

    public static final BrowserProxyHostProperty BROWSER_PROXY_HOST_PROPERTY = new BrowserProxyHostProperty();

    @Override
    public String getPropertyName() {
        return BROWSER_PROXY_HOST;
    }

    @Override
    public InetAddress get() {
        return returnOptionalFromEnvironment().map(host -> {
            try {
                return InetAddress.getByName(host);
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException(format("Host %s not resolved", host), e);
            }
        }).orElse(null);
    }
}
