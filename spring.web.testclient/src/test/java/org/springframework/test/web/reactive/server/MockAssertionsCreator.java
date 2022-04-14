package org.springframework.test.web.reactive.server;

public class MockAssertionsCreator {

    public static StatusAssertions createStatusAssertion(ExchangeResult result, WebTestClient.ResponseSpec spec) {
        return new StatusAssertions(result, spec);
    }

    public static HeaderAssertions createHeaderAssertion(ExchangeResult result, WebTestClient.ResponseSpec spec) {
        return new HeaderAssertions(result, spec);
    }

    public static JsonPathAssertions createJsonPathAssertions(WebTestClient.BodyContentSpec spec, String content, String expression, Object... args) {
        return new JsonPathAssertions(spec, content, expression, args);
    }

    public static CookieAssertions createCookieAssertion(ExchangeResult exchangeResult, WebTestClient.ResponseSpec responseSpec) {
        return new CookieAssertions(exchangeResult, responseSpec);
    }
}
