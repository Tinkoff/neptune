package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Function;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public final class Expectation<T> {

    private final Function<WebTestClient.ResponseSpec, T> assertion;
    private String description;
    private StringBuilder builder;

    private Throwable t;

    Expectation(Function<WebTestClient.ResponseSpec, T> assertion) {
        this.assertion = assertion;
        if (isLoggable(assertion)) {
            description = assertion.toString();
        }
    }

    void verify(WebTestClient.ResponseSpec spec) {
        this.t = null;
        try {
            assertion.apply(spec);
        } catch (Throwable t) {
            this.t = t;
        }
    }

    @Override
    public String toString() {
        if (nonNull(builder)) {
            return builder.toString();
        }

        if (isNotBlank(description)) {
            return description;
        }

        return super.toString();
    }

    public StringBuilder descriptionBuilder() {
        builder = new StringBuilder();
        if (isNotBlank(description)) {
            builder.append(description);
        }
        return builder;
    }

    public Throwable getThrowable() {
        return t;
    }
}
