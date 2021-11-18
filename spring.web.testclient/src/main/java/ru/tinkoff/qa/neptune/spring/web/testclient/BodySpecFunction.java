package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectAnyBody;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectEmptyBody;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectedBodyOfClass;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectedBodyOfType;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.any.AnyMatcher.anyOne;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;

final class BodySpecFunction<T, BODY_SPEC> implements Function<WebTestClient.ResponseSpec, T> {

    private final Function<WebTestClient.ResponseSpec, BODY_SPEC> getBodySpec;
    private final ReturnBodyFromSpec<T, BODY_SPEC> getBody;
    private boolean isExecuted;
    private String description;

    BodySpecFunction(Function<WebTestClient.ResponseSpec, BODY_SPEC> getBodySpec,
                     ReturnBodyFromSpec<T, BODY_SPEC> getBody) {
        checkNotNull(getBodySpec);
        checkNotNull(getBody);
        this.getBodySpec = getBodySpec;
        this.getBody = getBody;
    }

    static BodySpecFunction<Byte[], WebTestClient.BodyContentSpec> defaultContent() {
        return new BodySpecFunction<>(WebTestClient.ResponseSpec::expectBody,
                new ReturnRawBody(false));
    }

    static BodySpecFunction<Byte[], WebTestClient.BodyContentSpec> nonEmptyRawContent() {
        return new BodySpecFunction<>(WebTestClient.ResponseSpec::expectBody,
                new ReturnRawBody(true))
                .setDescription(new ExpectAnyBody().toString());
    }

    static BodySpecFunction<Void, WebTestClient.BodyContentSpec> emptyBody() {
        return new BodySpecFunction<>(WebTestClient.ResponseSpec::expectBody,
                new ReturnEmptyBody())
                .setDescription(new ExpectEmptyBody().toString());
    }

    static <T> BodySpecFunction<T, WebTestClient.BodySpec<T, ?>> bodyMappedAs(Class<T> tClass) {
        return new BodySpecFunction<>(spec -> (WebTestClient.BodySpec<T, ?>) spec.expectBody(tClass),
                new MappedBody<>())
                .setDescription(new ExpectedBodyOfClass(tClass).toString());
    }

    static <T> BodySpecFunction<T, WebTestClient.BodySpec<T, ?>> bodyMappedAs(ParameterizedTypeReference<T> type) {
        return new BodySpecFunction<>(spec -> (WebTestClient.BodySpec<T, ?>) spec.expectBody(type),
                new MappedBody<>())
                .setDescription(new ExpectedBodyOfType(type).toString());
    }

    static <T> BodySpecFunction<List<T>, WebTestClient.BodySpec<List<T>, ?>> listBodyOf(Class<T> tClass) {
        return new BodySpecFunction<>(spec -> spec.expectBodyList(tClass),
                new MappedBody<>())
                .setDescription(new ExpectedBodyOfClass(tClass).toString());
    }

    static <T> BodySpecFunction<List<T>, WebTestClient.BodySpec<List<T>, ?>> listBodyOf(ParameterizedTypeReference<T> type) {
        return new BodySpecFunction<>(spec -> spec.expectBodyList(type),
                new MappedBody<>())
                .setDescription(new ExpectedBodyOfType(type).toString());
    }

    private static Byte[] bytePrimitiveToByteWrapperArray(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        var result = new Byte[]{};
        for (var b : bytes) {
            result = add(result, b);
        }

        return result;
    }

    @Override
    public T apply(WebTestClient.ResponseSpec spec) {
        if (isExecuted) {
            return getBody();
        }

        var result = getBodySpec.andThen(getBody).apply(spec);
        isExecuted = true;
        return result;
    }

    ExchangeResult exchangeResult() {
        return getBody.exchangeResult();
    }

    T getBody() {
        return getBody.getBody();
    }

    public String toString() {
        if (isBlank(description)) {
            return super.toString();
        }

        return description;
    }

    public BodySpecFunction<T, BODY_SPEC> setDescription(String description) {
        this.description = description;
        return this;
    }

    private static final class ExchangeResultAndBodyContainer<T> {

        private T body;

        private ExchangeResult exchangeResult;

        T getBody() {
            return body;
        }

        ExchangeResultAndBodyContainer<T> setBody(T body) {
            this.body = body;
            return this;
        }

        ExchangeResult getExchangeResult() {
            return exchangeResult;
        }

        ExchangeResultAndBodyContainer<T> setExchangeResult(ExchangeResult exchangeResult) {
            this.exchangeResult = exchangeResult;
            return this;
        }
    }

    private static abstract class ReturnBodyFromSpec<T, BODY_SPEC> implements Function<BODY_SPEC, T> {

        final ExchangeResultAndBodyContainer<T> container = new ExchangeResultAndBodyContainer<>();

        ExchangeResult exchangeResult() {
            return container.getExchangeResult();
        }

        T getBody() {
            return container.getBody();
        }
    }

    private static final class ReturnRawBody extends ReturnBodyFromSpec<Byte[], WebTestClient.BodyContentSpec> {

        private final boolean requiresNotEmpty;

        private ReturnRawBody(boolean requiresNotEmpty) {
            this.requiresNotEmpty = requiresNotEmpty;
        }

        @Override
        public Byte[] apply(WebTestClient.BodyContentSpec bodyContentSpec) {
            bodyContentSpec.consumeWith(e -> {
                container.setExchangeResult(e);

                var body = bytePrimitiveToByteWrapperArray(e.getResponseBody());
                if (requiresNotEmpty) {
                    assertThat("Response body", body, notOf(nullValue(), emptyArray()));
                }

                container.setBody(body);
            });

            return getBody();
        }
    }

    private static final class ReturnEmptyBody extends ReturnBodyFromSpec<Void, WebTestClient.BodyContentSpec> {

        @Override
        public Void apply(WebTestClient.BodyContentSpec bodyContentSpec) {
            bodyContentSpec.consumeWith(e -> {
                container.setExchangeResult(e);

                var body = bytePrimitiveToByteWrapperArray(e.getResponseBody());
                assertThat("Response body", body, anyOne(emptyArray(), nullValue()));

                container.setBody(null);
            });

            return null;
        }
    }

    private static final class MappedBody<T> extends ReturnBodyFromSpec<T, WebTestClient.BodySpec<T, ?>> {

        @Override
        public T apply(WebTestClient.BodySpec<T, ?> tBodySpec) {
            tBodySpec.consumeWith(tEntityExchangeResult -> {
                container.setExchangeResult(tEntityExchangeResult);
                var body = tEntityExchangeResult.getResponseBody();
                assertThat("Response body", body, not(nullValue()));
                container.setBody(body);
            });

            return getBody();
        }
    }
}
