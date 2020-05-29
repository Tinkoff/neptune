package ru.tinkoff.qa.neptune.http.api.test.capturing;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.test.BaseHttpTest;

import java.net.URI;

import static java.lang.System.getProperties;
import static java.net.URI.create;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.test.capturing.LogInjector.clearLogs;
import static ru.tinkoff.qa.neptune.http.api.test.capturing.LogInjector.getLog;

public class RequestResponseMultiThreadLogCapturingTests extends BaseHttpTest {

    private static final URI CORRECT_URI = create(REQUEST_URI + "/success2.html");

    @BeforeClass
    public void beforeClass() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/success2.html"), unlimited())
                .respond(response()
                        .withBody("SUCCESS")
                        .withStatusCode(200));

        clearLogs();
    }

    @AfterClass
    public void afterClass() {
        getProperties().remove(DO_CAPTURES_OF_INSTANCE.getPropertyName());
    }

    @Test(threadPoolSize = 6, invocationCount = 6)
    public void test() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS.name());
        http().responseOf(GET(CORRECT_URI), ofString());
        assertThat(getLog(), hasItems(
                containsString("Logs that have been captured during the sending of a request"),
                equalTo("Response\n" +
                        "Status code: 200\r\n" +
                        "Response URI: http://127.0.0.1:1080/success2.html\r\n" +
                        "Corresponding request: http://127.0.0.1:1080/success2.html GET\r\n" +
                        "Response headers: {connection=[keep-alive], content-length=[7]}\r\n")));
    }
}
