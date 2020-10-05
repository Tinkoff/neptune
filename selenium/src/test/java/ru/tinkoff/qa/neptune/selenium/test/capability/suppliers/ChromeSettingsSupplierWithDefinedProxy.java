package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import com.browserup.bup.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class ChromeSettingsSupplierWithDefinedProxy implements CapabilitySettingSupplier<ChromeOptions> {

    @Override
    public Consumer<ChromeOptions> get() {
        return chromeOptions -> {
            Proxy tempProxy = ClientUtil.createSeleniumProxy(new InetSocketAddress("127.0.0.1", 8089));

            chromeOptions.setCapability(CapabilityType.PROXY, tempProxy);
            chromeOptions.addArguments("--headless")
                    .addArguments("--disable-extensions")
                    .addArguments("--accept-insecure-localhost")
                    .addArguments("--ignore-certificate-errors");
        };
    }
}

