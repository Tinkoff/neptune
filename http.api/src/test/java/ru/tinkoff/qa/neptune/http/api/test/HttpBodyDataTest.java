package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.response.ExpectedHttpResponseHasNotBeenReceivedException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.System.currentTimeMillis;
import static java.net.URI.create;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.FileAssert.fail;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilderFactory.GET;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.*;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.test.FunctionToGetXMLTagArray.toNodeArray;
import static ru.tinkoff.qa.neptune.http.api.test.FunctionToGetXMLTagList.toNodeList;

public class HttpBodyDataTest extends BaseHttpTest {

    @BeforeClass
    public static void beforeClass() {
        stubFor(get(urlPathEqualTo("/data.html"))
            .willReturn(aResponse().withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>")));

        stubFor(get(urlPathEqualTo("/badData.html"))
            .willReturn(aResponse().withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?<b></b><c></c></a")));
        //malformed xml
    }

    @Test
    public void objectFromBodyTest1() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturn("List of tags <a>", toNodeList("a"))
            .criteria("Has 1 tag <a>", nodeList -> nodeList.size() == 1));

        assertThat(result, hasSize(1));
    }

    @Test
    public void objectFromBodyTest2() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturn("List of tags <a>", toNodeList("a"))
            .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void objectFromBodyTest3() {
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturn("List of tags <a>", toNodeList("a"))
            .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
            .throwOnNoResult());

        fail("Exception was expected");
    }

    @Test
    public void objectFromBodyTest4() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturn("List of tags <a>", toNodeList("a"))
            .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void objectFromBodyTest5() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/data.html")
                .responseBodyHandler(ofString())
                .tryToReturn("List of tags <a>", toNodeList("a"))
                .criteria("Has 2 tags <a>", nodeList -> nodeList.size() == 2)
                .responseCriteria(statusCode(404))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void objectFromBodyTest6() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturn("List of tags <a>", toNodeList("a"))
            .criteria("Has 1 tags <a>", nodeList -> nodeList.size() == 1)
            .responseCriteria(statusCode(404)));

        assertThat(result, nullValue());
    }

    @Test
    public void objectFromBodyTest7() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturn("List of tags <a>", toNodeList("a"))
            .criteria("Has 1 tags <a>", nodeList -> nodeList.size() == 1)
            .responseCriteria(statusCode(404))
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void objectFromBodyTest8() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(mapped(ofString(), toNodeList("a")))
            .tryToReturnBody()
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void objectFromBodyTest9() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/badData.html")
                .responseBodyHandler(mapped(ofString(), toNodeList("a")))
                .tryToReturnBody()
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableTest1() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnList("List of tags <a>", toNodeList("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, hasSize(1));
    }

    @Test
    public void getIterableTest2() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnList("List of tags <a>", toNodeList("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getIterableTest3() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(ofString())
            .tryToReturnList("List of tags <a>", toNodeList("a"))
            .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getIterableTest4() {
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnList("List of tags <a>", toNodeList("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .throwOnNoResult());

        fail("Exception was expected");
    }

    @Test
    public void getIterableTest5() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnList("List of tags <a>", toNodeList("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getIterableTest6() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/data.html")
                .responseBodyHandler(ofString())
                .tryToReturnList("List of tags <a>", toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                    s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>")))
                .throwOnNoResult());

        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getIterableTest7() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnList("List of tags <a>", toNodeList("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
            .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>"))));

        assertThat(result, nullValue());
    }

    @Test
    public void getIterableTest8() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnList("List of tags <a>", toNodeList("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
            .responseCriteria(bodyMatches("body != \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><a><b></b><c></c></a>\"",
                s -> !s.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>")))
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getIterableTest9() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(mapped(ofString(), toNodeList("a")))
            .tryToReturnList("List of tags <a>", ts -> ts)
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getIterableTest110() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/badData.html")
                .responseBodyHandler(mapped(ofString(), toNodeList("a")))
                .tryToReturnList("List of tags <a>", ts -> ts)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayTest1() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArray("Array of tags <a>", toNodeArray("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, arrayWithSize(1));
    }

    @Test
    public void getArrayTest2() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArray("Array of tags <a>", toNodeArray("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getArrayTest3() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(ofString())
            .tryToReturnArray("Array of tags <a>", toNodeArray("a"))
            .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayTest4() {
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArray("Array of tags <a>", toNodeArray("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .throwOnNoResult());

        fail("Exception was expected");
    }

    @Test
    public void getArrayTest5() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArray("Array of tags <a>", toNodeArray("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .retryTimeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayTest6() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/data.html")
                .responseBodyHandler(ofString())
                .tryToReturnArray("Array of tags <a>", toNodeArray("a"))
                .responseCriteria(responseURI(create("https://www.google.com/")))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getArrayTest7() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArray("Array of tags <a>", toNodeArray("a"))
            .responseCriteria(responseURI(create("https://www.google.com/"))));

        assertThat(result, nullValue());
    }

    @Test
    public void getArrayTest8() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArray("Array of tags <a>", toNodeArray("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
            .responseCriteria(responseURI(create("https://www.google.com/")))
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getArrayTest9() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(mapped(ofString(), toNodeArray("a")))
            .tryToReturnArray("Array of tags <a>", ts -> ts)
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getArrayTest10() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/badData.html")
                .responseBodyHandler(mapped(ofString(), toNodeArray("a")))
                .tryToReturnArray("Array of tags <a>", ts -> ts)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getOneFromIterableTest1() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnItem("Tag <a>", toNodeList("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromIterableTest2() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnItem("Tag <a>", toNodeList("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromIterableTest3() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(ofString())
            .tryToReturnItem("Tag <a>", toNodeList("a"))
            .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getOneFromIterableTest4() {
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnItem("Tag <a>", toNodeList("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .throwOnNoResult());
        fail("Exception was expected");
    }

    @Test
    public void getOneFromIterableTest5() {
        var start = currentTimeMillis();

        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnItem("Tag <a>", toNodeList("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .retryTimeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getOneFromIterableTest6() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/data.html")
                .responseBodyHandler(ofString())
                .tryToReturnItem("Tag <a>", toNodeList("a"))
                .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
                .responseCriteria(responseURIPort(200))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getOneFromIterableTest7() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnItem("Tag <a>", toNodeList("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
            .responseCriteria(responseURIPort(200)));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromIterableTest8() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnItem("Tag <a>", toNodeList("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0)
            .responseCriteria(responseURIPort(200))
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getOneFromIterableTest9() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(mapped(ofString(), toNodeList("a")))
            .tryToReturnItem("Tag <a>", nodes -> nodes)
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getOneFromIterableTest10() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/badData.html")
                .responseBodyHandler(mapped(ofString(), toNodeList("a")))
                .tryToReturnItem("Tag <a>", nodes -> nodes)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getOneFromArrayTest1() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArrayItem("Tag <a>", toNodeArray("a"))
            .criteria("Node has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, not(nullValue()));
    }

    @Test
    public void getOneFromArrayTest2() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArrayItem("Tag <a>", toNodeArray("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromArrayTest3() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(ofString())
            .tryToReturnArrayItem("Tag <a>", toNodeArray("a"))
            .criteria("Has children", node -> node.getChildNodes().getLength() > 0));

        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getOneFromArrayTest4() {
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArrayItem("Tag <a>", toNodeArray("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .throwOnNoResult());

        fail("Exception was expected");
    }

    @Test
    public void getOneFromArrayTest5() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArrayItem("Tag <a>", toNodeArray("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .retryTimeOut(ofSeconds(5)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getOneFromArrayTest6() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/data.html")
                .responseBodyHandler(ofString())
                .tryToReturnArrayItem("Tag <a>", toNodeArray("a"))
                .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
                .responseCriteria(responseURIPort(200))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), nullValue());
            throw e;
        }

        fail("Exception was expected");
    }

    @Test
    public void getOneFromArrayTest7() {
        var result = http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArrayItem("Tag <a>", toNodeArray("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .responseCriteria(responseURIPort(200)));

        assertThat(result, nullValue());
    }

    @Test
    public void getOneFromArrayTest11() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/data.html")
            .responseBodyHandler(ofString())
            .tryToReturnArrayItem("Tag <a>", toNodeArray("a"))
            .criteria("Has no children", node -> node.getChildNodes().getLength() == 0)
            .responseCriteria(responseURIPort(200))
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test
    public void getOneFromArrayTest12() {
        var start = currentTimeMillis();
        http().responseOf(GET()
            .baseURI(REQUEST_URI)
            .relativePath("/badData.html")
            .responseBodyHandler(mapped(ofString(), toNodeArray("a")))
            .tryToReturnArrayItem("Tag <a>", ts -> ts)
            .retryTimeOut(ofSeconds(5))
            .pollingInterval(ofMillis(500)));

        var stop = currentTimeMillis();
        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 850));
        assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
    }

    @Test(expectedExceptions = ExpectedHttpResponseHasNotBeenReceivedException.class)
    public void getOneFromArrayTest13() {
        try {
            http().responseOf(GET()
                .baseURI(REQUEST_URI)
                .relativePath("/badData.html")
                .responseBodyHandler(mapped(ofString(), toNodeArray("a")))
                .tryToReturnArrayItem("Tag <a>", ts -> ts)
                .retryTimeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500))
                .throwOnNoResult());
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(RuntimeException.class));
            throw e;
        }

        fail("Exception was expected");
    }
}