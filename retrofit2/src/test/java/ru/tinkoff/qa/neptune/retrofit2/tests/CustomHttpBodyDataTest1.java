package ru.tinkoff.qa.neptune.retrofit2.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.ApiService;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService3;

import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector.injectValues;
import static ru.tinkoff.qa.neptune.retrofit2.tests.CustomUrls.URL1;

public class CustomHttpBodyDataTest1 extends AbstractCustomHttpBodyDataTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static WireMockServer wireMockServer;
    @ApiService
    private CustomService3 service3;

    @BeforeClass
    public static void preparation() {
        wireMockServer = new WireMockServer(options().port(8090));
        wireMockServer.start();
        configureFor(LOCALHOST, 8090);
        prepareMock();
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        URL1.accept(new URL("http://" + LOCALHOST + ":8090/"));
        injectValues(this);
    }

    @AfterClass
    public void afterClass() {
        URL1.accept(null);
    }

    @Override
    CustomService getService() {
        return service3;
    }

    @Test
    public void objectFromBodyTest1() {
        super.objectFromBodyTest1();
    }
}
