package ru.tinkoff.qa.neptune.http.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonObjectMapper;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlObjectMapper;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter;
import ru.tinkoff.qa.neptune.http.api.response.*;

import java.io.File;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.net.http.HttpResponse.BodyHandlers.discarding;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.http.api.request.body.RequestBodyFactory.body;
import static ru.tinkoff.qa.neptune.http.api.response.GetResponseDataStepFactory.*;

/**
 * Creates instances of {@link RequestBuilder}
 */
public final class RequestBuilderFactory {

    private RequestBuilderFactory() {
        super();
    }

    /**
     * Creates a builder of a POST http request.
     *
     * @param body is a request body
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(RequestBody<?> body) {
        checkNotNull(body);
        return new RequestBuilderWithHandler<>(body) {
            @Override
            void defineRequestMethodAndBody() {
                builder.POST(this.body.createPublisher());
            }
        };
    }

    /**
     * Creates a builder of a POST http request with empty body
     *
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST() {
        return POST(body());
    }

    /**
     * Creates a builder of a POST http request with binary body
     *
     * @param body   is a full binary array
     * @param length is length of a sub-array to send
     * @param offset the offset of the first byte
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(byte[] body, int length, int offset) {
        return POST(body(body, length, offset));
    }

    /**
     * Creates a builder of a POST http request with binary body
     *
     * @param body is a binary array body
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(byte[] body) {
        return POST(body(body));
    }

    /**
     * Creates a builder of a POST http request with file to send
     *
     * @param body is a file to send
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(File body) {
        return POST(body(body));
    }

    /**
     * Creates a builder of a POST http request with file to send
     *
     * @param body is a path to a file to send
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(Path body) {
        return POST(body(body));
    }

    /**
     * Creates a builder of a POST http request with string body
     *
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(String body, Charset encoding) {
        return POST(body(body, encoding));
    }

    /**
     * Creates a builder of a POST http request with UTF-8 string body
     *
     * @param body is an original string content
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(String body) {
        return POST(body(body));
    }

    /**
     * Creates a builder of a POST http request with stream body
     *
     * @param body is an input stream body
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(InputStream body) {
        return POST(body(body));
    }

    /**
     * Creates a builder of a POST http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param body is an input stream body. It is considered that an input stream is created
     *             at the moment when http request is sent
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(Supplier<InputStream> body) {
        return POST(body(body));
    }


    /**
     * Creates a builder of a POST http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param body array of form parameters
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(FormParameter... body) {
        return POST(body(body));
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param mapper is an instance of {@link ObjectMapper} that is used to transform object
     *               into json/xml string
     * @param body   object to be serialized
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(ObjectMapper mapper, Object body) {
        return POST(body(mapper, body));
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param mapper creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *               into json/xml string.
     * @param body   object to be serialized
     * @return a POST request builder.
     * @see DefaultMapper
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder<Void> POST(DefaultMapper mapper, Object body) {
        return POST(body(mapper, body));
    }

    /**
     * Creates a builder of a POST http request with multipart body.
     *
     * @param parts are parts of multipart body
     * @return a POST request builder.
     */
    public static RequestBuilder<Void> POST(BodyPart... parts) {
        return POST(body(randomAlphanumeric(15), parts));
    }

    /**
     * Creates a builder of a GET http request.
     *
     * @return a GET request builder.
     */
    public static RequestBuilder<Void> GET() {
        return new RequestBuilderWithHandler<>(null) {
            @Override
            void defineRequestMethodAndBody() {
                builder.GET();
            }
        };
    }

    /**
     * Creates a builder of a DELETE http request.
     *
     * @return a DELETE request builder.
     */
    public static RequestBuilder<Void> DELETE() {
        return new RequestBuilderWithHandler<>(null) {
            @Override
            void defineRequestMethodAndBody() {
                builder.DELETE();
            }
        };
    }

    /**
     * Creates a builder of a PUT http request.
     *
     * @param body is a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(RequestBody<?> body) {
        return new RequestBuilderWithHandler<>(body) {
            @Override
            void defineRequestMethodAndBody() {
                builder.PUT(this.body.createPublisher());
            }
        };
    }

    /**
     * Creates a builder of a PUT http request with empty body
     *
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT() {
        return PUT(body());
    }

    /**
     * Creates a builder of a PUT http request with binary body
     *
     * @param body   is a full binary array
     * @param length is length of a sub-array to send
     * @param offset the offset of the first byte
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(byte[] body, int length, int offset) {
        return PUT(body(body, length, offset));
    }

    /**
     * Creates a builder of a PUT http request with binary body
     *
     * @param body is a binary array body
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(byte[] body) {
        return PUT(body(body));
    }

    /**
     * Creates a builder of a PUT http request with file to send
     *
     * @param body is a file to send
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(File body) {
        return PUT(body(body));
    }

    /**
     * Creates a builder of a PUT http request with file to send
     *
     * @param body is a path to a file to send
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(Path body) {
        return PUT(body(body));
    }

    /**
     * Creates a builder of a PUT http request with string body
     *
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(String body, Charset encoding) {
        return PUT(body(body, encoding));
    }

    /**
     * Creates a builder of a PUT http request with UTF-8 string body
     *
     * @param body is an original string content
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(String body) {
        return PUT(body(body));
    }

    /**
     * Creates a builder of a PUT http request with stream body
     *
     * @param body is an input stream body
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(InputStream body) {
        return PUT(body(body));
    }

    /**
     * Creates a builder of a PUT http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param body is an input stream body. It is considered that an input stream is created
     *             at the moment when http request is sent
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(Supplier<InputStream> body) {
        return PUT(body(body));
    }

    /**
     * Creates a builder of a PUT http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param body array of form parameters
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(FormParameter... body) {
        return PUT(body(body));
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param mapper is an instance of {@link ObjectMapper} that is used to transform object
     *               into json/xml string
     * @param body   object to be serialized
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(ObjectMapper mapper, Object body) {
        return PUT(body(mapper, body));
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param mapper creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *               into json/xml string.
     * @param body   object to be serialized
     * @return a PUT request builder.
     * @see DefaultMapper
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder<Void> PUT(DefaultMapper mapper, Object body) {
        return PUT(body(mapper, body));
    }

    /**
     * Creates a builder of a PUT http request with multipart body.
     *
     * @param parts are parts of multipart body
     * @return a PUT request builder.
     */
    public static RequestBuilder<Void> PUT(BodyPart... parts) {
        return PUT(body(randomAlphanumeric(15), parts));
    }

    /**
     * Creates a builder of a fluent http request.
     *
     * @param method is a name of http method
     * @param body   is a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, RequestBody<?> body) {
        checkNotNull(body);
        return new RequestBuilderWithHandler<>(body) {
            @Override
            void defineRequestMethodAndBody() {
                checkArgument(isNotBlank(method));
                builder.method(method, this.body.createPublisher());
            }
        };
    }

    /**
     * Creates a builder of a fluent http request with empty body
     *
     * @param method is a name of http method
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method) {
        return METHOD(method, body());
    }

    /**
     * Creates a builder of a fluent http request with binary body
     *
     * @param method is a name of http method
     * @param body   is a full binary array
     * @param length is length of a sub-array to send
     * @param offset the offset of the first byte
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, byte[] body, int length, int offset) {
        return METHOD(method, body(body, length, offset));
    }

    /**
     * Creates a builder of a fluent http request with binary body
     *
     * @param method is a name of http method
     * @param body   is a binary array body
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, byte[] body) {
        return METHOD(method, body(body));
    }

    /**
     * Creates a builder of a fluent http request with file to send
     *
     * @param method is a name of http method
     * @param body   is a file to send
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, File body) {
        return METHOD(method, body(body));
    }

    /**
     * Creates a builder of a fluent http request with file to send
     *
     * @param method is a name of http method
     * @param body   is a path to a file to send
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, Path body) {
        return METHOD(method, body(body));
    }

    /**
     * Creates a builder of a fluent http request with string body
     *
     * @param method   is a name of http method
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, String body, Charset encoding) {
        return METHOD(method, body(body, encoding));
    }

    /**
     * Creates a builder of a fluent http request with UTF-8 string body
     *
     * @param method is a name of http method
     * @param body   is an original string content
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, String body) {
        return METHOD(method, body(body));
    }

    /**
     * Creates a builder of a fluent http request with stream body
     *
     * @param method is a name of http method
     * @param body   is an input stream body
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, InputStream body) {
        return METHOD(method, body(body));
    }

    /**
     * Creates a builder of a fluent http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param method is a name of http method
     * @param body   is an input stream body. It is considered that an input stream is created
     *               at the moment when http request is sent
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, Supplier<InputStream> body) {
        return METHOD(method, body(body));
    }

    /**
     * Creates a builder of a fluent http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param method is a name of http method
     * @param body   array of form parameters
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, FormParameter... body) {
        return METHOD(method, body(body));
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method is a name of http method
     * @param mapper is an instance of {@link ObjectMapper} that is used to transform object
     *               into json/xml string
     * @param body   object to be serialized
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, ObjectMapper mapper, Object body) {
        return METHOD(method, body(mapper, body));
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method is a name of http method
     * @param mapper creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *               into json/xml string.
     * @param body   object to be serialized
     * @return a fluent http request builder.
     * @see DefaultMapper
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder<Void> METHOD(String method, DefaultMapper mapper, Object body) {
        return METHOD(method, body(mapper, body));
    }

    /**
     * Creates a builder of a fluent http request with multipart body.
     *
     * @param method is a name of http method
     * @param parts  are parts of multipart body
     * @return a fluent http request builder.
     */
    public static RequestBuilder<Void> METHOD(String method, BodyPart... parts) {
        return METHOD(method, body(randomAlphanumeric(15), parts));
    }

    public static abstract class RequestBuilderWithHandler<T> extends RequestBuilder<T> {

        RequestBuilderWithHandler(RequestBody<?> body) {
            super(body);
        }

        @Override
        public <R> GetObjectFromBodyStepSupplier<T, R> sendAndTryToReturn(String description, Function<T, R> f) {
            return getObjectFromResponseBody(description, this, f);
        }

        @Override
        public GetObjectFromBodyStepSupplier<T, T> sendAndTryToReturnBody() {
            return getBody(this);
        }

        @Override
        public <R, S extends Iterable<R>> GetObjectsFromIterableBodyStepSupplier<T, R, S> sendAndTryToReturnList(String description, Function<T, S> f) {
            return getListFromResponseBody(description, this, f);
        }

        @Override
        public <R> GetObjectsFromArrayBodyStepSupplier<T, R> sendAndTryToReturnArray(String description, Function<T, R[]> f) {
            return getArrayFromResponseBody(description, this, f);
        }

        @Override
        public <R, S extends Iterable<R>> GetObjectFromIterableBodyStepSupplier<T, R> sendAndTryToReturnItem(String description, Function<T, S> f) {
            return getItemFromResponseBody(description, this, f);
        }

        @Override
        public <R> GetObjectFromArrayBodyStepSupplier<T, R> sendAndTryToReturnArrayItem(String description, Function<T, R[]> f) {
            return getArrayItemFromResponseBody(description, this, f);
        }

        @SuppressWarnings("unchecked")
        public HttpResponse.BodyHandler<T> getBodyHandler() {
            return ofNullable(bodyHandler).orElseGet(() -> (HttpResponse.BodyHandler<T>) discarding());
        }
    }
}
