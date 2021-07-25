package ru.tinkoff.qa.neptune.retrofit2.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.ApiService;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.UseRetrofitSettings;
import ru.tinkoff.qa.neptune.retrofit2.tests.retrofit.suppliers.SyncAdaptorRetrofitBuilderSupplier;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService4;

import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector.injectValues;
import static ru.tinkoff.qa.neptune.retrofit2.tests.CustomUrls.URL2;

public class CustomHttpBodyDataTest2 extends AbstractCustomHttpBodyDataTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static WireMockServer wireMockServer;
    @UseRetrofitSettings(SyncAdaptorRetrofitBuilderSupplier.class)
    @ApiService
    private CustomService4 service4;

    @BeforeClass
    public static void preparation() {
        wireMockServer = new WireMockServer(options().port(8091));
        wireMockServer.start();
        configureFor(LOCALHOST, 8091);
        prepareMock();
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        URL2.accept(new URL("http://" + LOCALHOST + ":8091/"));
        injectValues(this);
    }

    @AfterClass
    public void afterClass() {
        URL2.accept(null);
    }

    @Override
    CustomService getService() {
        return service4;
    }

    @Test
    public void objectFromBodyTest1() {
        super.objectFromBodyTest1();
    }
}
