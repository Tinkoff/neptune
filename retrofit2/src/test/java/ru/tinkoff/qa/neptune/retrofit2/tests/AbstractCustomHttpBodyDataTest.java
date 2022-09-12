package ru.tinkoff.qa.neptune.retrofit2.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.NotPresentException;
import ru.tinkoff.qa.neptune.retrofit2.steps.ExpectedHttpResponseHasNotBeenReceivedException;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.retrofit2.RetrofitContext.retrofit;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitProperty.DEFAULT_RETROFIT_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitURLProperty.DEFAULT_RETROFIT_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetArraySupplier.array;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetIterableSupplier.iterable;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromArraySupplier.arrayItem;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromIterableSupplier.iterableItem;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.body;

public abstract class AbstractCustomHttpBodyDataTest extends BaseBodyDataPreparing {

    abstract CustomService getService();

    @Test
    public void objectFromBodyTest1() {
        var result = retrofit().get(body(() -> getService().getJson())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size == 2", r -> r.size() == 2));

        assertThat(result, hasSize(2));
    }

    @Test
    public void objectFromBodyTest2() {
        var result = retrofit().get(body(() -> getService().getJson())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size > 2", r -> r.size() > 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = NotPresentException.class)
    public void objectFromBodyTest3() {
        try {
            retrofit().get(body(() -> getService().getJson())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(body(() -> getService().getXml())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        retrofit().get(body(() -> getService().getJson())
                .responseStatusCodeIs(400)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
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
            retrofit().get(body(() -> getService().getJson())
                    .responseStatusCodeIs(400)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(body(() -> getService().getXml())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        var result = retrofit().get(iterable("Result list",
                () -> getService().getJson())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2));

        assertThat(result, hasSize(2));
    }

    @Test
    public void getIterableTest2() {
        var result = retrofit().get(iterable("Result list",
                () -> getService().getJson())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2));

        assertThat(result, emptyIterable());
    }

    @Test(expectedExceptions = NotPresentException.class)
    public void getIterableTest3() {
        try {
            retrofit().get(iterable("Result list",
                    () -> getService().getJson())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(iterable("Result list",
                    () -> getService().getXml())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        retrofit().get(iterable("Result list",
                () -> getService().getJson())
                .responseStatusCodeIs(400)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
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
            retrofit().get(iterable("Result list",
                    () -> getService().getJson())
                    .responseStatusCodeIs(400)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(iterable("Result list",
                    () -> getService().getXml())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        var result = retrofit().get(array("Result array",
                () -> getService().getJsonArray())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2));

        assertThat(result, arrayWithSize(2));
    }

    @Test
    public void getArrayTest2() {
        var result = retrofit().get(array("Result array",
                () -> getService().getJsonArray())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2));

        assertThat(result, emptyArray());
    }

    @Test(expectedExceptions = NotPresentException.class)
    public void getArrayTest3() {
        try {
            retrofit().get(array("Result list",
                    () -> getService().getJsonArray())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(array("Result list",
                    () -> getService().getXmlArray())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        retrofit().get(array("Result list",
                () -> getService().getJsonArray())
                .responseStatusCodeIs(400)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
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
            retrofit().get(array("Result list",
                    () -> getService().getJsonArray())
                    .responseStatusCodeIs(400)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(array("Result list",
                    () -> getService().getXmlArray())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        var result = retrofit().get(iterableItem("Result",
                () -> getService().getJson())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getFromIterableTest2() {
        var result = retrofit().get(iterableItem("Result",
                () -> getService().getJson())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = NotPresentException.class)
    public void getFromIterableTest3() {
        try {
            retrofit().get(iterableItem("Result",
                    () -> getService().getJson())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(iterableItem("Result",
                    () -> getService().getXml())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        retrofit().get(iterableItem("Result",
                () -> getService().getJson())
                .responseStatusCodeIs(400)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
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
            retrofit().get(iterableItem("Result",
                    () -> getService().getJson())
                    .responseStatusCodeIs(400)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(iterableItem("Result",
                    () -> getService().getXml())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        var result = retrofit().get(arrayItem("Result",
                () -> getService().getJsonArray())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size of 'object' == 2", r -> r.getObject().size() == 2));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getArrayItemTest2() {
        var result = retrofit().get(arrayItem("Result",
                () -> getService().getJsonArray())
                .responseStatusCodeIs(200)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
                .criteria("Size of 'object' > 2", r -> r.getObject().size() > 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = NotPresentException.class)
    public void getArrayItemTest3() {
        try {
            retrofit().get(arrayItem("Result",
                    () -> getService().getJsonArray())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(arrayItem("Result",
                    () -> getService().getXmlArray())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
        retrofit().get(arrayItem("Result",
                () -> getService().getJsonArray())
                .responseStatusCodeIs(400)
                .responseHeaderValueIs("custom header", "true")
                .responseHeaderValueMatches("custom header", "Some")
                .responseMessageIs("Successful json")
                .responseMessageMatches("Successful")
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
            retrofit().get(arrayItem("Result",
                    () -> getService().getJsonArray())
                    .responseStatusCodeIs(400)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
            retrofit().get(arrayItem("Result",
                    () -> getService().getXmlArray())
                    .responseStatusCodeIs(200)
                    .responseHeaderValueIs("custom header", "true")
                    .responseHeaderValueMatches("custom header", "Some")
                    .responseMessageIs("Successful json")
                    .responseMessageMatches("Successful")
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
