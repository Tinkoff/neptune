package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

public class MockMvcContext extends Context<MockMvcContext> {

    private static final MockMvcContext context = getInstance(MockMvcContext.class);
    private MockMvc defaultMockMvc;

    static MockMvcContext getContext() {
        return context;
    }

    /**
     * Gets response.
     *
     * @param request is specification of request
     * @return response
     */
    public static MockHttpServletResponse mockMvcGet(GetMockMvcResponseResultSupplier request) {
        var context = getContext();
        return context.get(request);
    }

    /**
     * Gets some value from body of response.
     *
     * @param specification is specification of value to get
     * @return value taken from / calculated by response body
     */
    public static <T> T mockMvcGet(GetObjectFromResponseBody<T> specification) {
        var context = getContext();
        return context.get(specification);
    }

    /**
     * Gets some array from body of response.
     *
     * @param specification is specification of array to get
     * @return array taken from / calculated by response body
     */
    public static <T> T[] mockMvcGet(GetArrayFromResponse<T> specification) {
        var context = getContext();
        return context.get(specification);
    }

    /**
     * Gets some iterable from body of response.
     *
     * @param specification is specification of iterable to get
     * @return iterable taken from / calculated by response body
     */
    public static <T, S extends Iterable<T>> S mockMvcGet(GetIterableFromResponse<T, S> specification) {
        var context = getContext();
        return context.get(specification);
    }

    /**
     * Gets some item of array from body of response.
     *
     * @param specification is specification of value to get
     * @return value taken from / calculated by response body
     */
    public static <T> T mockMvcGet(GetObjectFromResponseBodyArray<T> specification) {
        var context = getContext();
        return context.get(specification);
    }

    /**
     * Gets some item of iterable from body of response.
     *
     * @param specification is specification of value to get
     * @return value taken from / calculated by response body
     */
    public static <T> T mockMvcGet(GetObjectFromResponseBodyIterable<T> specification) {
        var context = getContext();
        return context.get(specification);
    }

    void setDefault(MockMvc defaultMockMvc) {
        checkNotNull(defaultMockMvc);
        this.defaultMockMvc = defaultMockMvc;
    }

    MockMvc getDefaultMockMvc() {
        checkState(nonNull(defaultMockMvc), "There is no field of type MockMvc that has a non-null value");
        return defaultMockMvc;
    }
}
