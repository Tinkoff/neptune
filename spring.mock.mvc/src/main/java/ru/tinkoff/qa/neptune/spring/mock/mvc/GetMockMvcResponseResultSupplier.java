package ru.tinkoff.qa.neptune.spring.mock.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hamcrest.Matcher;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.result.*;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.spring.mock.mvc.captors.RequestBodyStringCaptor;
import ru.tinkoff.qa.neptune.spring.mock.mvc.captors.ResponseBodyStringCaptor;
import ru.tinkoff.qa.neptune.spring.mock.mvc.captors.ResponseStringCaptor;
import ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer;
import ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers.*;

import javax.xml.xpath.XPathExpressionException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.list;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.ofclass.OfClassMatcher.isObjectOfClass;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable.ThrowableMessageMatcher.throwableHasMessage;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.CheckMockMvcExpectation.checkExpectation;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetArrayFromResponse.array;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetObjectFromResponseBody.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetObjectFromResponseBodyArray.objectFromArray;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetObjectFromResponseBodyIterable.objectFromIterable;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer.SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers.proxy.ResultMatcherProxyFactory.createResultMatcherFactoryProxy;

/**
 * This class is designed to create a step that receives a response and then gets some value
 */
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Retrieve:")
@Description("Response")
@ThrowWhenNoData(startDescription = "Response is not is as expected:")
public final class GetMockMvcResponseResultSupplier extends SequentialGetStepSupplier
        .GetSimpleStepSupplier<MockMvcContext, MockHttpServletResponse, GetMockMvcResponseResultSupplier> {

    private final GetAndCheckResponse getResponse;

    @CaptureOnSuccess(by = RequestBodyStringCaptor.class)
    @CaptureOnFailure(by = RequestBodyStringCaptor.class)
    String requestBody;

    @CaptureOnSuccess(by = ResponseBodyStringCaptor.class)
    @CaptureOnFailure(by = ResponseBodyStringCaptor.class)
    String responseBody;

    @CaptureOnSuccess(by = ResponseStringCaptor.class)
    @CaptureOnFailure(by = ResponseStringCaptor.class)
    MockHttpServletResponse response;

    private GetMockMvcResponseResultSupplier(GetAndCheckResponse originalFunction) {
        super(originalFunction);
        this.getResponse = originalFunction;
        throwOnNoResult();
    }

    /**
     * Creates a step that gets a response.
     *
     * @param builder is a request specification
     * @return an instance of GetMockMvcResponseResultSupplier
     */
    public static GetMockMvcResponseResultSupplier response(RequestBuilder builder) {
        return new GetMockMvcResponseResultSupplier(new GetAndCheckResponse(MockMvcContext::getDefaultMockMvc, builder));
    }

    /**
     * Creates a step that gets a response.
     *
     * @param mockMvc explicitly defined instance of {@link MockMvc}
     * @param builder is a request specification
     * @return an instance of GetMockMvcResponseResultSupplier
     */
    public static GetMockMvcResponseResultSupplier response(MockMvc mockMvc, RequestBuilder builder) {
        checkNotNull(mockMvc);
        return new GetMockMvcResponseResultSupplier(new GetAndCheckResponse(mockMvcContext -> mockMvc, builder));
    }

    private <T> GetMockMvcResponseResultSupplier addExpectation(T resultMatcherFactory, Function<T, ResultMatcher> createMatcher) {
        this.getResponse.addExpectation(createMatcher.apply(createResultMatcherFactoryProxy(resultMatcherFactory)));
        return this;
    }

    private GetMockMvcResponseResultSupplier addForwardURLExpectation(Function<NeptuneForwardedUrlResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(new NeptuneForwardedUrlResultMatchers(), createMatcher);
    }

    private GetMockMvcResponseResultSupplier addRedirectedURLExpectation(Function<NeptuneRedirectedUrlResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(new NeptuneRedirectedUrlResultMatchers(), createMatcher);
    }

    /**
     * Asserts the request was forwarded to the given URL.
     */
    public GetMockMvcResponseResultSupplier expectForward(String expectedUrl) {
        return addForwardURLExpectation(f -> f.forwardedUrl(expectedUrl));
    }

    /**
     * Asserts the request was forwarded to the given URL template.
     */
    public GetMockMvcResponseResultSupplier expectForwardTemplate(String urlTemplate, Object... uriVars) {
        return addForwardURLExpectation(f -> f.forwardedUrlTemplate(urlTemplate, uriVars));
    }

    /**
     * Asserts the request was forwarded to the given URL.
     */
    public GetMockMvcResponseResultSupplier expectForwardPattern(String urlPattern) {
        return addForwardURLExpectation(f -> f.forwardedUrlPattern(urlPattern));
    }

    /**
     * Asserts the request was redirected to the given URL.
     */
    public GetMockMvcResponseResultSupplier expectRedirect(String expectedUrl) {
        return addRedirectedURLExpectation(f -> f.redirectedUrl(expectedUrl));
    }

    /**
     * Asserts the request was redirected to the given URL template.
     */
    public GetMockMvcResponseResultSupplier expectRedirectTemplate(String urlTemplate, Object... uriVars) {
        return addRedirectedURLExpectation(f -> f.redirectedUrlTemplate(urlTemplate, uriVars));
    }

    /**
     * Asserts the request was redirected to the given URL.
     */
    public GetMockMvcResponseResultSupplier expectRedirectPattern(String urlPattern) {
        return addRedirectedURLExpectation(f -> f.redirectedUrlPattern(urlPattern));
    }

    /**
     * Evaluate the JSON path expression against the response and assert the result in fluent way
     *
     * @param expression    the JSON path expression, optionally parameterized with arguments
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code jsonMatchers -> jsonMatchers.value(value)}
     * @param jsonPathArgs  arguments to parameterize the JSON path expression with
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectJsonPath(String expression,
                                                           Function<JsonPathResultMatchers, ResultMatcher> createMatcher,
                                                           Object... jsonPathArgs) {
        return addExpectation(new NeptuneJsonPathResultMatchers(expression, jsonPathArgs), createMatcher);
    }

    /**
     * Evaluate the JSON path expression against the response content and assert
     * that a non-null value, possibly an empty array or map, exists at the given path.
     * If the JSON path expression is not definite, this method asserts that the value
     * at the given path is not empty.
     */
    public GetMockMvcResponseResultSupplier expectJsonPath(String expression,
                                                           Object... jsonPathArgs) {
        return expectJsonPath(expression, JsonPathResultMatchers::exists, jsonPathArgs);
    }

    /**
     * Evaluate the JSON path expression against the response content and assert that
     * the result is equal to the supplied value.
     */
    public GetMockMvcResponseResultSupplier expectJsonPathValue(String expression,
                                                                Object value,
                                                                Object... jsonPathArgs) {
        return expectJsonPath(expression, jsonPath -> jsonPath.value(value), jsonPathArgs);
    }

    /**
     * Evaluate the JSON path expression against the response content and assert
     * the resulting value with the given Hamcrest {@link Matcher}
     */
    public <T> GetMockMvcResponseResultSupplier expectJsonPathValue(String expression,
                                                                    Matcher<? super T> matcher,
                                                                    Object... jsonPathArgs) {
        return expectJsonPath(expression, jsonPath -> jsonPath.value(matcher), jsonPathArgs);
    }

    /**
     * Evaluate the JSON path expression against the response content and assert the resulting value
     * is read into value of expected type and suits the given matcher.
     * <p>
     * This can be useful for matching numbers reliably — for example, to coerce an integer into a double.
     */
    public <T> GetMockMvcResponseResultSupplier expectJsonPath(String expression,
                                                               Matcher<? super T> matcher,
                                                               Class<T> targetType,
                                                               Object... jsonPathArgs) {
        return expectJsonPath(expression, jsonPath -> jsonPath.value(matcher, targetType), jsonPathArgs);
    }

    /**
     * Evaluate the xPath expression against the response and assert the result in fluent way
     *
     * @param expression    the XPath expression, optionally parameterized with arguments
     * @param namespaces    the namespaces referenced in the XPath expression
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code xpathMatchers -> xpathMatchers.string("someString")}
     * @param xPathArgs     arguments to parameterize the XPath expression with
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectXPath(String expression,
                                                        Map<String, String> namespaces,
                                                        Function<XpathResultMatchers, ResultMatcher> createMatcher,
                                                        Object... xPathArgs) {
        try {
            return addExpectation(new NeptuneXpathResultMatchers(expression, namespaces, xPathArgs), createMatcher);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Evaluate the xPath expression against the response and assert the result in fluent way
     *
     * @param expression    the XPath expression, optionally parameterized with arguments
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code xpathMatchers -> xpathMatchers.string("someString")}
     * @param xPathArgs     arguments to parameterize the XPath expression with
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectXPath(String expression,
                                                        Function<XpathResultMatchers, ResultMatcher> createMatcher,
                                                        Object... xPathArgs) {
        try {
            return addExpectation(new NeptuneXpathResultMatchers(expression, null, xPathArgs), createMatcher);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Evaluate the XPath and assert that content exists.
     */
    public GetMockMvcResponseResultSupplier expectXPath(String expression,
                                                        Map<String, String> namespaces,
                                                        Object... xPathArgs) {
        return expectXPath(expression, namespaces, XpathResultMatchers::exists, xPathArgs);
    }

    /**
     * Evaluate the XPath and assert that content exists.
     */
    public GetMockMvcResponseResultSupplier expectXPath(String expression,
                                                        Object... xPathArgs) {
        return expectXPath(expression, XpathResultMatchers::exists, xPathArgs);
    }

    /**
     * Evaluate the XPath and assert the {@link Node} content found with the given Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectXPathNode(String expression,
                                                            Map<String, String> namespaces,
                                                            Matcher<? super Node> matcher,
                                                            Object... xPathArgs) {
        return expectXPath(expression, namespaces, xPath -> xPath.node(matcher), xPathArgs);
    }

    /**
     * Evaluate the XPath and assert the {@link Node} content found with the given Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectXPathNode(String expression,
                                                            Matcher<? super Node> matcher,
                                                            Object... xPathArgs) {
        return expectXPath(expression, xPath -> xPath.node(matcher), xPathArgs);
    }

    /**
     * Evaluate the XPath and assert the {@link NodeList} content found with the given Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectXPathNodeList(String expression,
                                                                Map<String, String> namespaces,
                                                                Matcher<? super NodeList> matcher,
                                                                Object... xPathArgs) {
        return expectXPath(expression, namespaces, xPath -> xPath.nodeList(matcher), xPathArgs);
    }

    /**
     * Evaluate the XPath and assert the {@link NodeList} content found with the given Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectXPathNodeList(String expression,
                                                                Matcher<? super NodeList> matcher,
                                                                Object... xPathArgs) {
        return expectXPath(expression, xPath -> xPath.nodeList(matcher), xPathArgs);
    }

    /**
     * Evaluate the XPath and assert the number of nodes found.
     */
    public GetMockMvcResponseResultSupplier expectXPathNodeCount(String expression,
                                                                 Map<String, String> namespaces,
                                                                 int count,
                                                                 Object... xPathArgs) {
        return expectXPath(expression, namespaces, xPath -> xPath.nodeCount(count), xPathArgs);
    }

    /**
     * Evaluate the XPath and assert the number of nodes found.
     */
    public GetMockMvcResponseResultSupplier expectXPathNodeCount(String expression,
                                                                 int count,
                                                                 Object... xPathArgs) {
        return expectXPath(expression, xPath -> xPath.nodeCount(count), xPathArgs);
    }

    /**
     * Asserts the response status in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code statusMatchers -> statusMatchers.is2xxSuccessful()}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectStatus(Function<StatusResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(status(), createMatcher);
    }

    /**
     * Assert the response status code is equal to an integer value
     */
    public GetMockMvcResponseResultSupplier expectStatus(int expected) {
        return expectStatus(status -> status.is(expected));
    }

    /**
     * Assert the response status code with the given Hamcrest {@link Matcher}
     */
    public GetMockMvcResponseResultSupplier expectStatus(Matcher<? super Integer> matcher) {
        return expectStatus(status -> status.is(matcher));
    }

    /**
     * Assert the response status code is in the 1xx range.
     */
    public GetMockMvcResponseResultSupplier expectStatusIs1xxInformational() {
        return expectStatus(StatusResultMatchers::is1xxInformational);
    }

    /**
     * Assert the response status code is in the 2xx range.
     */
    public GetMockMvcResponseResultSupplier expectStatusIs2xxSuccessful() {
        return expectStatus(StatusResultMatchers::is2xxSuccessful);
    }

    /**
     * Assert the response status code is in the 3xx range.
     */
    public GetMockMvcResponseResultSupplier expectStatusIs3xxRedirection() {
        return expectStatus(StatusResultMatchers::is3xxRedirection);
    }

    /**
     * Assert the response status code is in the 4xx range.
     */
    public GetMockMvcResponseResultSupplier expectStatusIs4xxClientError() {
        return expectStatus(StatusResultMatchers::is4xxClientError);
    }

    /**
     * Assert the response status code is in the 5xx range.
     */
    public GetMockMvcResponseResultSupplier expectStatusIs5xxServerError() {
        return expectStatus(StatusResultMatchers::is5xxServerError);
    }

    /**
     * Assert the Servlet response error message.
     */
    public GetMockMvcResponseResultSupplier expectStatusReason(String reason) {
        return expectStatus(statusResultMatchers -> statusResultMatchers.reason(reason));
    }

    /**
     * Assert the Servlet response error message with the given Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectStatusReason(Matcher<? super String> matcher) {
        return expectStatus(statusResultMatchers -> statusResultMatchers.reason(matcher));
    }

    /**
     * Asserts the request in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code requestMatchers -> requestMatchers.asyncStarted()}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectRequest(Function<RequestResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(request(), createMatcher);
    }

    /**
     * Asserts view in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code viewMatchers -> viewMatchers.name(name)}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectView(Function<ViewResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(view(), createMatcher);
    }

    /**
     * Assert the selected view name.
     */
    public GetMockMvcResponseResultSupplier expectViewName(String name) {
        return expectView(viewResultMatchers -> viewResultMatchers.name(name));
    }

    /**
     * Assert the selected view name with the given Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectViewName(Matcher<? super String> nameMatcher) {
        return expectView(viewResultMatchers -> viewResultMatchers.name(nameMatcher));
    }

    /**
     * Asserts model in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code modelMatchers -> modelMatchers.attribute(attribute, value)}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectModel(Function<ModelResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(model(), createMatcher);
    }

    /**
     * Assert a model attribute value.
     */
    public GetMockMvcResponseResultSupplier expectModelAttribute(String attribute, Object value) {
        return expectModel(model -> model.attribute(attribute, value));
    }

    /**
     * Assert a model attribute value with the given Hamcrest {@link Matcher}.
     */
    public <T> GetMockMvcResponseResultSupplier expectModelAttribute(String attribute, Matcher<? super T> matcher) {
        return expectModel(model -> model.attribute(attribute, matcher));
    }

    /**
     * Assert the given model attributes exist.
     */
    public GetMockMvcResponseResultSupplier expectModelAttributes(String... attributes) {
        return expectModel(model -> model.attributeExists(attributes));
    }

    /**
     * Asserts flash attributes in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code flashMatchers -> flashMatchers.attribute(attribute, value)}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectFlash(Function<FlashAttributeResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(flash(), createMatcher);
    }

    /**
     * Assert a flash attribute's value.
     */
    public GetMockMvcResponseResultSupplier expectFlashAttribute(String attribute, Object value) {
        return expectFlash(flash -> flash.attribute(attribute, value));
    }

    /**
     * Assert a flash attribute's value with the given Hamcrest {@link Matcher}.
     */
    public <T> GetMockMvcResponseResultSupplier expectFlashAttribute(String attribute, Matcher<? super T> matcherValue) {
        return expectFlash(flash -> flash.attribute(attribute, matcherValue));
    }

    /**
     * Assert the existence of the given flash attributes.
     */
    public GetMockMvcResponseResultSupplier expectFlashAttributes(String... names) {
        return expectFlash(flash -> flash.attributeExists(names));
    }

    /**
     * Assert the number of flash attributes.
     */
    public GetMockMvcResponseResultSupplier expectFlashAttributeCount(int count) {
        return expectFlash(flash -> flash.attributeCount(count));
    }

    /**
     * Asserts handlers in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code handlersMatchers -> handlersMatchers.handlerType(handlerType)}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectHandler(Function<HandlerResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(handler(), createMatcher);
    }

    /**
     * Assert the type of the handler that processed the request.
     */
    public <T> GetMockMvcResponseResultSupplier expectHandlerType(Class<T> handlerType) {
        return expectHandler(handler -> handler.handlerType(handlerType));
    }

    /**
     * Assert the controller method used to process the request.
     */
    public GetMockMvcResponseResultSupplier expectHandlerMethod(Method method) {
        return expectHandler(handler -> handler.method(method));
    }

    /**
     * Assert the name of the controller method used to process the request.
     */
    public GetMockMvcResponseResultSupplier expectHandlerMethod(String methodName) {
        return expectHandler(handler -> handler.methodName(methodName));
    }

    /**
     * Assert the name of the controller method used to process the request
     * using the given Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectHandlerMethod(Matcher<? super String> methodNameMatcher) {
        return expectHandler(handler -> handler.methodName(methodNameMatcher));
    }

    /**
     * Assert the controller method used to process the request.
     * <p>The expected method is specified through a "mock" controller method
     * invocation similar to {@link MvcUriComponentsBuilder#fromMethodCall(Object)}.
     *
     * @param handlerType   is a type of expected handler
     * @param getCallResult describes expected invocation of a method of the handler.
     *                      The method which is expected to be invoked should return some value
     * @param <T>           is a type of expected handler
     */
    public <T, R> GetMockMvcResponseResultSupplier expectHandlerCall(Class<T> handlerType, Function<T, R> getCallResult) {
        return expectHandler(handler -> handler.methodCall(getCallResult.apply(on(handlerType))));
    }

    /**
     * Asserts header in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code headerMatchers -> headerMatchers.string(headerName, value)}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectHeader(Function<HeaderResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(header(), createMatcher);
    }

    /**
     * Assert that the named response header exists.
     */
    public GetMockMvcResponseResultSupplier expectHeader(String headerName) {
        return expectHeader(header -> header.exists(headerName));
    }

    /**
     * Assert the primary value of the response header as a String value.
     */
    public GetMockMvcResponseResultSupplier expectHeaderValue(String headerName, String value) {
        return expectHeader(header -> header.string(headerName, value));
    }

    /**
     * Assert the primary value of the response header with the given Hamcrest
     * String {@code Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectHeaderValue(String headerName,
                                                              Matcher<? super String> valueMatcher) {
        return expectHeader(header -> header.string(headerName, valueMatcher));
    }

    /**
     * Assert the values of the response header as String values.
     */
    public GetMockMvcResponseResultSupplier expectHeaderValues(String headerName, String... values) {
        return expectHeader(header -> header.stringValues(headerName, values));
    }

    /**
     * Assert the values of the response header with the given Hamcrest
     * Iterable {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectHeaderValues(String headerName, Matcher<? super Iterable<String>> valuesMatcher) {
        return expectHeader(header -> header.stringValues(headerName, valuesMatcher));
    }

    /**
     * Asserts content in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code contentMatchers -> contentMatchers.string(expectedString)}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectContent(Function<ContentResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(content(), createMatcher);
    }

    /**
     * Assert the ServletResponse content type. The given content type must
     * fully match including type, sub-type, and parameters
     */
    public GetMockMvcResponseResultSupplier expectContentType(String contentType) {
        return expectContent(content -> content.contentType(contentType));
    }

    /**
     * Assert the ServletResponse content type after parsing it as a MediaType.
     * The given content type must fully match including type, sub-type, and
     * parameters.
     */
    public GetMockMvcResponseResultSupplier expectContentType(MediaType contentType) {
        return expectContent(content -> content.contentType(contentType));
    }

    /**
     * Assert the character encoding in the ServletResponse.
     */
    public GetMockMvcResponseResultSupplier expectContentEncoding(String encoding) {
        return expectContent(content -> content.encoding(encoding));
    }

    /**
     * Assert the character encoding in the ServletResponse.
     */
    public GetMockMvcResponseResultSupplier expectContentEncoding(Charset characterEncoding) {
        return expectContent(content -> content.encoding(characterEncoding));
    }

    /**
     * Assert the response body content as a byte array.
     */
    public GetMockMvcResponseResultSupplier expectContentBytes(byte[] expectedBytes) {
        return expectContent(content -> content.bytes(expectedBytes));
    }

    /**
     * Assert the response body content as a String.
     */
    public GetMockMvcResponseResultSupplier expectContent(String expectedString) {
        return expectContent(content -> content.string(expectedString));
    }

    /**
     * Assert the response body content with a Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectContent(Matcher<? super String> stringMatcher) {
        return expectContent(content -> content.string(stringMatcher));
    }

    /**
     * Parse the expected and actual strings as JSON and assert the two
     * are "similar" - i.e. they contain the same attribute-value pairs
     * regardless of formatting with a lenient checking (extensible, and non-strict array
     * ordering).
     */
    public GetMockMvcResponseResultSupplier expectJsonContent(String expectedJsonContent) {
        return expectContent(content -> content.json(expectedJsonContent));
    }

    /**
     * Parse the response content and the given string as JSON and assert the two are "similar" -
     * i.e. they contain the same attribute-value pairs regardless of formatting.
     * <p>Can compare in two modes, depending on {@code strict} parameter value:
     * <ul>
     * <li>{@code true}: strict checking. Not extensible, and strict array ordering.</li>
     * <li>{@code false}: lenient checking. Extensible, and non-strict array ordering.</li>
     * </ul>
     * <p>Use of this matcher requires the <a
     * href="https://jsonassert.skyscreamer.org/">JSONassert</a> library.
     */
    public GetMockMvcResponseResultSupplier expectJsonContent(String expectedJsonContent, boolean strict) {
        return expectContent(content -> content.json(expectedJsonContent, strict));
    }

    /**
     * Parse the response content and the given string as XML and assert the two
     * are "similar" - i.e. they contain the same elements and attributes
     * regardless of order.
     * <p>Use of this matcher requires the <a
     * href="http://xmlunit.sourceforge.net/">XMLUnit</a> library.
     */
    public GetMockMvcResponseResultSupplier expectXmlContent(String xmlContent) {
        return expectContent(content -> content.xml(xmlContent));
    }

    /**
     * Assert the response body content as a String. It is expected that it is equal to serialized
     * POJO/DTO/collection
     *
     * @param serialized      is an object to be serialized into string and compared with content
     * @param dataTransformer performs serialization of the object
     * @param <T>             is a type of object to be serialized into string and compared with content
     */
    public <T> GetMockMvcResponseResultSupplier expectContent(T serialized, DataTransformer dataTransformer) {
        return expectContent(dataTransformer.serialize(serialized));
    }

    /**
     * Assert the response body content as a String. It is expected that it is equal to serialized
     * POJO/DTO/collection
     *
     * @param serialized is an object to be serialized into string and compared with content
     * @param <T>        is a type of object to be serialized into string and compared with content
     * @see SpringMockMvcDefaultResponseBodyTransformer
     * @see SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER
     */
    public <T> GetMockMvcResponseResultSupplier expectContent(T serialized) {
        return expectContent(serialized, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get());
    }

    /**
     * Asserts cookie in fluent way
     *
     * @param createMatcher the function what describes the assertion
     *                      <p></p>
     *                      Example: {@code cookie -> cookie.value(name, value)}
     * @return self-reference
     */
    public GetMockMvcResponseResultSupplier expectCookie(Function<CookieResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(cookie(), createMatcher);
    }

    /**
     * Assert a cookie exists. The existence check is irrespective of whether max age is 0 (i.e. expired).
     */
    public GetMockMvcResponseResultSupplier expectCookie(String name) {
        return expectCookie(cookie -> cookie.exists(name));
    }

    /**
     * Assert a cookie value.
     */
    public GetMockMvcResponseResultSupplier expectCookieValue(String name, String value) {
        return expectCookie(cookie -> cookie.value(name, value));
    }

    /**
     * Assert a cookie value with the given Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectCookieValue(String name, Matcher<? super String> valueMatcher) {
        return expectCookie(cookie -> cookie.value(name, valueMatcher));
    }

    /**
     * Assert a cookie's maxAge with a Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectCookieMaxAge(String name, Matcher<? super Integer> matcher) {
        return expectCookie(cookie -> cookie.maxAge(name, matcher));
    }

    /**
     * Assert a cookie's maxAge.
     */
    public GetMockMvcResponseResultSupplier expectCookieMaxAge(String name, int maxAge) {
        return expectCookie(cookie -> cookie.maxAge(name, maxAge));
    }

    /**
     * Assert a cookie's path with a Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectCookiePath(String name, Matcher<? super String> matcher) {
        return expectCookie(cookie -> cookie.path(name, matcher));
    }

    /**
     * Assert a cookie's path.
     */
    public GetMockMvcResponseResultSupplier expectCookiePath(String name, String path) {
        return expectCookie(cookie -> cookie.path(name, path));
    }

    /**
     * Assert a cookie's domain with a Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectCookieDomain(String name, Matcher<? super String> matcher) {
        return expectCookie(cookie -> cookie.domain(name, matcher));
    }

    /**
     * Assert a cookie's domain.
     */
    public GetMockMvcResponseResultSupplier expectCookieDomain(String name, String domain) {
        return expectCookie(cookie -> cookie.domain(name, domain));
    }

    /**
     * Assert a cookie's comment with a Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectCookieComment(String name, Matcher<? super String> matcher) {
        return expectCookie(cookie -> cookie.comment(name, matcher));
    }

    /**
     * Assert a cookie's comment.
     */
    public GetMockMvcResponseResultSupplier expectCookieComment(String name, String comment) {
        return expectCookie(cookie -> cookie.comment(name, comment));
    }

    /**
     * Assert a cookie's version with a Hamcrest {@link Matcher}.
     */
    public GetMockMvcResponseResultSupplier expectCookieVersion(String name, Matcher<? super Integer> matcher) {
        return expectCookie(cookie -> cookie.version(name, matcher));
    }

    /**
     * Assert a cookie's version.
     */
    public GetMockMvcResponseResultSupplier expectCookieVersion(String name, int version) {
        return expectCookie(cookie -> cookie.version(name, version));
    }

    /**
     * Assert whether the cookie must be sent over a secure protocol or not.
     */
    public GetMockMvcResponseResultSupplier expectCookieSecure(String name, boolean secure) {
        return expectCookie(cookie -> cookie.secure(name, secure));
    }

    /**
     * Assert whether the cookie must be HTTP only.
     */
    public GetMockMvcResponseResultSupplier expectCookieHttpOnly(String name, boolean httpOnly) {
        return expectCookie(cookie -> cookie.httpOnly(name, httpOnly));
    }

    private GetMockMvcResponseResultSupplier expectResolvedException(Function<NeptuneExceptionResultMatchers, ResultMatcher> createMatcher) {
        return addExpectation(new NeptuneExceptionResultMatchers(), createMatcher);
    }

    /**
     * Asserts that there is no exception raised by a handler and successfully resolved
     * through a {@link HandlerExceptionResolver}.
     */
    public GetMockMvcResponseResultSupplier expectNoResolvedException() {
        return expectResolvedException(NeptuneExceptionResultMatchers::hasNoResolvedException);
    }

    /**
     * Asserts that there is an exception raised by a handler and successfully resolved
     * through a {@link HandlerExceptionResolver}
     */
    public GetMockMvcResponseResultSupplier expectResolvedException() {
        return expectResolvedException(NeptuneExceptionResultMatchers::hasResolvedException);
    }

    /**
     * Asserts exception raised by a handler and successfully resolved
     * through a {@link HandlerExceptionResolver} with a Hamcrest {@link Matcher}
     */
    @SafeVarargs
    public final GetMockMvcResponseResultSupplier expectResolvedException(Matcher<? super Throwable>... matchers) {
        return expectResolvedException(exceptionResultMatchers -> exceptionResultMatchers.resolvedException(all(matchers)));
    }

    /**
     * Asserts exception raised by a handler and successfully resolved
     * through a {@link HandlerExceptionResolver} is an object of
     * the defined class (not of any subclass)
     */
    public <T extends Exception> GetMockMvcResponseResultSupplier expectResolvedException(Class<T> exceptionClass) {
        return expectResolvedException(isObjectOfClass(exceptionClass));
    }

    /**
     * Asserts exception raised by a handler and successfully resolved
     * through a {@link HandlerExceptionResolver} is an object of
     * the defined class (not of any subclass) and has defined message
     */
    public <T extends Exception> GetMockMvcResponseResultSupplier expectResolvedException(Class<T> exceptionClass,
                                                                                          String message) {
        return expectResolvedException(isObjectOfClass(exceptionClass), throwableHasMessage(message));
    }

    /**
     * Asserts exception raised by a handler and successfully resolved
     * through a {@link HandlerExceptionResolver} is an object of
     * the defined class (not of any subclass) and has a message
     * that suits defined Hamcrest {@link Matcher}
     */
    @SafeVarargs
    public final <T extends Exception> GetMockMvcResponseResultSupplier expectResolvedException(Class<T> exceptionClass,
                                                                                                Matcher<? super String>... messageMatchers) {
        return expectResolvedException(isObjectOfClass(exceptionClass), throwableHasMessage(messageMatchers));
    }

    /**
     * For the testing purposes
     */
    List<CheckMockMvcExpectation> getResultMatches() {
        return getResponse.resultMatches;
    }

    private void dataForCapturing() {
        var c = getResponse.getInnerRequestResponseCatcher();
        try {
            var request = c.getRequest();
            requestBody = nonNull(request) ? ofNullable(request.getContentAsByteArray())
                    .map(bytes -> new String(bytes, UTF_8))
                    .orElse(null) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        response = c.getResponse();

        var bytes = nonNull(response) ? response.getContentAsByteArray() : null;

        if (nonNull(bytes) && bytes.length > 0) {
            responseBody = new String(bytes, UTF_8);
        }
    }

    @Override
    protected void onSuccess(MockHttpServletResponse mockHttpServletResponse) {
        dataForCapturing();
    }

    @Override
    protected void onFailure(MockMvcContext mockMvcContext, Throwable throwable) {
        dataForCapturing();
        var cause = throwable.getCause();
        if (cause instanceof AssertionError) {
            throw new AssertionError(throwable.getMessage(), cause);
        }
    }

    @Override
    protected Map<String, String> additionalParameters() {
        var request = getResponse.getInnerRequestResponseCatcher().getRequest();

        if (isNull(request)) {
            return Map.of();
        }

        var result = new LinkedHashMap<String, String>();

        result.put("Request URI", request.getRequestURI());
        result.put("Protocol", request.getProtocol());
        result.put("Method", request.getMethod());

        var names = request.getHeaderNames();
        names.asIterator().forEachRemaining(s -> result.put(s, String.join(";", list(request.getHeaders(s)))));

        var user = request.getRemoteUser();
        if (isNotBlank(user)) {
            result.put("User", user);
        }

        return result;
    }

    /**
     * Creates a step that returns raw string content of body of the received response.
     *
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public GetObjectFromResponseBody<String> thenGetStringContent() {
        return contentAsString().from(this);
    }

    /**
     * Creates a step that returns deserialized content of body of the received response.
     *
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of deserialized body
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T> GetObjectFromResponseBody<T> thenGetBody(DataTransformer transformer, Class<T> tClass) {
        return responseBody(transformer, tClass).from(this);
    }

    /**
     * Creates a step that returns deserialized content of body of the received response.
     *
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of deserialized body
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T> GetObjectFromResponseBody<T> thenGetBody(DataTransformer transformer, TypeReference<T> tRef) {
        return responseBody(transformer, tRef).from(this);
    }

    /**
     * Creates a step that returns deserialized content of body of the received response.
     * Value of {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param tClass is a class of result of performed deserialization
     * @param <T>    is a type of deserialized body
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T> GetObjectFromResponseBody<T> thenGetBody(Class<T> tClass) {
        return thenGetBody(SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass);
    }

    /**
     * Creates a step that returns deserialized content of body of the received response.
     * Value of {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param tRef is a type of result of performed deserialization
     * @param <T>  is a type of deserialized body
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T> GetObjectFromResponseBody<T> thenGetBody(TypeReference<T> tRef) {
        return thenGetBody(SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef);
    }

    /**
     * Creates a step that deserializes content of body of the received response and then return value
     * taken from / calculated by deserialized content.
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T, R> GetObjectFromResponseBody<T> thenGetValue(
            String description,
            DataTransformer transformer,
            Class<R> tClass,
            Function<R, T> howToGet) {
        return objectFromBody(description, transformer, tClass, howToGet).from(this);
    }

    /**
     * Creates a step that deserializes content of body of the received response and then return value
     * taken from / calculated by deserialized content.
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T, R> GetObjectFromResponseBody<T> thenGetValue(
            String description,
            DataTransformer transformer,
            TypeReference<R> tRef,
            Function<R, T> howToGet) {
        return objectFromBody(description, transformer, tRef, howToGet).from(this);
    }

    /**
     * Creates a step that deserializes content of body of the received response and then return value
     * taken from / calculated by deserialized content. Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T, R> GetObjectFromResponseBody<T> thenGetValue(
            String description,
            Class<R> tClass,
            Function<R, T> howToGet) {
        return thenGetValue(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass, howToGet);
    }

    /**
     * Creates a step that deserializes content of body of the received response and then return value
     * taken from / calculated by deserialized content. Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T, R> GetObjectFromResponseBody<T> thenGetValue(
            String description,
            TypeReference<R> tRef,
            Function<R, T> howToGet) {
        return thenGetValue(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef, howToGet);
    }


    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an array from deserialized response content</li>
     *     <li>- then returns item of the array (the first found or one that matches defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get an array
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T, R> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            DataTransformer transformer,
            Class<R> tClass,
            Function<R, T[]> howToGet) {
        return objectFromArray(description, transformer, tClass, howToGet).from(this);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an array from deserialized response content</li>
     *     <li>- then returns item of the array (the first found or one that matches defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get an array
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T, R> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            DataTransformer transformer,
            TypeReference<R> tRef,
            Function<R, T[]> howToGet) {
        return objectFromArray(description, transformer, tRef, howToGet).from(this);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an array from deserialized response content</li>
     *     <li>- then returns item of the array (the first found or one that matches defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get an array
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T, R> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            Class<R> tClass,
            Function<R, T[]> howToGet) {
        return thenGetValueFromArray(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass, howToGet);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an array from deserialized response content</li>
     *     <li>- then returns item of the array (the first found or one that matches defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get an array
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T, R> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            TypeReference<R> tRef,
            Function<R, T[]> howToGet) {
        return thenGetValueFromArray(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef, howToGet);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to array</li>
     *     <li>- then returns item of the array (the first found or one that matches defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            DataTransformer transformer,
            Class<T[]> tClass) {
        return thenGetValueFromArray(description, transformer, tClass, ts -> ts);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to array</li>
     *     <li>- then returns item of the array (the first found or one that matches defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            DataTransformer transformer,
            TypeReference<T[]> tRef) {
        return thenGetValueFromArray(description, transformer, tRef, ts -> ts);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to array</li>
     *     <li>- then returns item of the array (the first found or one that matches defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            Class<T[]> tClass) {
        return thenGetValueFromArray(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to array</li>
     *     <li>- then returns item of the array (the first found or one that matches defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            TypeReference<T[]> tRef) {
        return thenGetValueFromArray(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef);
    }


    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an {@link Iterable} from deserialized response content</li>
     *     <li>- then returns item of the iterable (the first found or one that matches defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, R, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            DataTransformer transformer,
            Class<R> tClass,
            Function<R, S> howToGet) {
        return objectFromIterable(description, transformer, tClass, howToGet).from(this);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an {@link Iterable} from deserialized response content</li>
     *     <li>- then returns item of the iterable (the first found or one that matches defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, R, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            DataTransformer transformer,
            TypeReference<R> tRef,
            Function<R, S> howToGet) {
        return objectFromIterable(description, transformer, tRef, howToGet).from(this);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an {@link Iterable} from deserialized response content</li>
     *     <li>- then returns item of the iterable (the first found or one that matches defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, R, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            Class<R> tClass,
            Function<R, S> howToGet) {
        return thenGetValueFromIterable(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass, howToGet);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an {@link Iterable} from deserialized response content</li>
     *     <li>- then returns item of the iterable (the first found or one that matches defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, R, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            TypeReference<R> tRef,
            Function<R, S> howToGet) {
        return thenGetValueFromIterable(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef, howToGet);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to {@link Iterable}</li>
     *     <li>- then returns item of the iterable (the first found or one that matches defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            DataTransformer transformer,
            Class<S> tClass) {
        return thenGetValueFromIterable(description, transformer, tClass, ts -> ts);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to {@link Iterable}</li>
     *     <li>- then returns item of the iterable (the first found or one that matches defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            DataTransformer transformer,
            TypeReference<S> tRef) {
        return thenGetValueFromIterable(description, transformer, tRef, ts -> ts);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to {@link Iterable}</li>
     *     <li>- then returns item of the iterable (the first found or one that matches defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            Class<S> tClass) {
        return thenGetValueFromIterable(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to {@link Iterable}</li>
     *     <li>- then returns item of the iterable (the first found or one that matches defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            TypeReference<S> tRef) {
        return thenGetValueFromIterable(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef);
    }


    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an {@link Iterable} from deserialized response content</li>
     *     <li>- then returns items of the iterable (all items or ones that match defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, R, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(
            String description,
            DataTransformer transformer,
            Class<R> tClass,
            Function<R, S> howToGet) {
        return GetListFromResponse.list(description, transformer, tClass, howToGet).from(this);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an {@link Iterable} from deserialized response content</li>
     *     <li>- then returns items of the iterable (all items or ones that match defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, R, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(
            String description,
            DataTransformer transformer,
            TypeReference<R> tRef,
            Function<R, S> howToGet) {
        return GetListFromResponse.list(description, transformer, tRef, howToGet).from(this);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an {@link Iterable} from deserialized response content</li>
     *     <li>- then returns items of the iterable (all items or ones that match defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, R, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(
            String description,
            Class<R> tClass,
            Function<R, S> howToGet) {
        return thenGetList(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass, howToGet);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an {@link Iterable} from deserialized response content</li>
     *     <li>- then returns items of the iterable (all items or ones that match defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get desired value
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, R, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(
            String description,
            TypeReference<R> tRef,
            Function<R, S> howToGet) {
        return thenGetList(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef, howToGet);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to {@link Iterable}</li>
     *     <li>- then returns items of the iterable (all items or ones that match defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(
            String description,
            DataTransformer transformer,
            Class<S> tClass) {
        return thenGetList(description, transformer, tClass, ts -> ts);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to {@link Iterable}</li>
     *     <li>- then returns items of the iterable (all items or ones that match defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(
            String description,
            DataTransformer transformer,
            TypeReference<S> tRef) {
        return thenGetList(description, transformer, tRef, ts -> ts);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to {@link Iterable}</li>
     *     <li>- then returns items of the iterable (all items or ones that match defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(
            String description,
            Class<S> tClass) {
        return thenGetList(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to {@link Iterable}</li>
     *     <li>- then returns items of the iterable (all items or ones that match defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(
            String description,
            TypeReference<S> tRef) {
        return thenGetList(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef);
    }


    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an array from deserialized response content</li>
     *     <li>- then returns items of the array (all items or ones that match defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get an array
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of item of resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T, R> GetArrayFromResponse<T> thenGetArray(
            String description,
            DataTransformer transformer,
            Class<R> tClass,
            Function<R, T[]> howToGet) {
        return array(description, transformer, tClass, howToGet).from(this);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an array from deserialized response content</li>
     *     <li>- then returns items of the array (all items or ones that match defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get an array
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of item of resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T, R> GetArrayFromResponse<T> thenGetArray(
            String description,
            DataTransformer transformer,
            TypeReference<R> tRef,
            Function<R, T[]> howToGet) {
        return array(description, transformer, tRef, howToGet).from(this);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an array from deserialized response content</li>
     *     <li>- then returns items of the array (all items or ones that match defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param howToGet    describes how to get an array
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of item of resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T, R> GetArrayFromResponse<T> thenGetArray(
            String description,
            Class<R> tClass,
            Function<R, T[]> howToGet) {
        return thenGetArray(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass, howToGet);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response</li>
     *     <li>- then tries to get an array from deserialized response content</li>
     *     <li>- then returns items of the array (all items or ones that match defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param howToGet    describes how to get an array
     * @param <R>         is a type of deserialized body
     * @param <T>         is a type of item of resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T, R> GetArrayFromResponse<T> thenGetArray(
            String description,
            TypeReference<R> tRef,
            Function<R, T[]> howToGet) {
        return thenGetArray(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef, howToGet);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to array</li>
     *     <li>- then returns items of the array (all items or ones that match defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of item of resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T> GetArrayFromResponse<T> thenGetArray(
            String description,
            DataTransformer transformer,
            Class<T[]> tClass) {
        return thenGetArray(description, transformer, tClass, ts -> ts);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to array</li>
     *     <li>- then returns items of the array (all items or ones that match defined criteria)</li>
     * </ul>
     *
     * @param description is description of value to get
     * @param transformer performs deserialization
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of item of resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T> GetArrayFromResponse<T> thenGetArray(
            String description,
            DataTransformer transformer,
            TypeReference<T[]> tRef) {
        return thenGetArray(description, transformer, tRef, ts -> ts);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to array</li>
     *     <li>- then returns items of the array (all items or ones that match defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tClass      is a class of result of performed deserialization
     * @param <T>         is a type of item of resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T> GetArrayFromResponse<T> thenGetArray(
            String description,
            Class<T[]> tClass) {
        return thenGetArray(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass);
    }

    /**
     * Creates a step that:
     * <ul>
     *     <li>- deserializes content of body of the received response to array</li>
     *     <li>- then returns items of the array (all items or ones that match defined criteria)</li>
     * </ul>
     * <p>
     * Value of
     * {@link ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER}
     * is used to deserialize string body content.
     *
     * @param description is description of value to get
     * @param tRef        is a type of result of performed deserialization
     * @param <T>         is a type of item of resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T> GetArrayFromResponse<T> thenGetArray(
            String description,
            TypeReference<T[]> tRef) {
        return thenGetArray(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef);
    }


    private static class GetAndCheckResponse implements Function<MockMvcContext, MockHttpServletResponse> {

        private final Function<MockMvcContext, MockMvc> getMockMvc;
        private final RequestBuilder builder;
        private final List<CheckMockMvcExpectation> resultMatches = new LinkedList<>();
        private final List<AssertionError> errors = new LinkedList<>();
        private final InnerRequestResponseCatcher innerRequestResponseCatcher = new InnerRequestResponseCatcher();

        private GetAndCheckResponse(Function<MockMvcContext, MockMvc> getMockMvc, RequestBuilder builder) {
            checkNotNull(builder);
            this.getMockMvc = getMockMvc;
            this.builder = builder;
        }

        private void addExpectation(ResultMatcher matcher) {
            resultMatches.add(checkExpectation(matcher));
        }

        private InnerRequestResponseCatcher getInnerRequestResponseCatcher() {
            return innerRequestResponseCatcher;
        }

        @Override
        public MockHttpServletResponse apply(MockMvcContext mockMvcContext) {
            errors.clear();
            var mockMvc = getMockMvc.apply(mockMvcContext);
            try {
                var result = mockMvc
                        .perform(builder)
                        .andDo(innerRequestResponseCatcher);

                resultMatches.forEach(m -> {
                    try {
                        m.get().performAction(result);
                    } catch (AssertionError e) {
                        errors.add(e);
                    }
                });

                if (errors.isEmpty()) {
                    return result.andReturn().getResponse();
                }

                var messageBuilder = new StringBuilder();
                messageBuilder.append("Mismatches: ");
                errors.forEach(e -> messageBuilder.append("\r\n").append("\r\n").append(e.getMessage()));
                throw new AssertionError(messageBuilder.toString());
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }

    private static final class InnerRequestResponseCatcher implements ResultHandler {

        private MockHttpServletRequest request;
        private MockHttpServletResponse response;

        @Override
        public void handle(MvcResult result) {
            request = result.getRequest();
            response = result.getResponse();
        }

        MockHttpServletRequest getRequest() {
            return request;
        }

        MockHttpServletResponse getResponse() {
            return response;
        }
    }
}
