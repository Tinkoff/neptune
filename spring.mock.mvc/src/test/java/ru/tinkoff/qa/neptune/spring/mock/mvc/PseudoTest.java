package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

public class PseudoTest {

    private MockMvc mockMvc;

    public PseudoTest setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        return this;
    }

    public MockMvc test1() {
        return MockMvcContext.getContext().getDefaultMockMvc();
    }

    public MockHttpServletResponse test2(RequestBuilder builder) {
        return mockMvcGet(response(builder));
    }
}
