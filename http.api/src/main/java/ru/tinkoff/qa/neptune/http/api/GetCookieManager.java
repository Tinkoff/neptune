package ru.tinkoff.qa.neptune.http.api;

import java.net.CookieManager;
import java.util.function.Function;

import static java.lang.String.format;

class GetCookieManager implements Function<HttpStepContext, CookieManager> {

    static GetCookieManager getCookieManager() {
        return new GetCookieManager();
    }

    @Override
    public CookieManager apply(HttpStepContext httpStepContext) {
        return httpStepContext
                .getCurrentClient()
                .cookieHandler()
                .map(cookieHandler -> {
                    var clazz = cookieHandler.getClass();
                    if (!CookieManager.class.isAssignableFrom(cookieHandler.getClass())) {
                        throw new IllegalStateException(format("Unexpected class of the cookie handler %s. We support only %s " +
                                        "as cookie handler for a while",
                                clazz.getName(),
                                CookieManager.class.getName()));
                    }
                    return (CookieManager) cookieHandler;
                })
                .orElseThrow(() -> new IllegalStateException("Can't get access to a cookie store of the current http client"));
    }
}
