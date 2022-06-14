package ru.tinkoff.qa.neptune.http.api;

import java.net.CookieStore;
import java.util.function.Function;

public final class GetCurrentCookieStore implements Function<HttpStepContext, CookieStore> {

    private GetCurrentCookieStore() {
        super();
    }

    public static GetCurrentCookieStore getCurrentCookieStore() {
        return new GetCurrentCookieStore();
    }

    @Override
    public CookieStore apply(HttpStepContext httpStepContext) {
        return httpStepContext.getCookieStore();
    }
}
