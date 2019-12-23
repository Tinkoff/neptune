package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.DesiredResponseHasNotBeenReceivedException;
import ru.tinkoff.qa.neptune.http.api.ResponseHasNoDesiredDataException;

import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.System.*;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.time.Duration.ofSeconds;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.http.api.HttpGetObjectFromResponseBody.*;
import static ru.tinkoff.qa.neptune.http.api.HttpGetObjectsFromResponseBody.bodyArrayDataOf;
import static ru.tinkoff.qa.neptune.http.api.HttpGetObjectsFromResponseBody.bodyIterableDataOf;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseSequentialGetSupplier.responseOf;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest.GET;
import static ru.tinkoff.qa.neptune.http.api.properties.TimeToGetDesiredResponseProperty.DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.TimeToSleepProperty.SLEEP_RESPONSE_TIME_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.TimeUnitToGetDesiredResponseProperty.TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.TimeValueToGetDesiredResponseProperty.TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.test.FunctionToGetXMLTagArray.toNodeArray;
import static ru.tinkoff.qa.neptune.http.api.test.FunctionToGetXMLTagList.toNodeList;

public class HttpBodyDataTest extends BaseHttpTest {

    @BeforeClass
    public static void beforeClass() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/data.html"))
                .respond(response().withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>"));

        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/badData.html"))
                .respond(response().withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?<b></b><c></c></a"));
        //malformed xml
    }

    @Test
    public void getAnObjectFromBodyPositiveTest1() {
        var result = http().get(bodyDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "List of tags <a>", toNodeList("a"))

                .criteria("Has 1 tag <a>", nodeList -> nodeList.size() == 1));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getAnObjectFromBodyPositiveTest2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyDataOf(response,
                "List of tags <a>", toNodeList("a"))

                .criteria("Has 1 tag <a>", nodeList -> nodeList.size() == 1));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getAnObjectFromBodyNegativeTestWithNullResult1() {
        var result = http().get(bodyDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "List of tags <a>", toNodeList("a"))

                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        assertThat(result, nullValue());
    }

    @Test
    public void getAnObjectFromBodyNegativeTestWithNullResult2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyDataOf(response,
                "List of tags <a>", toNodeList("a"))

                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredResponseHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getAnObjectFromBodyNegativeTestWithExceptionThrowing1() {
        var exceptionToBeThrown = new DesiredResponseHasNotBeenReceivedException("Test exception");
        Supplier<DesiredResponseHasNotBeenReceivedException> supplier = () -> exceptionToBeThrown;

        http().get(bodyDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "List of tags <a>", toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .throwWhenResultEmpty(supplier));
        fail("Exception was expected");
    }

    @Test(expectedExceptions = ResponseHasNoDesiredDataException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getAnObjectFromBodyNegativeTestWithExceptionThrowing2() {
        var exceptionToBeThrown = new ResponseHasNoDesiredDataException("Test exception");
        Supplier<ResponseHasNoDesiredDataException> supplier = () -> exceptionToBeThrown;

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        http().get(bodyDataOf(response,
                "List of tags <a>", toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .throwWhenResultEmpty(supplier));
        fail("Exception was expected");
    }

    @Test
    public void getAnObjectFromBodyNegativeTestWithDefaultTimeOut1() {
        var start = currentTimeMillis();
        http().get(bodyDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "List of tags <a>", toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        assertThat(time, greaterThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()));
    }

    @Test
    public void getAnObjectFromBodyNegativeTestWithDefaultTimeOut2() {
        var start = currentTimeMillis();

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
        http().get(bodyDataOf(response, "List of tags <a>", toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
    }

    @Test
    public void getAnObjectFromBodyNegativeTestWithTimeDefinedInProperties1() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "List of tags <a>", toNodeList("a"))
                    .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(10).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(10).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getAnObjectFromBodyNegativeTestWithTimeDefinedInProperties2() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();

            var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
            http().get(bodyDataOf(response, "List of tags <a>", toNodeList("a"))
                    .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getAnObjectFromBodyNegativeTestWithTimeDefinedExplicitly() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "List of tags <a>", toNodeList("a"))
                    .timeOut(ofSeconds(5))
                    .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getSubIterableFromBodyPositiveTest1() {
        var result = http().get(bodyIterableDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "List of tags <a>", toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getSubIterableFromBodyPositiveTest2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyIterableDataOf(response, "List of tags <a>", toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getSubIterableFromBodyNegativeTestWithEmptyCollectionResult1() {
        var result = http().get(bodyIterableDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "List of tags <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getSubIterableFromBodyNegativeTestWithEmptyCollectionResult2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyIterableDataOf(response, "List of tags <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyIterable());
    }

    @Test
    public void getSubIterableFromBodyNegativeTestWithEmptyCollectionResult3() {
        var result = http().get(bodyIterableDataOf(responseOf(GET(format("%s/badData.html", REQUEST_URI)), ofString()),
                "List of tags <a>",
                toNodeList("a"))
                .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredResponseHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getSubIterableFromBodyNegativeTestWithExceptionThrowing1() {
        var exceptionToBeThrown = new DesiredResponseHasNotBeenReceivedException("Test exception");
        Supplier<DesiredResponseHasNotBeenReceivedException> supplier = () -> exceptionToBeThrown;

        http().get(bodyIterableDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "List of tags <a>",
                toNodeList("a"))

                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenResultEmpty(supplier));
        fail("Exception was expected");
    }

    @Test(expectedExceptions = ResponseHasNoDesiredDataException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getSubIterableFromBodyNegativeTestWithExceptionThrowing2() {
        var exceptionToBeThrown = new ResponseHasNoDesiredDataException("Test exception");
        Supplier<ResponseHasNoDesiredDataException> supplier = () -> exceptionToBeThrown;

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        http().get(bodyIterableDataOf(response, "List of tags <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenResultEmpty(supplier));
    }

    @Test
    public void getSubIterableFromBodyNegativeTestWithDefaultTimeOut1() {
        var start = currentTimeMillis();
        http().get(bodyIterableDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "List of tags <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        assertThat(time, greaterThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()));
    }

    @Test
    public void getSubIterableFromBodyNegativeTestWithDefaultTimeOut2() {
        var start = currentTimeMillis();

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
        http().get(bodyIterableDataOf(response, "List of tags <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
    }

    @Test
    public void getSubIterableFromBodyNegativeTestWithTimeDefinedInProperties1() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyIterableDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "List of tags <a>", toNodeList("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(10).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(10).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getSubIterableFromBodyNegativeTestWithTimeDefinedInProperties2() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();

            var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
            http().get(bodyIterableDataOf(response, "List of tags <a>", toNodeList("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getSubIterableFromBodyNegativeTestWithTimeDefinedExplicitly() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyIterableDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "List of tags <a>", toNodeList("a"))
                    .timeOut(ofSeconds(5))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getArrayFromBodyPositiveTest1() {
        var result = http().get(bodyArrayDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Array of tags <a>", toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, arrayWithSize(1));
    }

    @Test
    public void getArrayFromBodyPositiveTest2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyArrayDataOf(response, "Array of tags <a>", toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, arrayWithSize(1));
    }

    @Test
    public void getArrayFromBodyNegativeTestWithEmptyCollectionResult1() {
        var result = http().get(bodyArrayDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Array of tags <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getArrayFromBodyNegativeTestWithEmptyCollectionResult2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyArrayDataOf(response, "Array of tags <a>",
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyArray());
    }

    @Test
    public void getArrayFromBodyNegativeTestWithEmptyCollectionResult3() {
        var result = http().get(bodyArrayDataOf(responseOf(GET(format("%s/badData.html", REQUEST_URI)), ofString()),
                "Array of tags <a>",
                toNodeArray("a"))
                .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredResponseHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getArrayFromBodyNegativeTestWithExceptionThrowing1() {
        var exceptionToBeThrown = new DesiredResponseHasNotBeenReceivedException("Test exception");
        Supplier<DesiredResponseHasNotBeenReceivedException> supplier = () -> exceptionToBeThrown;

        http().get(bodyArrayDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Array of tags <a>",
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenResultEmpty(supplier));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = ResponseHasNoDesiredDataException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getArrayFromBodyNegativeTestWithExceptionThrowing2() {
        var exceptionToBeThrown = new ResponseHasNoDesiredDataException("Test exception");
        Supplier<ResponseHasNoDesiredDataException> supplier = () -> exceptionToBeThrown;

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        http().get(bodyArrayDataOf(response, "Array of tags <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenResultEmpty(supplier));
    }

    @Test
    public void getArrayFromBodyNegativeTestWithDefaultTimeOut1() {
        var start = currentTimeMillis();
        http().get(bodyArrayDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Array of tags <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        assertThat(time, greaterThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()));
    }

    @Test
    public void getArrayFromBodyNegativeTestWithDefaultTimeOut2() {
        var start = currentTimeMillis();

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
        http().get(bodyArrayDataOf(response, "Array of tags <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
    }

    @Test
    public void getArrayFromBodyNegativeTestWithTimeDefinedInProperties1() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyArrayDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "Array of tags <a>", toNodeArray("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(10).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(10).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getArrayFromBodyNegativeTestWithTimeDefinedInProperties2() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();

            var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
            http().get(bodyArrayDataOf(response, "Array of tags <a>", toNodeArray("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getArrayFromBodyNegativeTestWithTimeDefinedExplicitly() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyArrayDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "Array of tags <a>", toNodeArray("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                    .timeOut(ofSeconds(5)));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getObjectFromIterableBodyPositiveTest1() {
        var result = http().get(bodyDataFromIterable(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getObjectFromIterableBodyPositiveTest2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyDataFromIterable(response, "Tag <a>", toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getObjectFromIterableBodyNegativeTestWithEmptyCollectionResult1() {
        var result = http().get(bodyDataFromIterable(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getObjectFromIterableBodyNegativeTestWithEmptyCollectionResult2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyDataFromIterable(response, "Tag <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getObjectFromIterableBodyNegativeTestWithEmptyCollectionResult3() {
        var result = http().get(bodyDataFromIterable(responseOf(GET(format("%s/badData.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeList("a"))
                .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredResponseHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getObjectFromIterableBodyNegativeTestWithExceptionThrowing1() {
        var exceptionToBeThrown = new DesiredResponseHasNotBeenReceivedException("Test exception");
        Supplier<DesiredResponseHasNotBeenReceivedException> supplier = () -> exceptionToBeThrown;

        http().get(bodyDataFromIterable(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenResultEmpty(supplier));
        fail("Exception was expected");
    }

    @Test(expectedExceptions = ResponseHasNoDesiredDataException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getObjectFromIterableBodyNegativeTestWithExceptionThrowing2() {
        var exceptionToBeThrown = new ResponseHasNoDesiredDataException("Test exception");
        Supplier<ResponseHasNoDesiredDataException> supplier = () -> exceptionToBeThrown;

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        http().get(bodyDataFromIterable(response, "Tag <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenResultEmpty(supplier));
    }

    @Test
    public void getObjectFromIterableBodyNegativeTestWithDefaultTimeOut1() {
        var start = currentTimeMillis();
        http().get(bodyDataFromIterable(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        assertThat(time, greaterThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()));
    }

    @Test
    public void getObjectFromIterableBodyNegativeTestWithDefaultTimeOut2() {
        var start = currentTimeMillis();

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
        http().get(bodyDataFromIterable(response,
                "Tag <a>", toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
    }

    @Test
    public void getObjectFromIterableBodyNegativeTestWithTimeDefinedInProperties1() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyDataFromIterable(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "Tag <a>", toNodeList("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(10).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(10).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getObjectFromIterableBodyNegativeTestWithTimeDefinedInProperties2() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();

            var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
            http().get(bodyDataFromIterable(response,
                    "Tag <a>", toNodeList("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getObjectFromIterableBodyNegativeTestWithTimeDefinedExplicitly() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyDataFromIterable(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "Tag <a>", toNodeList("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                    .timeOut(ofSeconds(5)));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getObjectFromArrayBodyPositiveTest1() {
        var result = http().get(bodyDataFromArray(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getObjectFromArrayBodyPositiveTest2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyDataFromArray(response, "Tag <a>", toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getObjectFromArrayBodyNegativeTestWithEmptyCollectionResult1() {
        var result = http().get(bodyArrayDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Array of tags <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getObjectFromArrayBodyNegativeTestWithEmptyCollectionResult2() {
        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        var result = http().get(bodyDataFromArray(response, "Tag <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getObjectFromArrayBodyNegativeTestWithEmptyCollectionResult3() {
        var result = http().get(bodyDataFromArray(responseOf(GET(format("%s/badData.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeArray("a"))
                .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredResponseHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getObjectFromArrayBodyNegativeTestWithExceptionThrowing1() {
        var exceptionToBeThrown = new DesiredResponseHasNotBeenReceivedException("Test exception");
        Supplier<DesiredResponseHasNotBeenReceivedException> supplier = () -> exceptionToBeThrown;

        http().get(bodyDataFromArray(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenResultEmpty(supplier));
        fail("Exception was expected");
    }

    @Test(expectedExceptions = ResponseHasNoDesiredDataException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getObjectFromArrayBodyNegativeTestWithExceptionThrowing2() {
        var exceptionToBeThrown = new ResponseHasNoDesiredDataException("Test exception");
        Supplier<ResponseHasNoDesiredDataException> supplier = () -> exceptionToBeThrown;

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));

        http().get(bodyDataFromArray(response, "Tag <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenResultEmpty(supplier));
        fail("Exception was expected");
    }

    @Test
    public void getObjectFromArrayBodyNegativeTestWithDefaultTimeOut1() {
        var start = currentTimeMillis();
        http().get(bodyDataFromArray(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Tag <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        assertThat(time, greaterThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()));
    }

    @Test
    public void getObjectFromArrayBodyNegativeTestWithDefaultTimeOut2() {
        var start = currentTimeMillis();

        var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
        http().get(bodyDataFromArray(response, "Tag <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
    }

    @Test
    public void getObjectFromArrayBodyNegativeTestWithTimeDefinedInProperties1() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyDataFromArray(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "Tag <a>", toNodeArray("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(10).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(10).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getObjectFromArrayBodyNegativeTestWithTimeDefinedInProperties2() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();

            var response = http().get(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()));
            http().get(bodyDataFromArray(response, "Tag <a>", toNodeArray("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(1).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getObjectFromArrayBodyNegativeTestWithTimeDefinedExplicitly() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            http().get(bodyDataFromArray(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                    "Tag <a>", toNodeArray("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                    .timeOut(ofSeconds(5)));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis()
                    + SLEEP_RESPONSE_TIME_PROPERTY.get().toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void immutabilityOfRequestGettingObjectTest() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");
        try {
            var start = currentTimeMillis();

            var responseSpec = responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());
            http().get(bodyDataOf(responseSpec, "Tag <a>", toNodeList("a"))
                    .criteria("Has no children", nodes -> nodes.stream()
                            .anyMatch(node -> node.getChildNodes().getLength() == 0))
                    .criteriaOfResponse("Has status code 404",
                            stringHttpResponse -> stringHttpResponse.statusCode() == 404));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, greaterThan(ofSeconds(10).toMillis()));

            start = currentTimeMillis();
            var response = http().get(responseSpec);
            stop = currentTimeMillis();
            assertThat(response, not(nullValue()));
            time = stop - start;
            assertThat(time, lessThan(ofSeconds(1).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void immutabilityOfRequestGettingIterableTest() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");
        try {
            var start = currentTimeMillis();

            var responseSpec = responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());
            http().get(bodyIterableDataOf(responseSpec, "Tag <a>", toNodeList("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                    .criteriaOfResponse("Has status code 404",
                            stringHttpResponse -> stringHttpResponse.statusCode() == 404));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, greaterThan(ofSeconds(10).toMillis()));

            start = currentTimeMillis();
            var response = http().get(responseSpec);
            stop = currentTimeMillis();
            assertThat(response, not(nullValue()));
            time = stop - start;
            assertThat(time, lessThan(ofSeconds(1).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void immutabilityOfRequestGettingObjectFromIterableTest() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");
        try {
            var start = currentTimeMillis();

            var responseSpec = responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());
            http().get(bodyDataFromIterable(responseSpec, "Tag <a>", toNodeList("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                    .criteriaOfResponse("Has status code 404",
                            stringHttpResponse -> stringHttpResponse.statusCode() == 404));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, greaterThan(ofSeconds(10).toMillis()));

            start = currentTimeMillis();
            var response = http().get(responseSpec);
            stop = currentTimeMillis();
            assertThat(response, not(nullValue()));
            time = stop - start;
            assertThat(time, lessThan(ofSeconds(1).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void immutabilityOfRequestGettingArrayTest() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");
        try {
            var start = currentTimeMillis();

            var responseSpec = responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());
            http().get(bodyArrayDataOf(responseSpec, "Tag <a>", toNodeArray("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                    .criteriaOfResponse("Has status code 404",
                            stringHttpResponse -> stringHttpResponse.statusCode() == 404));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, greaterThan(ofSeconds(10).toMillis()));

            start = currentTimeMillis();
            var response = http().get(responseSpec);
            stop = currentTimeMillis();
            assertThat(response, not(nullValue()));
            time = stop - start;
            assertThat(time, lessThan(ofSeconds(1).toMillis()));
        } finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void immutabilityOfRequestGettingObjectFromArrayTest() {
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");
        try {
            var start = currentTimeMillis();

            var responseSpec = responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());
            http().get(bodyDataFromArray(responseSpec, "Tag <a>", toNodeArray("a"))
                    .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                    .criteriaOfResponse("Has status code 404",
                            stringHttpResponse -> stringHttpResponse.statusCode() == 404));

            var stop = currentTimeMillis();
            var time = stop - start;
            assertThat(time, greaterThan(ofSeconds(10).toMillis()));

            start = currentTimeMillis();
            var response = http().get(responseSpec);
            stop = currentTimeMillis();
            assertThat(response, not(nullValue()));
            time = stop - start;
            assertThat(time, lessThan(ofSeconds(1).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }
}
