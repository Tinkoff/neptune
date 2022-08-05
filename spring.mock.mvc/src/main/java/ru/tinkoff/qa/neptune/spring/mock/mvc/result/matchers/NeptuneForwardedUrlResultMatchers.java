package ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers;

import org.springframework.lang.Nullable;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Forwarded URL")
public class NeptuneForwardedUrlResultMatchers {

    /**
     * Asserts the request was forwarded to the given URL.
     *
     * @param expectedUrl the exact URL expected
     */
    @Description("'{0}'")
    public ResultMatcher forwardedUrl(@Nullable String expectedUrl) {
        return MockMvcResultMatchers.forwardedUrl(expectedUrl);
    }

    /**
     * Asserts the request was forwarded to the given URL template.
     *
     * @param urlTemplate a URL template; the expanded URL will be encoded
     * @param uriVars     zero or more URI variables to populate the template
     */
    @Description("meets pattern '{0}'. Arguments {1}")
    public ResultMatcher forwardedUrlTemplate(String urlTemplate, Object... uriVars) {
        return MockMvcResultMatchers.forwardedUrlTemplate(urlTemplate, uriVars);
    }

    /**
     * Asserts the request was forwarded to the given URL.
     *
     * @param urlPattern an Ant-style path pattern to match against
     */
    @Description("meets pattern '{0}'")
    public ResultMatcher forwardedUrlPattern(String urlPattern) {
        return MockMvcResultMatchers.forwardedUrlPattern(urlPattern);
    }
}
