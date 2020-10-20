package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

public final class RequestHasBody<T> extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super T> bodyMatcher;
    private final ObjectMapper objectMapper;
    private final Class<?> objectClass;

    private RequestHasBody(Matcher<? super T> bodyMatcher, ObjectMapper objectMapper, Class<?> objectClass) {
        this.bodyMatcher = bodyMatcher;
        this.objectMapper = objectMapper;
        this.objectClass = objectClass;
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param bodyMatcher is the criteria that describes request body
     * @param <T>         is the type of deserialized body
     * @return a new instance of {@link RequestHasBody}
     */
    public static <T> RequestHasBody<T> requestHasBody(Matcher<? super T> bodyMatcher) {
        return new RequestHasBody<>(bodyMatcher, new ObjectMapper(), bodyMatcher.getClass());
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param bodyMatcher  is the criteria that describes request body
     * @param objectMapper is the Jackson mapper which will deserialize actual request body
     * @param <T>          is the type of deserialized body
     * @return a new instance of {@link RequestHasBody}
     */
    public static <T> RequestHasBody<T> requestHasBody(Matcher<? super T> bodyMatcher, ObjectMapper objectMapper) {
        return new RequestHasBody<>(bodyMatcher, objectMapper, bodyMatcher.getClass());
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param body is the expected body of the request
     * @param <T>  is the type of deserialized body
     * @return a new instance of {@link RequestHasBody}
     */
    public static <T> RequestHasBody<T> requestHasBody(T body) {
        return new RequestHasBody<>(is(body), new ObjectMapper(), body.getClass());
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param body         is the expected body of the request
     * @param objectMapper is the Jackson mapper which will deserialize actual request body
     * @param <T>          is the type of deserialized body
     * @return a new instance of {@link RequestHasBody}
     */
    public static <T> RequestHasBody<T> requestHasBody(T body, ObjectMapper objectMapper) {
        return new RequestHasBody<>(is(body), objectMapper, body.getClass());
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var postData = item.getRequest().getPostData();

        if (postData == null) {
            mismatchDescription.appendText("Request body is null");
            return false;
        }

        var requestBody = postData.getText();

        boolean result;

        try {
            result = bodyMatcher.matches(objectMapper.readValue(requestBody, objectClass));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result) {
            bodyMatcher.describeMismatch(requestBody, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("request has body %s", bodyMatcher);
    }
}
