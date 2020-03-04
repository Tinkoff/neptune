package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.integers.IntValuePropertySupplier;

public final class BrowserProxyPortProperty implements IntValuePropertySupplier {

    /**
     * This property is used to specify the port on which the {@link net.lightbody.bmp.BrowserMobProxy}
     * proxy server will start. This property must be declared if the {@code browser.proxy.host} property
     * is declared, otherwise an attempt to initialize the {@link org.openqa.selenium.WebDriver} will
     * throw the {@link IllegalArgumentException}.
     */
    private static final String BROWSER_PROXY_PORT = "browser.proxy.port";

    public static final BrowserProxyPortProperty BROWSER_PROXY_PORT_PROPERTY = new BrowserProxyPortProperty();

    @Override
    public String getPropertyName() {
        return BROWSER_PROXY_PORT;
    }
}
