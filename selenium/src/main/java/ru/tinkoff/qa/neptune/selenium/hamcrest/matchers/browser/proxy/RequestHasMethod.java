package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HttpMethod;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;

@Description("Request method {methodMatcher}")
public final class RequestHasMethod extends NeptuneFeatureMatcher<HarEntry> {

    @DescriptionFragment(value = "methodMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
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
    public static Matcher<HarEntry> requestHasMethod(Matcher<? super HttpMethod> methodMatcher) {
        return new RequestHasMethod(methodMatcher);
    }

    /**
     * Creates matcher that checks HTTP method of a request.
     *
     * @param method is the expected method of the request
     * @return a new instance of {@link RequestHasMethod}
     */
    public static Matcher<HarEntry> requestHasMethod(HttpMethod method) {
        return new RequestHasMethod(is(method));
    }


    @Override
    protected boolean featureMatches(HarEntry toMatch) {
        var requestMethod = toMatch.getRequest().getMethod();
        var result = methodMatcher.matches(requestMethod);

        if (!result) {
            appendMismatchDescription(methodMatcher, requestMethod);
        }

        return result;
    }
}
