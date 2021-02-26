package ru.tinkoff.qa.neptune.http.api.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.System.setProperty;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;

public class BaseHttpTest {

    protected static final String LOCALHOST = "127.0.0.1";
    protected static final String REQUEST_URI = "http://" + LOCALHOST + ":8089";
    private static WireMockServer wireMockServer;

    static {
        //necessary to test response/request log
        setProperty("jdk.httpclient.HttpClient.log", "all");
    }

    @BeforeSuite
    public static void preparation() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();
        configureFor(LOCALHOST, 8089);
    }

    @AfterSuite
    public static void tearDown() {
        wireMockServer.stop();
    }
}
