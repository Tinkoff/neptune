package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

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
    public static <T> T mockMvcGet(SequentialGetStepSupplier<MockMvcContext, T, ?, ?, ?> specification) {
        var context = getContext();
        return context.get(specification);
    }

    void setDefault(MockMvc defaultMockMvc) {
        this.defaultMockMvc = defaultMockMvc;
    }

    MockMvc getDefaultMockMvc() {
        checkState(nonNull(defaultMockMvc), "There is no field of type MockMvc that has a non-null value");
        return defaultMockMvc;
    }
}
