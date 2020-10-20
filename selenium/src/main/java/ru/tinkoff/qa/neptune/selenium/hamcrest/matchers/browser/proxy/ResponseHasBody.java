package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

public class ResponseHasBody<T> extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super T> bodyMatcher;
    private final ObjectMapper objectMapper;
    private final Class<?> objectClass;

    private ResponseHasBody(Matcher<? super T> bodyMatcher, ObjectMapper objectMapper, Class<?> objectClass) {
        this.bodyMatcher = bodyMatcher;
        this.objectMapper = objectMapper;
        this.objectClass = objectClass;
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param bodyMatcher is the criteria that describes response body
     * @param <T>         is the type of deserialized body
     * @return a new instance of {@link ResponseHasBody}
     */
    public static <T> ResponseHasBody<T> responseHasBody(Matcher<? super T> bodyMatcher) {
        return new ResponseHasBody<>(bodyMatcher, new ObjectMapper(), bodyMatcher.getClass());
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param bodyMatcher  is the criteria that describes response body
     * @param objectMapper is the Jackson mapper which will deserialize actual response body
     * @param <T>          is the type of deserialized body
     * @return a new instance of {@link ResponseHasBody}
     */
    public static <T> ResponseHasBody<T> responseHasBody(Matcher<? super T> bodyMatcher, ObjectMapper objectMapper) {
        return new ResponseHasBody<>(bodyMatcher, objectMapper, bodyMatcher.getClass());
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param body is the expected body of the response
     * @param <T>  is the type of deserialized body
     * @return a new instance of {@link ResponseHasBody}
     */
    public static <T> ResponseHasBody<T> responseHasBody(T body) {
        return new ResponseHasBody<>(is(body), new ObjectMapper(), body.getClass());
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param body         is the expected body of the response
     * @param objectMapper is the Jackson mapper which will deserialize actual response body
     * @param <T>          is the type of deserialized body
     * @return a new instance of {@link ResponseHasBody}
     */
    public static <T> ResponseHasBody<T> responseHasBody(T body, ObjectMapper objectMapper) {
        return new ResponseHasBody<>(is(body), objectMapper, body.getClass());
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var content = item.getResponse().getContent();

        if (content == null) {
            mismatchDescription.appendText("Request body is null");
            return false;
        }

        var responseBody = content.getText();

        boolean result;

        try {
            result = bodyMatcher.matches(objectMapper.readValue(responseBody, objectClass));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result) {
            bodyMatcher.describeMismatch(responseBody, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("response has body %s", bodyMatcher);
    }
}
