package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hamcrest.Matcher;
import org.openqa.selenium.devtools.v95.network.Network;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NullValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.DeserializationErrorMismatch;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedRequest;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.RecordedResponse;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.selenium.properties.ProxiedTrafficBodyTransformer.PROXIED_TRAFFIC_BODY_TRANSFORMER;

@Description("{getFrom} body {responseBody}")
public final class HasContentMatcher extends NeptuneFeatureMatcher<HttpTraffic> {

    @DescriptionFragment(value = "getFrom")
    private final Object getFrom;

    @DescriptionFragment(value = "responseBody")
    private final Matcher<?> bodyMatcher;
    private final DataTransformer transformer;
    private final Class<?> objectClass;
    private final TypeReference<?> objectTypeRef;

    private HasContentMatcher(Object getFrom, Matcher<?> bodyMatcher, DataTransformer dataTransformer, Class<?> objectClass, TypeReference<?> objectTypeRef) {
        super(true);
        checkNotNull(getFrom);
        this.getFrom = getFrom;
        checkNotNull(bodyMatcher);
        this.bodyMatcher = bodyMatcher;
        this.transformer = dataTransformer;
        this.objectClass = objectClass;
        this.objectTypeRef = objectTypeRef;
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass          is a class of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param bodyMatchers    criteria to check response body
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HttpTraffic> responseHasBody(Class<T> tClass, DataTransformer dataTransformer, Matcher<? super T>... bodyMatchers) {
        checkNotNull(tClass);
        checkNotNull(dataTransformer);
        return new HasContentMatcher(new RecordedResponse(), all(bodyMatchers), dataTransformer, tClass, null);
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass          is a class of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param bodyMatcher     criteria to check response body
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HttpTraffic> responseHasBody(Class<T> tClass, DataTransformer dataTransformer, Matcher<? super T> bodyMatcher) {
        return responseHasBody(tClass, dataTransformer, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass          is a class of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> responseHasBody(Class<T> tClass, DataTransformer dataTransformer) {
        return responseHasBody(tClass, dataTransformer, anything());
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param t               is expected response body
     * @param dataTransformer performs deserialization
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HttpTraffic> responseHasBody(T t, DataTransformer dataTransformer) {
        return responseHasBody((Class<T>) t.getClass(), dataTransformer, equalTo(t));
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
    public static <T> Matcher<HttpTraffic> responseHasBody(Class<T> tClass, Matcher<? super T>... bodyMatchers) {
        return responseHasBody(tClass, PROXIED_TRAFFIC_BODY_TRANSFORMER.get(), bodyMatchers);
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
    public static <T> Matcher<HttpTraffic> responseHasBody(Class<T> tClass, Matcher<? super T> bodyMatcher) {
        return responseHasBody(tClass, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tClass is a class of a value of deserialized string body
     * @param <T>    is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> responseHasBody(Class<T> tClass) {
        return responseHasBody(tClass, anything());
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param t   is expected response body
     * @param <T> is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> responseHasBody(T t) {
        return responseHasBody(t, PROXIED_TRAFFIC_BODY_TRANSFORMER.get());
    }


    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference  is a reference to type of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param bodyMatchers    criteria to check response body
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HttpTraffic> responseHasBody(TypeReference<T> tTypeReference, DataTransformer dataTransformer, Matcher<? super T>... bodyMatchers) {
        checkNotNull(tTypeReference);
        checkNotNull(dataTransformer);
        return new HasContentMatcher(new RecordedResponse(), all(bodyMatchers), dataTransformer, null, tTypeReference);
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference  is a reference to type of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param bodyMatcher     criteria to check response body
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HttpTraffic> responseHasBody(TypeReference<T> tTypeReference, DataTransformer dataTransformer, Matcher<? super T> bodyMatcher) {
        return responseHasBody(tTypeReference, dataTransformer, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference  is a reference to type of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> responseHasBody(TypeReference<T> tTypeReference, DataTransformer dataTransformer) {
        return responseHasBody(tTypeReference, dataTransformer, anything());
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
    public static <T> Matcher<HttpTraffic> responseHasBody(TypeReference<T> tTypeReference, Matcher<? super T>... bodyMatchers) {
        return responseHasBody(tTypeReference, PROXIED_TRAFFIC_BODY_TRANSFORMER.get(), bodyMatchers);
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
    public static <T> Matcher<HttpTraffic> responseHasBody(TypeReference<T> tTypeReference, Matcher<? super T> bodyMatcher) {
        return responseHasBody(tTypeReference, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the response.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> responseHasBody(TypeReference<T> tTypeReference) {
        return responseHasBody(tTypeReference, anything());
    }


    /**
     * Creates matcher that checks body of the response.
     *
     * @param bodyMatchers criteria that describes response body
     * @return a new instance of {@link HasContentMatcher}
     */
    @SafeVarargs
    public static Matcher<HttpTraffic> responseHasRawBody(Matcher<? super String>... bodyMatchers) {
        return new HasContentMatcher(new RecordedResponse(), all(bodyMatchers), null, null, null);
    }

    /**
     * Creates matcher that checks body of the response.
     *
     * @param bodyMatcher criteria that describes response body
     * @return a new instance of {@link HasContentMatcher}
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HttpTraffic> responseHasRawBody(Matcher<? super String> bodyMatcher) {
        return responseHasRawBody(new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks body of the response.
     *
     * @param body is the expected body of the response
     * @return a new instance of {@link HasContentMatcher}
     */
    public static Matcher<HttpTraffic> responseHasRawBody(String body) {
        return responseHasRawBody(equalTo(body));
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass          is a class of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param bodyMatchers    criteria to check request body
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HttpTraffic> requestHasBody(Class<T> tClass, DataTransformer dataTransformer, Matcher<? super T>... bodyMatchers) {
        checkNotNull(tClass);
        checkNotNull(dataTransformer);
        return new HasContentMatcher(new RecordedRequest(), all(bodyMatchers), dataTransformer, tClass, null);
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass          is a class of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param bodyMatcher     criteria to check request body
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HttpTraffic> requestHasBody(Class<T> tClass, DataTransformer dataTransformer, Matcher<? super T> bodyMatcher) {
        return requestHasBody(tClass, dataTransformer, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass          is a class of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> requestHasBody(Class<T> tClass, DataTransformer dataTransformer) {
        return requestHasBody(tClass, dataTransformer, anything());
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param t               is expected request body
     * @param dataTransformer performs deserialization
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HttpTraffic> requestHasBody(T t, DataTransformer dataTransformer) {
        return requestHasBody((Class<T>) t.getClass(), dataTransformer, equalTo(t));
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
    public static <T> Matcher<HttpTraffic> requestHasBody(Class<T> tClass, Matcher<? super T>... bodyMatchers) {
        return requestHasBody(tClass, PROXIED_TRAFFIC_BODY_TRANSFORMER.get(), bodyMatchers);
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
    public static <T> Matcher<HttpTraffic> requestHasBody(Class<T> tClass, Matcher<? super T> bodyMatcher) {
        return requestHasBody(tClass, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tClass is a class of a value of deserialized string body
     * @param <T>    is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> requestHasBody(Class<T> tClass) {
        return requestHasBody(tClass, anything());
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param t   is expected request body
     * @param <T> is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> requestHasBody(T t) {
        return requestHasBody(t, PROXIED_TRAFFIC_BODY_TRANSFORMER.get());
    }


    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference  is a reference to type of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param bodyMatchers    criteria to check request body
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<HttpTraffic> requestHasBody(TypeReference<T> tTypeReference, DataTransformer dataTransformer, Matcher<? super T>... bodyMatchers) {
        checkNotNull(tTypeReference);
        checkNotNull(dataTransformer);
        return new HasContentMatcher(new RecordedRequest(), all(bodyMatchers), dataTransformer, null, tTypeReference);
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference  is a reference to type of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param bodyMatcher     criteria to check request body
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<HttpTraffic> requestHasBody(TypeReference<T> tTypeReference, DataTransformer dataTransformer, Matcher<? super T> bodyMatcher) {
        return requestHasBody(tTypeReference, dataTransformer, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference  is a reference to type of a value of deserialized string body
     * @param dataTransformer performs deserialization
     * @param <T>             is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> requestHasBody(TypeReference<T> tTypeReference, DataTransformer dataTransformer) {
        return requestHasBody(tTypeReference, dataTransformer, anything());
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
    public static <T> Matcher<HttpTraffic> requestHasBody(TypeReference<T> tTypeReference, Matcher<? super T>... bodyMatchers) {
        return requestHasBody(tTypeReference, PROXIED_TRAFFIC_BODY_TRANSFORMER.get(), bodyMatchers);
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
    public static <T> Matcher<HttpTraffic> requestHasBody(TypeReference<T> tTypeReference, Matcher<? super T> bodyMatcher) {
        return requestHasBody(tTypeReference, new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks deserialized body of the request.
     *
     * @param tTypeReference is a reference to type of a value of deserialized string body
     * @param <T>            is a type of a value of deserialized string body
     * @return a matcher
     */
    public static <T> Matcher<HttpTraffic> requestHasBody(TypeReference<T> tTypeReference) {
        return requestHasBody(tTypeReference, anything());
    }


    /**
     * Creates matcher that checks body of the request.
     *
     * @param bodyMatchers criteria that describes request body
     * @return a new instance of {@link HasContentMatcher}
     */
    @SafeVarargs
    public static Matcher<HttpTraffic> requestHasRawBody(Matcher<? super String>... bodyMatchers) {
        return new HasContentMatcher(new RecordedRequest(), all(bodyMatchers), null, null, null);
    }

    /**
     * Creates matcher that checks body of the request.
     *
     * @param bodyMatcher criteria that describes request body
     * @return a new instance of {@link HasContentMatcher}
     */
    @SuppressWarnings("unchecked")
    public static Matcher<HttpTraffic> requestHasRawBody(Matcher<? super String> bodyMatcher) {
        return requestHasRawBody(new Matcher[]{bodyMatcher});
    }

    /**
     * Creates matcher that checks body of the request.
     *
     * @param body is the expected body of the request
     * @return a new instance of {@link HasContentMatcher}
     */
    public static Matcher<HttpTraffic> requestHasRawBody(String body) {
        return requestHasRawBody(equalTo(body));
    }

    @Override
    protected boolean featureMatches(HttpTraffic toMatch) {
        String body;
        if (getFrom instanceof RecordedResponse) {
            body = ofNullable(toMatch.getBody()).map(Network.GetResponseBodyResponse::getBody).orElse(null);
        } else {
            body = toMatch.getRequest().getRequest().getPostData().orElse(null);
        }

        if (body == null) {
            appendMismatchDescription(new NullValueMismatch());
            return false;
        }

        if (transformer != null) {
            try {
                Object o;
                if (objectClass != null) {
                    o = transformer.deserialize(body, objectClass);
                } else {
                    o = transformer.deserialize(body, objectTypeRef);
                }

                var result = bodyMatcher.matches(o);
                if (!result) {
                    appendMismatchDescription(bodyMatcher, o);
                    return false;
                }

            } catch (Exception e) {
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
