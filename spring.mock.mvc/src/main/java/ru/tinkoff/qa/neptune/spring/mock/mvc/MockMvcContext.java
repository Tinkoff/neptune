package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

public class MockMvcContext extends Context<MockMvcContext> {

    private static final MockMvcContext context = getInstance(MockMvcContext.class);
    private final MockMvcProvider defaultMockMvcProvider;

    public MockMvcContext() {
        defaultMockMvcProvider = new MockMvcProvider();
    }

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

    MockMvc getDefaultMockMvc() {
        var defaultMockMvc = defaultMockMvcProvider.provide();
        checkState(nonNull(defaultMockMvc), "The instance of "
                + MockMvcProvider.class
                + " returned null");
        return defaultMockMvc;
    }
}
