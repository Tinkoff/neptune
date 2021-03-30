package ru.tinkoff.qa.neptune.http.api.test.capturing;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;
import ru.tinkoff.qa.neptune.http.api.response.DesiredDataHasNotBeenReceivedException;
import ru.tinkoff.qa.neptune.http.api.test.BaseHttpTest;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.http.Fault.MALFORMED_RESPONSE_CHUNK;
import static java.lang.System.getProperties;
import static java.net.URI.create;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asIs;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asObject;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.bodyMatches;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;
import static ru.tinkoff.qa.neptune.http.api.test.capturing.LogInjector.clearLogs;
import static ru.tinkoff.qa.neptune.http.api.test.capturing.LogInjector.getLog;

public class RequestResponseLogCapturingTests extends BaseHttpTest {

    private static final URI CORRECT_URI = create(REQUEST_URI + "/success.html");
    private static final URI INCORRECT_URI = create(REQUEST_URI + "/failure.html");
    private static final String LINE_SEPARATOR = "\r\n";

    @BeforeClass
    public void beforeClass() {
        stubFor(get(urlPathEqualTo("/success.html"))
                .willReturn(aResponse().withBody("SUCCESS").withStatus(200)));

        var randomByteArray = new byte[25];
        new Random().nextBytes(randomByteArray);

        stubFor(get(urlPathEqualTo("/failure.html"))
                .willReturn(serverError()
                        .withFault(MALFORMED_RESPONSE_CHUNK)
                        .withBody(randomByteArray)));
    }

    @BeforeMethod
    public void beforeEveryTest(Method m) {
        clearLogs();
    }

    @DataProvider
    public Object[][] data1() {
        return new Object[][]{
                {SUCCESS, containsInAnyOrder(
                        containsString("Logs that have been captured during the sending of a request"),
                        containsString("Response\n" +
                                "Status code: 200" + LINE_SEPARATOR +
                                "Response URI: http://127.0.0.1:8089/success.html" + LINE_SEPARATOR +
                                "Corresponding request: http://127.0.0.1:8089/success.html GET" + LINE_SEPARATOR +
                                "Response headers:"),
                        Matchers.equalTo("Response Body. String\n" +
                                "SUCCESS"))},
                {FAILURE, anyOf(empty(), nullValue())},
                {SUCCESS_AND_FAILURE, containsInAnyOrder(
                        containsString("Logs that have been captured during the sending of a request"),
                        containsString("Response\n" +
                                "Status code: 200" + LINE_SEPARATOR +
                                "Response URI: http://127.0.0.1:8089/success.html" + LINE_SEPARATOR +
                                "Corresponding request: http://127.0.0.1:8089/success.html GET" + LINE_SEPARATOR +
                                "Response headers:"),
                        Matchers.equalTo("Response Body. String\n" +
                                "SUCCESS"))}
        };
    }

    @DataProvider
    public Object[][] data2() {
        return new Object[][]{
                {SUCCESS, anyOf(empty(), nullValue())},
                {FAILURE, contains(containsString("Logs that have been captured during the sending of a request"))},
                {SUCCESS_AND_FAILURE, contains(containsString("Logs that have been captured during the sending of a request"))},
        };
    }

    @DataProvider
    public Object[][] data3() {
        return new Object[][]{
                {SUCCESS, anyOf(empty(), nullValue())},
                {FAILURE, containsInAnyOrder(
                        containsString("Logs that have been captured during the sending of a request"),
                        containsString("Response\n" +
                                "Status code: 200" + LINE_SEPARATOR +
                                "Response URI: http://127.0.0.1:8089/success.html" + LINE_SEPARATOR +
                                "Corresponding request: http://127.0.0.1:8089/success.html GET" + LINE_SEPARATOR +
                                "Response headers:"),
                        Matchers.equalTo("Response Body. String\n" +
                                "SUCCESS"))},
                {SUCCESS_AND_FAILURE, containsInAnyOrder(
                        containsString("Logs that have been captured during the sending of a request"),
                        containsString("Response\n" +
                                "Status code: 200" + LINE_SEPARATOR +
                                "Response URI: http://127.0.0.1:8089/success.html" + LINE_SEPARATOR +
                                "Corresponding request: http://127.0.0.1:8089/success.html GET" + LINE_SEPARATOR +
                                "Response headers:"),
                        Matchers.equalTo("Response Body. String\n" +
                                "SUCCESS"))},
        };
    }

    @DataProvider
    public Object[][] data4() {
        return new Object[][]{
                {SUCCESS, contains(containsString("Logs that have been captured during the sending of a request"))},
                {FAILURE, anyOf(empty(), nullValue())},
                {SUCCESS_AND_FAILURE, contains(containsString("Logs that have been captured during the sending of a request"))},
        };
    }

    @Test(dataProvider = "data1")
    public void test1(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);
        http().responseOf(GET(CORRECT_URI), ofString());
        assertThat(getLog(), matcher);
    }

    @Test(dataProvider = "data2", expectedExceptions = Exception.class)
    public void test2(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);
        try {
            http().responseOf(GET(INCORRECT_URI).timeout(ofSeconds(1)),
                    ofString());
        } catch (Throwable t) {
            assertThat(getLog(), matcher);
            throw t;
        }

        fail("Exception was expected");
    }

    @Test(dataProvider = "data1")
    public void test3(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);
        http().bodyData(asIs(GET(CORRECT_URI), ofString()));
        assertThat(getLog(), matcher);
    }

    @Test(dataProvider = "data1")
    public void test4(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);

        http().bodyData(asIs(GET(CORRECT_URI), ofString())
                .responseCriteria(bodyMatches("equals FAILURE", "FAILURE"::equals))
                .retryTimeOut(ofSeconds(5)));

        assertThat(getLog(), matcher);
    }

    @Test(dataProvider = "data3", expectedExceptions = DesiredDataHasNotBeenReceivedException.class)
    public void test5(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);

        try {
            http().bodyData(asIs(GET(CORRECT_URI), ofString())
                    .responseCriteria(bodyMatches("equals FAILURE", "FAILURE"::equals))
                    .retryTimeOut(ofSeconds(5))
                    .throwIfNoDesiredDataReceived("Test exception"));
        } catch (Throwable t) {
            assertThat(getLog(), matcher);
            throw t;
        }
        fail("Exception was expected");
    }

    @Test(dataProvider = "data3", expectedExceptions = DesiredDataHasNotBeenReceivedException.class)
    public void test6(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);

        try {
            http().bodyData(asIs(GET(CORRECT_URI), ofString())
                    .retryTimeOut(ofSeconds(5))
                    .responseCriteria(statusCode(404))
                    .throwIfNoDesiredDataReceived("Test exception"));
        } catch (Throwable t) {
            assertThat(getLog(), matcher);
            throw t;
        }
        fail("Exception was expected");
    }

    @Test(dataProvider = "data4")
    public void test7(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);

        http().bodyData(asIs(GET(INCORRECT_URI).timeout(ofSeconds(1)),
                ofString())
                .addIgnored(Throwable.class));

        assertThat(getLog(), matcher);
    }

    @Test(dataProvider = "data1")
    public void test8(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);

        http().bodyData(asObject("Number value",
                GET(CORRECT_URI),
                ofString(),
                Integer::parseInt)
                .retryTimeOut(ofSeconds(5))
                .addIgnored(Throwable.class));

        assertThat(getLog(), matcher);
    }

    @Test(dataProvider = "data3", expectedExceptions = DesiredDataHasNotBeenReceivedException.class)
    public void test9(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);

        try {
            http().bodyData(asObject("Number value", GET(CORRECT_URI), ofString(), Integer::parseInt)
                    .retryTimeOut(ofSeconds(5))
                    .addIgnored(Throwable.class)
                    .throwIfNoDesiredDataReceived("Test exception"));
        } catch (Throwable t) {
            assertThat(getLog(), matcher);
            throw t;
        }

        fail("Exception was expected");
    }

    @Test(dataProvider = "data2", expectedExceptions = DesiredDataHasNotBeenReceivedException.class)
    public void test10(CapturedEvents toCatch, Matcher<List<String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(toCatch);
        try {
            http().bodyData(asIs(GET(INCORRECT_URI).timeout(ofSeconds(1)), ofString())
                    .addIgnored(Throwable.class)
                    .retryTimeOut(ofSeconds(5))
                    .throwIfNoDesiredDataReceived("Test exception"));
        } catch (Throwable t) {
            assertThat(getLog(), matcher);
            throw t;
        }

        fail("Exception was expected");
    }

    @AfterMethod
    public void afterMethod() {
        getProperties().remove(DO_CAPTURES_OF_INSTANCE.getName());
    }
}
