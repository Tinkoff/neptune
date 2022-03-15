package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectedBodyOfClass;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectedBodyOfType;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@SuppressWarnings("unchecked")
abstract class BodySpecFunction<T, BODY_SPEC, S extends BodySpecFunction<T, BODY_SPEC, S>> implements Function<WebTestClient.ResponseSpec, T> {

    private final Function<WebTestClient.ResponseSpec, BODY_SPEC> getBodySpec;
    private final ReturnBodyFromSpec<T, BODY_SPEC> getBody;
    private boolean isExecuted;
    private String description;
    private AssertionError thrown;

    BodySpecFunction(Function<WebTestClient.ResponseSpec, BODY_SPEC> getBodySpec,
                     ReturnBodyFromSpec<T, BODY_SPEC> getBody) {
        checkNotNull(getBodySpec);
        checkNotNull(getBody);
        this.getBodySpec = getBodySpec;
        this.getBody = getBody;
    }

    static <T> MappedBodySpecFunction<T> bodyMappedAs(Class<T> tClass) {
        return new MappedBodySpecFunction<>(spec -> (WebTestClient.BodySpec<T, ?>) spec.expectBody(tClass))
                .setDescription(new ExpectedBodyOfClass(tClass).toString());
    }

    static <T> MappedBodySpecFunction<T> bodyMappedAs(ParameterizedTypeReference<T> type) {
        return new MappedBodySpecFunction<>(spec -> (WebTestClient.BodySpec<T, ?>) spec.expectBody(type))
                .setDescription(new ExpectedBodyOfType(type).toString());
    }

    static RawBodySpecFunction rawBody() {
        return new RawBodySpecFunction(new ReturnRawBody());
    }

    ExchangeResult exchangeResult() {
        return getBody.exchangeResult();
    }

    public String toString() {
        if (isBlank(description)) {
            return super.toString();
        }

        return description;
    }

    @Override
    public T apply(WebTestClient.ResponseSpec spec) {
        if (isExecuted) {
            return getBody();
        }

        try {
            return getBodySpec.andThen(getBody).apply(spec);
        } catch (AssertionError e) {
            thrown = e;
            return null;
        } catch (Throwable t) {
            thrown = new AssertionError(t.getMessage(), t);
            return null;
        } finally {
            isExecuted = true;
        }
    }

    T getBody() {
        return ofNullable(thrown).map((Function<AssertionError, T>) assertionError -> {
            throw assertionError;
        }).orElseGet(getBody::getBody);
    }

    public S setDescription(String description) {
        this.description = description;
        return (S) this;
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

    static final class MappedBodySpecFunction<T> extends BodySpecFunction<T, WebTestClient.BodySpec<T, ?>, MappedBodySpecFunction<T>> {

        private MappedBodySpecFunction(Function<WebTestClient.ResponseSpec, WebTestClient.BodySpec<T, ?>> getBodySpec) {
            super(getBodySpec, new MappedBody<>());
        }
    }

    static final class RawBodySpecFunction extends BodySpecFunction<byte[], WebTestClient.BodyContentSpec, RawBodySpecFunction> {

        private final ReturnRawBody returnRawBody;

        private RawBodySpecFunction(ReturnRawBody getBody) {
            super(WebTestClient.ResponseSpec::expectBody, getBody);
            this.returnRawBody = getBody;
        }

        WebTestClient.BodyContentSpec getBodyContentSpec() {
            return returnRawBody.getBodyContentSpec();
        }
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

    private static final class ReturnRawBody extends ReturnBodyFromSpec<byte[], WebTestClient.BodyContentSpec> {

        private WebTestClient.BodyContentSpec bodyContentSpec;

        @Override
        public byte[] apply(WebTestClient.BodyContentSpec bodyContentSpec) {
            this.bodyContentSpec = bodyContentSpec;
            bodyContentSpec.consumeWith(e -> {
                container.setExchangeResult(e);
                container.setBody(e.getResponseBody());
            });

            return getBody();
        }

        WebTestClient.BodyContentSpec getBodyContentSpec() {
            return bodyContentSpec;
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
