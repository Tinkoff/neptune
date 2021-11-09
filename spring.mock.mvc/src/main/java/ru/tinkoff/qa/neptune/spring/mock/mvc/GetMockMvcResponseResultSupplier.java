package ru.tinkoff.qa.neptune.spring.mock.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.*;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.spring.mock.mvc.captors.RequestBodyStringCaptor;
import ru.tinkoff.qa.neptune.spring.mock.mvc.captors.ResponseBodyStringCaptor;
import ru.tinkoff.qa.neptune.spring.mock.mvc.captors.ResponseStringCaptor;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static java.util.Collections.list;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.CheckMockMvcExpectation.checkExpectation;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetArrayFromResponse.array;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetIterableFromResponse.iterable;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetObjectFromResponseBody.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetObjectFromResponseBodyArray.objectFromArray;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetObjectFromResponseBodyIterable.objectFromIterable;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer.SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER;

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

    public GetMockMvcResponseResultSupplier expect(ResultMatcher matcher) {
        this.getResponse.addExpectation(matcher);
        return this;
    }

    private void dataForCapturing() {
        var c = getResponse.getInnerRequestResponseCatcher();
        try {
            var request = c.getRequest();
            requestBody = nonNull(request) ? request.getContentAsString() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        response = c.getResponse();
        try {
            responseBody = nonNull(response) ? response.getContentAsString() : null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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

        result.put("Request URL", request.getRequestURL().toString());
        result.put("Port", valueOf(request.getRemotePort()));
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
     * @return an instance of {@link GetIterableFromResponse}
     */
    public <T, R, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(
            String description,
            DataTransformer transformer,
            Class<R> tClass,
            Function<R, S> howToGet) {
        return iterable(description, transformer, tClass, howToGet).from(this);
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
     * @return an instance of {@link GetIterableFromResponse}
     */
    public <T, R, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(
            String description,
            DataTransformer transformer,
            TypeReference<R> tRef,
            Function<R, S> howToGet) {
        return iterable(description, transformer, tRef, howToGet).from(this);
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
     * @return an instance of {@link GetIterableFromResponse}
     */
    public <T, R, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(
            String description,
            Class<R> tClass,
            Function<R, S> howToGet) {
        return thenGetIterable(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass, howToGet);
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
     * @return an instance of {@link GetIterableFromResponse}
     */
    public <T, R, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(
            String description,
            TypeReference<R> tRef,
            Function<R, S> howToGet) {
        return thenGetIterable(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef, howToGet);
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
     * @return an instance of {@link GetIterableFromResponse}
     */
    public <T, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(
            String description,
            DataTransformer transformer,
            Class<S> tClass) {
        return thenGetIterable(description, transformer, tClass, ts -> ts);
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
     * @return an instance of {@link GetIterableFromResponse}
     */
    public <T, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(
            String description,
            DataTransformer transformer,
            TypeReference<S> tRef) {
        return thenGetIterable(description, transformer, tRef, ts -> ts);
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
     * @return an instance of {@link GetIterableFromResponse}
     */
    public <T, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(
            String description,
            Class<S> tClass) {
        return thenGetIterable(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tClass);
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
     * @return an instance of {@link GetIterableFromResponse}
     */
    public <T, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(
            String description,
            TypeReference<S> tRef) {
        return thenGetIterable(description, SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get(), tRef);
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

                if (errors.size() == 0) {
                    return result.andReturn().getResponse();
                }

                var messageBuilder = new StringBuilder();
                messageBuilder.append("Mismatches: ");
                errors.forEach(e -> messageBuilder.append("\r\n").append("\r\n").append(e.getMessage()));
                throw new AssertionError(messageBuilder.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
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
