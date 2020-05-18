package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;

import java.util.function.Consumer;

public class ChromeSettingsSupplierWithDefinedProxy implements CapabilitySettingSupplier<ChromeOptions> {

    @Override
    public Consumer<ChromeOptions> get() {
        return chromeOptions -> {
            Proxy tempProxy = ClientUtil.createSeleniumProxy(new BrowserUpProxyServer());

            chromeOptions.setCapability(CapabilityType.PROXY, tempProxy);
            chromeOptions.addArguments("--headless");
        };
    }
}

