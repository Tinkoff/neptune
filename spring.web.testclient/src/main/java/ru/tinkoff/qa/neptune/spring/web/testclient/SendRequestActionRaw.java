package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.JsonPathAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.XpathAssertions;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.*;

import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.spring.web.testclient.BodySpecFunction.rawBody;

/**
 * Performs the sending of a request. Response body is read as raw byte-content.
 */
@Description("Send request and then get response")
public final class SendRequestActionRaw extends SendRequestAction<byte[], BodySpecFunction.RawBodySpecFunction, SendRequestActionRaw> {

    SendRequestActionRaw(Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec) {
        super(requestSpec, rawBody());
    }

    @Override
    void readBody() {
        bodyFormat.apply(responseSpec);
    }

    /**
     * Defines expected JSON-content. It performs a
     * "lenient" comparison verifying the same attribute-value pairs.
     * <p>Use of this option requires the
     * <a href="https://jsonassert.skyscreamer.org/">JSONassert</a> library
     * on to be on the classpath.
     *
     * @param expectedJson the expected JSON content.
     * @return self-reference
     */
    public SendRequestActionRaw expectBodyJson(String expectedJson) {
        return addExpectation(spec -> {
            bodyFormat.getBodyContentSpec().json(expectedJson);
            return true;
        }, new ExpectedBodyJson(expectedJson).toString());
    }

    /**
     * Defines expected XML-content. It asserts that
     * response body and defined xml-string are "similar", i.e.
     * they contain the same elements and attributes regardless of order.
     * <p>Use of this method requires the
     * <a href="https://github.com/xmlunit/xmlunit">XMLUnit</a> library on
     * the classpath.
     *
     * @param expectedXml the expected XML content.
     * @return self-reference
     */
    public SendRequestActionRaw expectBodyXml(String expectedXml) {
        return addExpectation(spec -> {
            bodyFormat.getBodyContentSpec().xml(expectedXml);
            return true;
        }, new ExpectedBodyXml(expectedXml).toString());
    }

    /**
     * Defines specification how to check response body by json-path.
     *
     * @param expression is json-path expression
     * @param assertion  specification how to check something which is calculated by defined json-path
     * @param args       arguments to parameterize the json-path expression
     * @param <T>        is a type of value returned by invocation of a method that belongs to {@link JsonPathAssertions}
     * @return self-reference
     */
    public <T> SendRequestActionRaw expectBodyJsonPath(String expression, Function<JsonPathAssertions, T> assertion, Object... args) {
        return addExpectation(spec -> {
            assertion.apply(bodyFormat.getBodyContentSpec().jsonPath(expression, args));
            return true;
        }, new ExpectJsonPath(expression, args).toString());
    }

    /**
     * Defines specification how to check response body by xpath.
     *
     * @param expression is xpath expression
     * @param assertion  specification how to check something which is calculated by defined xpath
     * @param args       arguments to parameterize the xpath-path expression
     * @param <T>        is a type of value returned by invocation of a method that belongs to {@link XpathAssertions}
     * @return self-reference
     */
    public <T> SendRequestActionRaw expectBodyXpath(String expression,
                                                    Function<XpathAssertions, T> assertion,
                                                    Object... args) {
        return addExpectation(spec -> {
            assertion.apply(bodyFormat.getBodyContentSpec().xpath(expression, args));
            return true;
        }, new ExpectXpath(expression, null, args).toString());
    }

    /**
     * Defines specification of empty body
     *
     * @return self-reference
     */
    public SendRequestActionRaw expectEmptyBody() {
        return addExpectation(spec -> {
            bodyFormat.getBodyContentSpec().isEmpty();
            return true;
        }, new ExpectEmptyBody().toString());
    }

    /**
     * Defines specification how to check response body by xpath.
     *
     * @param expression expression is xpath expression
     * @param assertion  specification how to check something which is calculated by defined xpath
     * @param namespaces the namespaces to use
     * @param args       arguments to parameterize the xpath-path expression
     * @param <T>        is a type of value returned by invocation of a method that belongs to {@link XpathAssertions}
     * @return self-reference
     */
    public <T> SendRequestActionRaw expectBodyXpath(String expression,
                                                    Function<XpathAssertions, T> assertion,
                                                    Map<String, String> namespaces,
                                                    Object... args) {
        return addExpectation(spec -> {
            assertion.apply(bodyFormat.getBodyContentSpec().xpath(expression, namespaces, args));
            return true;
        }, new ExpectXpath(expression, namespaces, args).toString());
    }

    /**
     * Creates a step that returns body of response as string content.
     *
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public GetObjectFromResponseBody<String> thenGetBodyAsString() {
        return thenGetValue(new BodyAsString().toString(), bytes -> ofNullable(bytes).map(String::new).orElse(null));
    }
}
