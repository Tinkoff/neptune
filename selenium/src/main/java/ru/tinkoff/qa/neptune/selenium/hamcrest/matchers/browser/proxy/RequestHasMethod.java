package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import io.netty.handler.codec.http.HttpMethod;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;

@Description("Request method {methodMatcher}")
public final class RequestHasMethod extends NeptuneFeatureMatcher<HttpTraffic> {

    @DescriptionFragment(value = "methodMatcher")
    private final Matcher<? super HttpMethod> methodMatcher;

    private RequestHasMethod(Matcher<? super HttpMethod> methodMatcher) {
        super(true);
        checkNotNull(methodMatcher, "HTTP method matcher is not defined");
        this.methodMatcher = methodMatcher;
    }

    /**
     * Creates matcher that checks HTTP method of a request.
     *
     * @param methodMatcher criteria that describes expected method
     * @return a new instance of {@link RequestHasMethod}
     */
    public static Matcher<HttpTraffic> requestHasMethod(Matcher<? super HttpMethod> methodMatcher) {
        return new RequestHasMethod(methodMatcher);
    }

    /**
     * Creates matcher that checks HTTP method of a request.
     *
     * @param method is the expected method of the request
     * @return a new instance of {@link RequestHasMethod}
     */
    public static Matcher<HttpTraffic> requestHasMethod(HttpMethod method) {
        return new RequestHasMethod(is(method));
    }


    @Override
    protected boolean featureMatches(HttpTraffic toMatch) {
        var requestMethod = new HttpMethod(toMatch.getRequest().getRequest().getMethod());
        var result = methodMatcher.matches(requestMethod);

        if (!result) {
            appendMismatchDescription(methodMatcher, requestMethod);
        }

        return result;
    }
}
