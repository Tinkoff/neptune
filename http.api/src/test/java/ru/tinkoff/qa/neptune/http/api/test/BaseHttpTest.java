package ru.tinkoff.qa.neptune.http.api.test;

import org.mockserver.integration.ClientAndServer;
import org.testng.annotations.BeforeClass;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;

public class BaseHttpTest {

    static final String DOMAIN = "http://127.0.0.1";
    static final String REQUEST_URI = DOMAIN + ":1080";
    static ClientAndServer clientAndServer = startClientAndServer(1080);

    @BeforeClass
    public static void preparation() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE.name());
    }
}
