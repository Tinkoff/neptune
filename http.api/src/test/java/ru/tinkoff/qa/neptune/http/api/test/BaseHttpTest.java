package ru.tinkoff.qa.neptune.http.api.test;

import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static java.util.Optional.ofNullable;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class BaseHttpTest {

    static final String DOMAIN = "http://127.0.0.1";
    static final String REQUEST_URI = DOMAIN + ":1080";
    static ClientAndServer clientAndServer;

    @BeforeSuite
    public static void beforeSuite() {
        clientAndServer = startClientAndServer(1080);
    }

    @AfterSuite
    public static void afterSuite() {
        ofNullable(clientAndServer).ifPresent(MockServerClient::stop);
    }
}
