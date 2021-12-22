package ru.tinkoff.qa.neptune.spring.boot.starter.mock.mvc;

import org.springframework.test.web.servlet.MockMvc;

/**
 * Provides an instance of {@link MockMvc}. Instance of {MockMvcProvider}
 * is considered to be loaded by SPI {@link java.util.ServiceLoader}
 */
public interface MockMvcProvider {

    /**
     * @return instance of {@link MockMvc}
     */
    MockMvc provide();
}
