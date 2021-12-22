package ru.tinkoff.qa.neptune.spring.boot.starter.mock.mvc;

import org.springframework.test.web.servlet.MockMvc;

import static ru.tinkoff.qa.neptune.spring.boot.starter.mock.mvc.MockMvcWrappingConfiguration.getMockMvcStatic;

public class MockMvcProviderDefaultImpl implements MockMvcProvider {

    @Override
    public MockMvc provide() {
        return getMockMvcStatic();
    }
}
