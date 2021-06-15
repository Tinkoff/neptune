package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarContent;
import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarPostData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NullValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.DeserializationErrorMismatch;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedRequest;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.AnyThingMatcher.anything;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;

@Description("{getFrom} body {responseBody}")
public final class HasContentMatcher extends NeptuneFeatureMatcher<HarEntry> {

    @DescriptionFragment(value = "getFrom")
    private final Object getFrom;

    @DescriptionFragment(value = "responseBody", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<?> bodyMatcher;
    private final ObjectMapper objectMapper;
    private final Class<?> objectClass;
    private final TypeReference<?> objectTypeRef;

    private HasContentMatcher(Object getFrom, Matcher<?> bodyMatcher, ObjectMapper objectMapper, Class<?> objectClass, TypeReference<?> objectTypeRef) {
        super(true);
        checkNotNull(getFrom);
        this.getFrom = getFrom;
        checkNotNull(bodyMatcher);
        this.bodyMatcher = bodyMatcher;
        this.objectMapper = objectMapper;
        this.objectClass = objectClass;
        this.objectTypeRef = objectTypeRef;
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass       is a class of a value of deserialized string body
     * @param mapper       is a mapper that performs deserialization
     * @param bodyMatchers criteria to check response body
     * @param <T>          is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HarEntry> responseHasBody(Class<T> tClass, ObjectMapper mapper, Matcher<? super T>... bodyMatchers) {
        checkNotNull(tClass);
        checkNotNull(mapper);
        return new HasContentMatcher(new RecordedResponse(), all(bodyMatchers), mapper, tClass, null);
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass      is a class of a value of deserialized string body
     * @param mapper      is a mapper that performs deserialization
     * @param bodyMatcher criteria to check response body
     * @param <T>         is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> responseHasBody(Class<T> tClass, ObjectMapper mapper, Matcher<? super T> bodyMatcher) {
        return responseHasBody(tClass, mapper, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass is a class of a value of deserialized string body
     * @param mapper is a mapper that performs deserialization
     * @param <T>    is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> responseHasBody(Class<T> tClass, ObjectMapper mapper) {
        return responseHasBody(tClass, mapper, anything());
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param t      is expected response body
     * @param mapper is a mapper that performs deserialization
     * @param <T>    is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> responseHasBody(T t, ObjectMapper mapper) {
        return responseHasBody((Class<T>) t.getClass(), mapper, equalTo(t));
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass       is a class of a value of deserialized string body
     * @param bodyMatchers criteria to check response body
     * @param <T>          is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HarEntry> responseHasBody(Class<T> tClass, Matcher<? super T>... bodyMatchers) {
        return responseHasBody(tClass, new ObjectMapper(), bodyMatchers);
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass      is a class of a value of deserialized string body
     * @param bodyMatcher criteria to check response body
     * @param <T>         is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> responseHasBody(Class<T> tClass, Matcher<? super T> bodyMatcher) {
        return responseHasBody(tClass, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass is a class of a value of deserialized string body
     * @param <T>    is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> responseHasBody(Class<T> tClass) {
        return responseHasBody(tClass, anything());
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param t   is expected response body
     * @param <T> is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> responseHasBody(T t) {
        return responseHasBody(t, new ObjectMapper());
    }


    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param mapper         is a mapper that performs deserialization
     * @param bodyMatchers   criteria to check response body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HarEntry> responseHasBody(TypeReference<T> tTypeReference, ObjectMapper mapper, Matcher<? super T>... bodyMatchers) {
        checkNotNull(tTypeReference);
        checkNotNull(mapper);
        return new HasContentMatcher(new RecordedResponse(), all(bodyMatchers), mapper, null, tTypeReference);
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param mapper         is a mapper that performs deserialization
     * @param bodyMatcher    criteria to check response body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> responseHasBody(TypeReference<T> tTypeReference, ObjectMapper mapper, Matcher<? super T> bodyMatcher) {
        return responseHasBody(tTypeReference, mapper, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param mapper         is a mapper that performs deserialization
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> responseHasBody(TypeReference<T> tTypeReference, ObjectMapper mapper) {
        return responseHasBody(tTypeReference, mapper, anything());
    }


    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param bodyMatchers   criteria to check response body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HarEntry> responseHasBody(TypeReference<T> tTypeReference, Matcher<? super T>... bodyMatchers) {
        return responseHasBody(tTypeReference, new ObjectMapper(), bodyMatchers);
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param bodyMatcher    criteria to check response body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> responseHasBody(TypeReference<T> tTypeReference, Matcher<? super T> bodyMatcher) {
        return responseHasBody(tTypeReference, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> responseHasBody(TypeReference<T> tTypeReference) {
        return responseHasBody(tTypeReference, anything());
    }


    /**
     * Creates matcher that checks body of the response.
     *
     * @param bodyMatchers criteria that describes response body
     * @return a new instance of {@link HasContentMatcher}
     */
    @SafeVarargs
    public static Matcher<HarEntry> responseHasRawBody(Matcher<? super String>... bodyMatchers) {
        return new HasContentMatcher(new RecordedResponse(), all(bodyMatchers), null, null, null);
    }

    /**
     * Creates matcher that checks body of the response.
     *
     * @param bodyMatcher criteria that describes response body
     * @return a new instance of {@link HasContentMatcher}
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HarEntry> responseHasRawBody(Matcher<? super String> bodyMatcher) {
        return responseHasRawBody(new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks body of the response.
     *
     * @param body is the expected body of the response
     * @return a new instance of {@link HasContentMatcher}
     */
    public static Matcher<HarEntry> responseHasRawBody(String body) {
        return responseHasRawBody(equalTo(body));
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass       is a class of a value of deserialized string body
     * @param mapper       is a mapper that performs deserialization
     * @param bodyMatchers criteria to check request body
     * @param <T>          is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HarEntry> requestHasBody(Class<T> tClass, ObjectMapper mapper, Matcher<? super T>... bodyMatchers) {
        checkNotNull(tClass);
        checkNotNull(mapper);
        return new HasContentMatcher(new RecordedRequest(), all(bodyMatchers), mapper, tClass, null);
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass      is a class of a value of deserialized string body
     * @param mapper      is a mapper that performs deserialization
     * @param bodyMatcher criteria to check request body
     * @param <T>         is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> requestHasBody(Class<T> tClass, ObjectMapper mapper, Matcher<? super T> bodyMatcher) {
        return requestHasBody(tClass, mapper, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass is a class of a value of deserialized string body
     * @param mapper is a mapper that performs deserialization
     * @param <T>    is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> requestHasBody(Class<T> tClass, ObjectMapper mapper) {
        return requestHasBody(tClass, mapper, anything());
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param t      is expected request body
     * @param mapper is a mapper that performs deserialization
     * @param <T>    is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> requestHasBody(T t, ObjectMapper mapper) {
        return requestHasBody((Class<T>) t.getClass(), mapper, equalTo(t));
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass       is a class of a value of deserialized string body
     * @param bodyMatchers criteria to check request body
     * @param <T>          is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HarEntry> requestHasBody(Class<T> tClass, Matcher<? super T>... bodyMatchers) {
        return requestHasBody(tClass, new ObjectMapper(), bodyMatchers);
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass      is a class of a value of deserialized string body
     * @param bodyMatcher criteria to check request body
     * @param <T>         is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> requestHasBody(Class<T> tClass, Matcher<? super T> bodyMatcher) {
        return requestHasBody(tClass, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass is a class of a value of deserialized string body
     * @param <T>    is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> requestHasBody(Class<T> tClass) {
        return requestHasBody(tClass, anything());
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param t   is expected request body
     * @param <T> is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> requestHasBody(T t) {
        return requestHasBody(t, new ObjectMapper());
    }


    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param mapper         is a mapper that performs deserialization
     * @param bodyMatchers   criteria to check request body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HarEntry> requestHasBody(TypeReference<T> tTypeReference, ObjectMapper mapper, Matcher<? super T>... bodyMatchers) {
        checkNotNull(tTypeReference);
        checkNotNull(mapper);
        return new HasContentMatcher(new RecordedRequest(), all(bodyMatchers), mapper, null, tTypeReference);
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param mapper         is a mapper that performs deserialization
     * @param bodyMatcher    criteria to check request body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> requestHasBody(TypeReference<T> tTypeReference, ObjectMapper mapper, Matcher<? super T> bodyMatcher) {
        return requestHasBody(tTypeReference, mapper, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param mapper         is a mapper that performs deserialization
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> requestHasBody(TypeReference<T> tTypeReference, ObjectMapper mapper) {
        return requestHasBody(tTypeReference, mapper, anything());
    }


    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param bodyMatchers   criteria to check request body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HarEntry> requestHasBody(TypeReference<T> tTypeReference, Matcher<? super T>... bodyMatchers) {
        return requestHasBody(tTypeReference, new ObjectMapper(), bodyMatchers);
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param bodyMatcher    criteria to check request body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HarEntry> requestHasBody(TypeReference<T> tTypeReference, Matcher<? super T> bodyMatcher) {
        return requestHasBody(tTypeReference, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HarEntry> requestHasBody(TypeReference<T> tTypeReference) {
        return requestHasBody(tTypeReference, anything());
    }


    /**
     * Creates matcher that checks body of the request.
     *
     * @param bodyMatchers criteria that describes request body
     * @return a new instance of {@link HasContentMatcher}
     */
    @SafeVarargs
    public static Matcher<HarEntry> requestHasRawBody(Matcher<? super String>... bodyMatchers) {
        return new HasContentMatcher(new RecordedRequest(), all(bodyMatchers), null, null, null);
    }

    /**
     * Creates matcher that checks body of the request.
     *
     * @param bodyMatcher criteria that describes request body
     * @return a new instance of {@link HasContentMatcher}
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HarEntry> requestHasRawBody(Matcher<? super String> bodyMatcher) {
        return requestHasRawBody(new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks body of the request.
     *
     * @param body is the expected body of the request
     * @return a new instance of {@link HasContentMatcher}
     */
    public static Matcher<HarEntry> requestHasRawBody(String body) {
        return requestHasRawBody(equalTo(body));
    }

    @Override
    protected boolean featureMatches(HarEntry toMatch) {
        String body;
        if (getFrom instanceof RecordedResponse) {
            body = ofNullable(toMatch.getResponse().getContent()).map(HarContent::getText).orElse(null);
        } else {
            body = ofNullable(toMatch.getRequest().getPostData()).map(HarPostData::getText).orElse(null);
        }

        if (body == null) {
            appendMismatchDescription(new NullValueMismatch());
            return false;
        }

        Object toCheck;
        if (objectMapper != null) {
            try {
                Object o;
                if (objectClass != null) {
                    o = objectMapper.readValue(body, objectClass);
                } else {
                    o = objectMapper.readValue(body, objectTypeRef);
                }

                var result = bodyMatcher.matches(o);
                if (!result) {
                    appendMismatchDescription(bodyMatcher, o);
                    return false;
                }

            } catch (JsonProcessingException e) {
                appendMismatchDescription(new DeserializationErrorMismatch(body, objectClass != null ?
                        objectClass :
                        objectTypeRef.getType()));
                return false;
            }
        } else {
            var result = bodyMatcher.matches(body);
            if (!result) {
                appendMismatchDescription(bodyMatcher, body);
                return false;
            }
        }

        return true;
    }
}
