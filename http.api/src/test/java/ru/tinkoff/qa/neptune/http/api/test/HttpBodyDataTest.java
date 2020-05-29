package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.response.DesiredDataHasNotBeenReceivedException;

import static java.lang.System.currentTimeMillis;
import static java.net.URI.create;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.testng.FileAssert.fail;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromArrayBodyStepSupplier.asOneOfArray;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asObject;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromIterableBodyStepSupplier.asOneOfIterable;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromArrayBodyStepSupplier.asArray;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromIterableBodyStepSupplier.asIterable;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.*;
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
    public void objectFromBodyTest1() {
        var result = http().bodyData(asObject("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has 1 tag <a>", nodeList -> nodeList.size() == 1));

        assertThat(result, hasSize(1));
    }

    @Test
    public void objectFromBodyTest2() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asObject(
                "List of tags <a>",
                response,
                toNodeList("a"))
                .criteria("Has 1 tag <a>", nodeList -> nodeList.size() == 1));

        assertThat(result, hasSize(1));
    }

    @Test
    public void objectFromBodyTest3() {
        var result = http().bodyData(asObject("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        assertThat(result, nullValue());
    }

    @Test
    public void objectFromBodyTest4() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asObject("List of tags <a>",
                response,
                toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void objectFromBodyTest5() {
        http().bodyData(asObject("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void objectFromBodyTest6() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        http().bodyData(asObject("List of tags <a>",
                response,
                toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void objectFromBodyTest7() {
        var start = currentTimeMillis();
        http().bodyData(asObject("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void objectFromBodyTest8() {
        http().bodyData(asObject("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .responseCriteria(statusCode(404))
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void objectFromBodyTest9() {
        var result = http().bodyData(asObject("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .responseCriteria(statusCode(404)));

        assertThat(result, nullValue());
    }

    @Test
    public void objectFromBodyTest10() {
        var start = currentTimeMillis();
        http().bodyData(asObject("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .responseCriteria(statusCode(404))
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getIterableTest1() {
        var result = http().bodyData(asIterable("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getIterableTest2() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asIterable("List of tags <a>",
                response,
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getIterableTest3() {
        var result = http().bodyData(asIterable("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyIterable());
    }

    @Test
    public void getIterableTest4() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asIterable("List of tags <a>",
                response,
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyIterable());
    }

    @Test
    public void getIterableTest5() {
        var result = http().bodyData(asIterable("List of tags <a>",
                GET(REQUEST_URI + "/badData.html"), ofString(),
                toNodeList("a"))
                .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getIterableTest6() {
        http().bodyData(asIterable("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getIterableTest7() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        http().bodyData(asIterable("List of tags <a>",
                response, toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void getIterableTest8() {
        var start = currentTimeMillis();
        http().bodyData(asIterable("List of tags <a>",
                GET(REQUEST_URI + "/data.html"), ofString(),
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getIterableTest9() {
        http().bodyData(asIterable("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                        s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>")))
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void getIterableTest10() {
        var result = http().bodyData(asIterable("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                        s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>"))));

        assertThat(result, nullValue());
    }

    @Test
    public void getIterableTest11() {
        var start = currentTimeMillis();
        http().bodyData(asIterable("List of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                        s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>")))
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getArrayTest1() {
        var result = http().bodyData(asArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"), ofString(),
                toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, arrayWithSize(1));
    }

    @Test
    public void getArrayTest2() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asArray("Array of tags <a>",
                response,
                toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, arrayWithSize(1));
    }

    @Test
    public void getArrayTest3() {
        var result = http().bodyData(asArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"), ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyArray());
    }

    @Test
    public void getArrayTest4() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asArray("Array of tags <a>",
                response,
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyArray());
    }

    @Test
    public void getArrayTest5() {
        var result = http().bodyData(asArray("Array of tags <a>",
                GET(REQUEST_URI + "/badData.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getArrayTest6() {
        http().bodyData(asArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getArrayTest7() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        http().bodyData(asArray("Array of tags <a>",
                response,
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }


    @Test
    public void getArrayTest8() {
        var start = currentTimeMillis();
        http().bodyData(asArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .retryTimeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getArrayTest9() {
        http().bodyData(asArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURI(create("https://www.google.com/")))
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void getArrayTest10() {
        var result = http().bodyData(asArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURI(create("https://www.google.com/"))));

        assertThat(result, nullValue());
    }

    @Test
    public void getArrayTest11() {
        var start = currentTimeMillis();
        http().bodyData(asArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURI(create("https://www.google.com/")))
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getOneFromIterableTest1() {
        var result = http().bodyData(asOneOfIterable("Tag <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromIterableTest2() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asOneOfIterable("Tag <a>",
                response,
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromIterableTest3() {
        var result = http().bodyData(asOneOfIterable("Tag <a>",
                GET(REQUEST_URI + "/data.html"), ofString(),
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromIterableTest4() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asOneOfIterable("Tag <a>",
                response,
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromIterableTest5() {
        var result = http().bodyData(asOneOfIterable("Tag <a>",
                GET(REQUEST_URI + "/badData.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromIterableTest6() {
        http().bodyData(asOneOfIterable("Tag <a>",
                GET(REQUEST_URI + "/data.html"), ofString(),
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwIfNoDesiredDataReceived("Test exception"));
        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromIterableTest7() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        http().bodyData(asOneOfIterable("Tag <a>",
                response,
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void getOneFromIterableTest8() {
        var start = currentTimeMillis();

        http().bodyData(asOneOfIterable("Tag <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .retryTimeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromIterableTest9() {
        http().bodyData(asOneOfIterable("Tag <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURIPort(200))
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void getOneFromIterableTest10() {
        var result = http().bodyData(asOneOfIterable("Tag <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURIPort(200)));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromIterableTest11() {
        var start = currentTimeMillis();
        http().bodyData(asOneOfIterable("Tag <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURIPort(200))
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getOneFromArrayTest1() {
        var result = http().bodyData(asOneOfArray("Tag <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromArrayTest2() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asOneOfArray("Tag <a>",
                response,
                toNodeArray("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromArrayTest3() {
        var result = http().bodyData(asOneOfArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromArrayTest4() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        var result = http().bodyData(asOneOfArray("Tag <a>",
                response,
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromArrayTest5() {
        var result = http().bodyData(asOneOfArray("Tag <a>",
                GET(REQUEST_URI + "/badData.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromArrayTest6() {
        http().bodyData(asOneOfArray("Tag <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromArrayTest7() {
        var response = http().responseOf(GET(REQUEST_URI + "/data.html"), ofString());

        http().bodyData(asOneOfArray("Tag <a>",
                response,
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwIfNoDesiredDataReceived("Test exception"));
        fail("Exception was expected");
    }

    @Test
    public void getOneFromArrayTest8() {
        var start = currentTimeMillis();
        http().bodyData(asOneOfArray("Tag <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .retryTimeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromArrayTest9() {
        http().bodyData(asOneOfArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .responseCriteria(responseURIPort(200))
                .throwIfNoDesiredDataReceived("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void getOneFromArrayTest10() {
        var result = http().bodyData(asOneOfArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .responseCriteria(responseURIPort(200)));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromArrayTest11() {
        var start = currentTimeMillis();
        http().bodyData(asOneOfArray("Array of tags <a>",
                GET(REQUEST_URI + "/data.html"),
                ofString(),
                toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .responseCriteria(responseURIPort(200))
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }
}
