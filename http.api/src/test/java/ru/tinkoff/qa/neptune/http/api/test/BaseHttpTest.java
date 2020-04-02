package ru.tinkoff.qa.neptune.http.api.test;

import org.mockserver.integration.ClientAndServer;
import org.testng.annotations.BeforeClass;

import static java.lang.System.setProperty;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;

public class BaseHttpTest {

    protected static final String DOMAIN = "http://127.0.0.1";
    protected static final String REQUEST_URI = DOMAIN + ":1080";
    protected static ClientAndServer clientAndServer = startClientAndServer(1080);

    static {
        //necessary to test response/request log
        setProperty("jdk.httpclient.HttpClient.log", "all");
    }

    @BeforeClass
    public static void preparation() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
    }
}
