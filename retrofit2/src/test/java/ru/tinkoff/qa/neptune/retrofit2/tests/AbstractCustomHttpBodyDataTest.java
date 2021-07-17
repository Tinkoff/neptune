package ru.tinkoff.qa.neptune.retrofit2.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.retrofit2.steps.ExpectedHttpResponseHasNotBeenReceivedException;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.retrofit2.RetrofitContext.retrofit;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitProperty.DEFAULT_RETROFIT_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitURLProperty.DEFAULT_RETROFIT_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetArraySupplier.array;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetIterableSupplier.iterable;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromArraySupplier.arrayItem;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromIterableSupplier.iterableItem;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.body;

public abstract class AbstractCustomHttpBodyDataTest extends BaseBodyDataTest {

    abstract CustomService getService();

    @Test
    public void objectFromBodyTest1() {
        var result = retrofit(getService()).receive(body(CustomService::getJson)
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
        var result = retrofit(getService()).receive(body(CustomService::getJson)
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
            retrofit(getService()).receive(body(CustomService::getJson)
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
            retrofit(getService()).receive(body(CustomService::getXml)
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
        retrofit(getService()).receive(body(CustomService::getJson)
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
            retrofit(getService()).receive(body(CustomService::getJson)
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
            retrofit(getService()).receive(body(CustomService::getXml)
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
        var result = retrofit(getService()).receive(iterable(
                "Result list", CustomService::getJson)
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
        var result = retrofit(getService()).receive(iterable(
                "Result list", CustomService::getJson)
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
            retrofit(getService()).receive(iterable(
                    "Result list", CustomService::getJson)
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
            retrofit(getService()).receive(iterable(
                    "Result list", CustomService::getXml)
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
        retrofit(getService()).receive(iterable(
                "Result list", CustomService::getJson)
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
            retrofit(getService()).receive(iterable(
                    "Result list", CustomService::getJson)
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
            retrofit(getService()).receive(iterable(
                    "Result list", CustomService::getXml)
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
        var result = retrofit(getService()).receive(array(
                "Result array", CustomService::getJsonArray)
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
        var result = retrofit(getService()).receive(array(
                "Result array", CustomService::getJsonArray)
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
            retrofit(getService()).receive(array(
                    "Result array", CustomService::getJsonArray)
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
            retrofit(getService()).receive(array(
                    "Result array", CustomService::getXmlArray)
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
        retrofit(getService()).receive(array(
                "Result array", CustomService::getJsonArray)
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
            retrofit(getService()).receive(array(
                    "Result array", CustomService::getJsonArray)
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
            retrofit(getService()).receive(array(
                    "Result array", CustomService::getXmlArray)
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
        var result = retrofit(getService()).receive(iterableItem(
                "Result", CustomService::getJson)
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
        var result = retrofit(getService()).receive(iterableItem(
                "Result", CustomService::getJson)
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
            retrofit(getService()).receive(iterableItem(
                    "Result", CustomService::getJson)
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
            retrofit(getService()).receive(iterableItem(
                    "Result", CustomService::getXml)
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
        retrofit(getService()).receive(iterableItem(
                "Result", CustomService::getJson)
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
            retrofit(getService()).receive(iterableItem(
                    "Result", CustomService::getJson)
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
            retrofit(getService()).receive(iterableItem(
                    "Result", CustomService::getXml)
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
        var result = retrofit(getService()).receive(arrayItem(
                "Result", CustomService::getJsonArray)
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
        var result = retrofit(getService()).receive(arrayItem(
                "Result", CustomService::getJsonArray)
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
            retrofit(getService()).receive(arrayItem(
                    "Result", CustomService::getJsonArray)
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
            retrofit(getService()).receive(arrayItem(
                    "Result", CustomService::getXmlArray)
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
        retrofit(getService()).receive(arrayItem(
                "Result", CustomService::getJsonArray)
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
            retrofit(getService()).receive(arrayItem(
                    "Result", CustomService::getJsonArray)
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
            retrofit(getService()).receive(arrayItem(
                    "Result", CustomService::getXmlArray)
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
