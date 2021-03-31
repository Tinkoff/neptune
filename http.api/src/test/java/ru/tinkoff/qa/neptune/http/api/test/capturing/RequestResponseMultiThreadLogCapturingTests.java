package ru.tinkoff.qa.neptune.http.api.test.capturing;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.test.BaseHttpTest;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.System.getProperties;
import static java.net.URI.create;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.test.capturing.LogInjector.clearLogs;
import static ru.tinkoff.qa.neptune.http.api.test.capturing.LogInjector.getLog;

public class RequestResponseMultiThreadLogCapturingTests extends BaseHttpTest {

    private static final URI CORRECT_URI = create(REQUEST_URI + "/success2.html");
    private static final String LINE_SEPARATOR = "\r\n";

    @BeforeClass
    public void beforeClass() {
        stubFor(get(urlPathEqualTo("/success2.html"))
                .willReturn(aResponse().withBody("SUCCESS").withStatus(200)));

        clearLogs();
    }

    @AfterClass
    public void afterClass() {
        getProperties().remove(DO_CAPTURES_OF_INSTANCE.getName());
    }

    @Test(threadPoolSize = 6, invocationCount = 6)
    public void test() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);
        http().responseOf(GET(CORRECT_URI), ofString());
        assertThat(getLog(), hasItems(
                containsString("Logs that have been captured during the sending of a request"),
                containsString("Response\n" +
                        "Status code: 200" + LINE_SEPARATOR +
                        "Response URI: http://127.0.0.1:8089/success2.html" + LINE_SEPARATOR +
                        "Corresponding request: http://127.0.0.1:8089/success2.html GET" + LINE_SEPARATOR +
                        "Response headers:")));
    }
}
