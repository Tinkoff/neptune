package ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers;

import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class NeptuneExceptionResultMatchers {

    @Description("Resolved exception matches '{exceptionMatcher}'")
    public ResultMatcher resolvedException(
            @DescriptionFragment("exceptionMatcher") Matcher<? super Throwable> exceptionMatcher) {
        return result -> {
            var exception = result.getResolvedException();
            assertThat(exception, exceptionMatcher);
        };
    }

    @Description("Resolved exception")
    public ResultMatcher hasResolvedException() {
        return resolvedException(not(nullValue()));
    }

    @Description("No resolved exception")
    public ResultMatcher hasNoResolvedException() {
        return resolvedException(nullValue());
    }
}
