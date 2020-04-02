package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.response.DesiredDataHasNotBeenReceivedException;
import ru.tinkoff.qa.neptune.http.api.response.DesiredResponseHasNotBeenReceivedException;

import static java.lang.String.format;
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
import static ru.tinkoff.qa.neptune.http.api.request.GetRequest.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromArrayBodyStepSupplier.oneOfArray;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.object;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromIterableBodyStepSupplier.oneOfIterable;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromArrayBodyStepSupplier.array;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromIterableBodyStepSupplier.iterable;
import static ru.tinkoff.qa.neptune.http.api.response.GetResponseDataStepSupplier.bodyData;
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
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 1 tag <a>", nodeList -> nodeList.size() == 1));

        assertThat(result, hasSize(1));
    }

    @Test
    public void objectFromBodyTest2() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 1 tag <a>", nodeList -> nodeList.size() == 1));

        assertThat(result, hasSize(1));
    }

    @Test
    public void objectFromBodyTest3() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        assertThat(result, nullValue());
    }

    @Test
    public void objectFromBodyTest4() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void objectFromBodyTest5() {
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .throwWhenNothing("Test exception"));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void objectFromBodyTest6() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        http().get(bodyData(response,
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .throwWhenNothing("Test exception"));
        fail("Exception was expected");
    }

    @Test
    public void objectFromBodyTest7() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void objectFromBodyTest8() {
        try {
            http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                    object("List of tags <a>", toNodeList("a")))
                    .dataCriteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                    .responseCriteria(statusCode(404))
                    .throwWhenNothing("Test exception"));
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(DesiredResponseHasNotBeenReceivedException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void objectFromBodyTest9() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .responseCriteria(statusCode(404)));

        assertThat(result, nullValue());
    }

    @Test
    public void objectFromBodyTest10() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                object("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .responseCriteria(statusCode(404))
                .timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getIterableTest1() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getIterableTest2() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getIterableTest3() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyIterable());
    }

    @Test
    public void getIterableTest4() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyIterable());
    }

    @Test
    public void getIterableTest5() {
        var result = http().get(bodyData(GET(format("%s/badData.html", REQUEST_URI)), ofString(),
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getIterableTest6() {
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenNothing("Test exception"));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getIterableTest7() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        http().get(bodyData(response,
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenNothing("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void getIterableTest8() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getIterableTest9() {
        try {
            http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                    iterable("List of tags <a>", toNodeList("a")))
                    .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                    .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                            s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>")))
                    .throwWhenNothing("Test exception"));
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(DesiredResponseHasNotBeenReceivedException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableTest10() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                        s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>"))));

        assertThat(result, nullValue());
    }

    @Test
    public void getIterableTest11() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                iterable("List of tags <a>", toNodeList("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                        s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>")))
                .timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getArrayTest1() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, arrayWithSize(1));
    }

    @Test
    public void getArrayTest2() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, arrayWithSize(1));
    }

    @Test
    public void getArrayTest3() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyArray());
    }

    @Test
    public void getArrayTest4() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, emptyArray());
    }

    @Test
    public void getArrayTest5() {
        var result = http().get(bodyData(GET(format("%s/badData.html", REQUEST_URI)), ofString(),
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getArrayTest6() {
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenNothing("Test exception"));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getArrayTest7() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        http().get(bodyData(response,
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenNothing("Test exception"));

        fail("Exception was expected");
    }


    @Test
    public void getArrayTest8() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .timeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getArrayTest9() {
        try {
            http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                    array("Array of tags <a>", toNodeArray("a")))
                    .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                    .responseCriteria(responseURI(create("https://www.google.com/")))
                    .throwWhenNothing("Test exception"));
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(DesiredResponseHasNotBeenReceivedException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayTest10() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURI(create("https://www.google.com/"))));

        assertThat(result, nullValue());
    }

    @Test
    public void getArrayTest11() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                array("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURI(create("https://www.google.com/")))
                .timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getOneFromIterableTest1() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromIterableTest2() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromIterableTest3() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromIterableTest4() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromIterableTest5() {
        var result = http().get(bodyData(GET(format("%s/badData.html", REQUEST_URI)), ofString(),
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromIterableTest6() {
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenNothing("Test exception"));
        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromIterableTest7() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        http().get(bodyData(response,
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenNothing("Test exception"));

        fail("Exception was expected");
    }

    @Test
    public void getOneFromIterableTest8() {
        var start = currentTimeMillis();

        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .timeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromIterableTest9() {
        try {
            http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                    oneOfIterable("Tag <a>", toNodeList("a")))
                    .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                    .responseCriteria(responseURIPort(200))
                    .throwWhenNothing("Test exception"));
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(DesiredResponseHasNotBeenReceivedException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getOneFromIterableTest10() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURIPort(200)));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromIterableTest11() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfIterable("Tag <a>", toNodeList("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURIPort(200))
                .timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getOneFromArrayTest1() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfArray("Tag <a>", toNodeArray("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromArrayTest2() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                oneOfArray("Tag <a>", toNodeArray("a")))
                .dataCriteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromArrayTest3() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfArray("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromArrayTest4() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        var result = http().get(bodyData(response,
                oneOfArray("Tag <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromArrayTest5() {
        var result = http().get(bodyData(GET(format("%s/badData.html", REQUEST_URI)), ofString(),
                oneOfArray("Tag <a>", toNodeArray("a")))
                .dataCriteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromArrayTest6() {
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfArray("Tag <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenNothing("Test exception"));

        fail("Exception was expected");
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromArrayTest7() {
        var response = http().responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString());

        http().get(bodyData(response,
                oneOfArray("Tag <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .throwWhenNothing("Test exception"));
        fail("Exception was expected");
    }

    @Test
    public void getOneFromArrayTest8() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfArray("Tag <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .timeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = DesiredDataHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = ".*[Test exception]*")
    public void getOneFromArrayTest9() {
        try {
            http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                    oneOfArray("Array of tags <a>", toNodeArray("a")))
                    .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                    .responseCriteria(responseURIPort(200))
                    .throwWhenNothing("Test exception"));
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(DesiredResponseHasNotBeenReceivedException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getOneFromArrayTest10() {
        var result = http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfArray("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .responseCriteria(responseURIPort(200)));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromArrayTest11() {
        var start = currentTimeMillis();
        http().get(bodyData(GET(format("%s/data.html", REQUEST_URI)), ofString(),
                oneOfArray("Array of tags <a>", toNodeArray("a")))
                .dataCriteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .responseCriteria(responseURIPort(200))
                .timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 700));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }
}
