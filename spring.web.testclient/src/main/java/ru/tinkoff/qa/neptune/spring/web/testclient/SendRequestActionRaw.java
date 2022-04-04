package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
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
        return addExpectation(new ExpectedBodyJson(expectedJson).toString(), spec -> {
            bodyFormat.getBodyContentSpec().json(expectedJson);
            return true;
        });
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
        return addExpectation(new ExpectedBodyXml(expectedXml).toString(), spec -> {
            bodyFormat.getBodyContentSpec().xml(expectedXml);
            return true;
        });
    }

    /**
     * Defines specification how to check response body by json-path.
     *
     * @param expression is json-path expression
     * @param assertion  specification how to check something which is calculated by defined json-path
     * @param args       arguments to parameterize the json-path expression
     * @return self-reference
     */
    public SendRequestActionRaw expectBodyJsonPath(String expression, Function<JsonPathAssertions, WebTestClient.BodyContentSpec> assertion, Object... args) {
        return addExpectation(new ExpectJsonPath(expression, args).toString(),
                spec -> bodyFormat.getBodyContentSpec().jsonPath(expression, args),
                assertion);
    }

    /**
     * Evaluate the JSON path expression against the response body content
     * and assert that a non-null value, possibly an empty array or map, exists
     * at the given path.
     */
    public SendRequestActionRaw expectBodyJsonPath(String expression, Object... args) {
        return expectBodyJsonPath(expression, JsonPathAssertions::exists, args);
    }

    /**
     * Evaluate the JSON path expression against response body content
     * and assert that the result is equal to the expected value.
     */
    public SendRequestActionRaw expectBodyJsonPathEquals(String expression, Object expectedValue, Object... args) {
        return expectBodyJsonPath(expression, jsonPathAssertions -> jsonPathAssertions.isEqualTo(expectedValue), args);
    }

    /**
     * Evaluate the JSON path expression against the response body content
     * and assert the resulting value with the given {@code Matcher}.
     */
    public <T> SendRequestActionRaw expectBodyJsonPathMatches(String expression, Matcher<T> matcher, Object... args) {
        return expectBodyJsonPath(expression, jsonPathAssertions -> jsonPathAssertions.value(matcher), args);
    }

    /**
     * The same as {@link #expectBodyJsonPathMatches(String, Matcher, Object...)}. It also
     * accepts a target type for the resulting value. This can be useful for
     * matching numbers reliably for example coercing an integer into a double.
     */
    public <T> SendRequestActionRaw expectBodyJsonPathMatches(String expression, Class<T> targetType, Matcher<? super T> matcher, Object... args) {
        return expectBodyJsonPath(expression, jsonPathAssertions -> jsonPathAssertions.value(matcher, targetType), args);
    }

    /**
     * Defines specification how to check response body by xpath.
     *
     * @param expression is xpath expression
     * @param assertion  specification how to check something which is calculated by defined xpath
     * @param args       arguments to parameterize the xpath-path expression
     * @return self-reference
     */
    public SendRequestActionRaw expectBodyXpath(String expression,
                                                Function<XpathAssertions, WebTestClient.BodyContentSpec> assertion,
                                                Object... args) {
        return addExpectation(new ExpectXpath(expression, null, args).toString(),
                spec -> bodyFormat.getBodyContentSpec().xpath(expression, args),
                assertion);
    }

    /**
     * Defines specification how to check response body by xpath.
     *
     * @param expression expression is xpath expression
     * @param assertion  specification how to check something which is calculated by defined xpath
     * @param namespaces the namespaces to use
     * @param args       arguments to parameterize the xpath-path expression
     * @return self-reference
     */
    public SendRequestActionRaw expectBodyXpath(String expression,
                                                Function<XpathAssertions, WebTestClient.BodyContentSpec> assertion,
                                                Map<String, String> namespaces,
                                                Object... args) {
        return addExpectation(new ExpectXpath(expression, namespaces, args).toString(),
                spec -> bodyFormat.getBodyContentSpec().xpath(expression, namespaces, args),
                assertion);
    }

    /**
     * Apply the XPath expression and assert the resulting content exists.
     */
    public SendRequestActionRaw expectBodyXpath(String expression, Object... args) {
        return expectBodyXpath(expression, XpathAssertions::exists, args);
    }

    /**
     * Apply the XPath expression and assert the resulting content exists.
     */
    public SendRequestActionRaw expectBodyXpath(String expression,
                                                Map<String, String> namespaces,
                                                Object... args) {
        return expectBodyXpath(expression, XpathAssertions::exists, namespaces, args);
    }

    /**
     * Apply the XPath expression and assert the resulting content as a String.
     */
    public SendRequestActionRaw expectBodyXpathEquals(String expression,
                                                      String value,
                                                      Object... args) {
        return expectBodyXpath(expression, xpathAssertions -> xpathAssertions.isEqualTo(value), args);
    }

    /**
     * Apply the XPath expression and assert the resulting content as a String.
     */
    public SendRequestActionRaw expectBodyXpathEquals(String expression,
                                                      String value,
                                                      Map<String, String> namespaces,
                                                      Object... args) {
        return expectBodyXpath(expression, xpathAssertions -> xpathAssertions.isEqualTo(value), namespaces, args);
    }

    /**
     * Apply the XPath expression and assert the resulting content with the given Hamcrest matcher.
     */
    public SendRequestActionRaw expectBodyXpathMatches(String expression,
                                                       Matcher<? super String> matcher,
                                                       Object... args) {
        return expectBodyXpath(expression, xpathAssertions -> xpathAssertions.string(matcher), args);
    }

    /**
     * Apply the XPath expression and assert the resulting content with the given Hamcrest matcher.
     */
    public SendRequestActionRaw expectBodyXpathMatches(String expression,
                                                       Matcher<? super String> matcher,
                                                       Map<String, String> namespaces,
                                                       Object... args) {
        return expectBodyXpath(expression, xpathAssertions -> xpathAssertions.string(matcher), namespaces, args);
    }

    /**
     * Apply the XPath expression and assert the resulting content with the given Hamcrest matcher.
     */
    public SendRequestActionRaw expectBodyXpathNodeCount(String expression,
                                                         Matcher<? super Integer> matcher,
                                                         Object... args) {
        return expectBodyXpath(expression, xpathAssertions -> xpathAssertions.nodeCount(matcher), args);
    }

    /**
     * Apply the XPath expression and assert the resulting content with the given Hamcrest matcher.
     */
    public SendRequestActionRaw expectBodyXpathNodeCount(String expression,
                                                         Matcher<? super Integer> matcher,
                                                         Map<String, String> namespaces,
                                                         Object... args) {
        return expectBodyXpath(expression, xpathAssertions -> xpathAssertions.nodeCount(matcher), namespaces, args);
    }

    /**
     * Apply the XPath expression and assert the resulting content as an integer.
     */
    public SendRequestActionRaw expectBodyXpathNodeCount(String expression,
                                                         int count,
                                                         Object... args) {
        return expectBodyXpath(expression, xpathAssertions -> xpathAssertions.nodeCount(count), args);
    }

    /**
     * Apply the XPath expression and assert the resulting content as an integer.
     */
    public SendRequestActionRaw expectBodyXpathNodeCount(String expression,
                                                         int count,
                                                         Map<String, String> namespaces,
                                                         Object... args) {
        return expectBodyXpath(expression, xpathAssertions -> xpathAssertions.nodeCount(count), namespaces, args);
    }

    /**
     * Defines specification of empty body
     *
     * @return self-reference
     */
    public SendRequestActionRaw expectEmptyBody() {
        return addExpectation(new ExpectEmptyBody().toString(), spec -> {
            bodyFormat.getBodyContentSpec().isEmpty();
            return true;
        });
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
