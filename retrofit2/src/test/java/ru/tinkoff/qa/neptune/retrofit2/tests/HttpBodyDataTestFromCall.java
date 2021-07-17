package ru.tinkoff.qa.neptune.retrofit2.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.ApiService;
import ru.tinkoff.qa.neptune.retrofit2.steps.ExpectedHttpResponseHasNotBeenReceivedException;
import ru.tinkoff.qa.neptune.retrofit2.tests.retrofit.suppliers.GsonRetrofitBuilderSupplier;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.common.CallService;

import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector.injectValues;
import static ru.tinkoff.qa.neptune.retrofit2.RetrofitContext.retrofit;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitProperty.DEFAULT_RETROFIT_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitURLProperty.DEFAULT_RETROFIT_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetArraySupplier.arrayFromCall;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetIterableSupplier.iterableFromCall;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromArraySupplier.arrayItemFromCall;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromIterableSupplier.iterableItemFromCall;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.bodyFromCall;

public class HttpBodyDataTestFromCall extends BaseBodyDataTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static WireMockServer wireMockServer;
    @ApiService
    private CallService callService;

    @BeforeClass
    public static void preparation() {
        wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();
        configureFor(LOCALHOST, 8089);
        prepareMock();
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        DEFAULT_RETROFIT_URL_PROPERTY.accept(new URL("http://" + LOCALHOST + ":8089/"));
        DEFAULT_RETROFIT_PROPERTY.accept(GsonRetrofitBuilderSupplier.class);
        injectValues(this);
    }

    @Test
    public void objectFromBodyTest1() {
        var result = retrofit(callService).receive(bodyFromCall(CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size == 2", r -> r.size() == 2));

        assertThat(result, hasSize(2));
    }

    @Test
    public void objectFromBodyTest2() {
        var result = retrofit(callService).receive(bodyFromCall(CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size > 2", r -> r.size() > 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void objectFromBodyTest3() {
        try {
            retrofit(callService).receive(bodyFromCall(CallService::getJson)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size > 2", r -> r.size() > 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }


    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void objectFromBodyTest4() {
        try {
            retrofit(callService).receive(bodyFromCall(CallService::getXml)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size == 2", r -> r.size() == 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void objectFromBodyTest5() {
        var start = currentTimeMillis();
        retrofit(callService).receive(bodyFromCall(CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size > 2", r -> r.size() > 2)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void objectFromBodyTest6() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(bodyFromCall(CallService::getJson)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size > 2", r -> r.size() > 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void objectFromBodyTest7() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(bodyFromCall(CallService::getXml)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size == 2", r -> r.size() == 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }


    @Test
    public void getIterableTest1() {
        var result = retrofit(callService).receive(iterableFromCall(
                "Result list", CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2));

        assertThat(result, hasSize(2));
    }

    @Test
    public void getIterableTest2() {
        var result = retrofit(callService).receive(iterableFromCall(
                "Result list", CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getIterableTest3() {
        try {
            retrofit(callService).receive(iterableFromCall(
                    "Result list", CallService::getJson)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }


    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getIterableTest4() {
        try {
            retrofit(callService).receive(iterableFromCall(
                    "Result list", CallService::getXml)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableTest5() {
        var start = currentTimeMillis();
        retrofit(callService).receive(iterableFromCall(
                "Result list", CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getIterableTest6() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(iterableFromCall(
                    "Result list", CallService::getJson)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getIterableTest7() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(iterableFromCall(
                    "Result list", CallService::getXml)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }


    @Test
    public void getArrayTest1() {
        var result = retrofit(callService).receive(arrayFromCall(
                "Result array", CallService::getJsonArray)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2));

        assertThat(result, arrayWithSize(2));
    }

    @Test
    public void getArrayTest2() {
        var result = retrofit(callService).receive(arrayFromCall(
                "Result array", CallService::getJsonArray)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayTest3() {
        try {
            retrofit(callService).receive(arrayFromCall(
                    "Result array", CallService::getJsonArray)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }


    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayTest4() {
        try {
            retrofit(callService).receive(arrayFromCall(
                    "Result array", CallService::getXmlArray)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayTest5() {
        var start = currentTimeMillis();
        retrofit(callService).receive(arrayFromCall(
                "Result array", CallService::getJsonArray)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayTest6() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(arrayFromCall(
                    "Result array", CallService::getJsonArray)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayTest7() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(arrayFromCall(
                    "Result array", CallService::getXmlArray)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }


    @Test
    public void getFromIterableTest1() {
        var result = retrofit(callService).receive(iterableItemFromCall(
                "Result", CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getFromIterableTest2() {
        var result = retrofit(callService).receive(iterableItemFromCall(
                "Result", CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getFromIterableTest3() {
        try {
            retrofit(callService).receive(iterableItemFromCall(
                    "Result", CallService::getJson)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }


    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getFromIterableTest4() {
        try {
            retrofit(callService).receive(iterableItemFromCall(
                    "Result", CallService::getXml)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getFromIterableTest5() {
        var start = currentTimeMillis();
        retrofit(callService).receive(iterableItemFromCall(
                "Result", CallService::getJson)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getFromIterableTest6() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(iterableItemFromCall(
                    "Result", CallService::getJson)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getFromIterableTest7() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(iterableItemFromCall(
                    "Result", CallService::getXml)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }


    @Test
    public void getArrayItemTest1() {
        var result = retrofit(callService).receive(arrayItemFromCall(
                "Result", CallService::getJsonArray)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getArrayItemTest2() {
        var result = retrofit(callService).receive(arrayItemFromCall(
                "Result", CallService::getJsonArray)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayItemTest3() {
        try {
            retrofit(callService).receive(arrayItemFromCall(
                    "Result", CallService::getJsonArray)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }


    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayItemTest4() {
        try {
            retrofit(callService).receive(arrayItemFromCall(
                    "Result", CallService::getXmlArray)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2)
                    .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayItemTest5() {
        var start = currentTimeMillis();
        retrofit(callService).receive(arrayItemFromCall(
                "Result", CallService::getJsonArray)
                .responseCriteria(statusCode(200))
                .responseCriteria(headerValue("custom header", "true"))
                .responseCriteria(headerValueMatches("custom header", "Some"))
                .responseCriteria(message("Successful json"))
                .responseCriteria(messageMatches("Successful"))
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayItemTest6() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(arrayItemFromCall(
                    "Result", CallService::getJsonArray)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayItemTest7() {
        var start = currentTimeMillis();
        try {
            retrofit(callService).receive(arrayItemFromCall(
                    "Result", CallService::getXmlArray)
                    .responseCriteria(statusCode(200))
                    .responseCriteria(headerValue("custom header", "true"))
                    .responseCriteria(headerValueMatches("custom header", "Some"))
                    .responseCriteria(message("Successful json"))
                    .responseCriteria(messageMatches("Successful"))
                    .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2)
                    .retryTimeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .throwOnNoResult());
        } catch (Exception e) {
            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
            throw e;
        }

        fail("Exception was expected");
    }

    @AfterClass
    public void afterClass() {
        DEFAULT_RETROFIT_URL_PROPERTY.accept(null);
        DEFAULT_RETROFIT_PROPERTY.accept(null);
    }
}
