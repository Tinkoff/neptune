package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.URI;

import static java.lang.String.format;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.*;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.NON_DEFINED;

/**
 * Marks a {@link java.lang.reflect.Method} and defines an invoked http method
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface HttpMethod {

    /**
     * Defines http method to be invoked. {@link #httpMethodStr()} should not be defined
     *
     * @return an item of the enum {@link DefaultHttpMethods}
     */
    DefaultHttpMethods httpMethod() default NON_DEFINED;

    /**
     * Defines a string name of http method to be invoked. This is good when name of http
     * method differs from GET, POST, PUT, DELETE. {@link #httpMethod()} should not be defined
     *
     * @return string value of http method to be invoked.
     */
    String httpMethodStr() default EMPTY;

    /**
     * Util class that reads a {@link java.lang.reflect.Method} and
     * creates {@link ru.tinkoff.qa.neptune.http.api.request.RequestBuilder}.
     */
    class HttpMethodFactory {

        /**
         * Reads a {@link java.lang.reflect.Method} and
         * creates {@link ru.tinkoff.qa.neptune.http.api.request.RequestBuilder}.
         *
         * @param toRead is a method to be read
         * @param uri    as a root URI of an API that should consist of scheme, host and port
         * @param path   is a path to requested end point
         * @param body   is a body of resulted request
         * @return an instance of {@link RequestBuilder}
         */
        public static RequestBuilder createRequestBuilder(Method toRead,
                                                          URI uri,
                                                          String path,
                                                          RequestBody<?> body) {
            return ofNullable(toRead.getAnnotation(HttpMethod.class))
                    .map(httpMethod -> {
                        var methodEnum = httpMethod.httpMethod();
                        var methodStr = httpMethod.httpMethodStr();

                        if ((methodEnum != NON_DEFINED) && (isNotBlank(methodStr))) {
                            throw new UnsupportedOperationException(format("Only one of 'httpMethod' or " +
                                            "'httpMethodStr' should be defined in %s of " +
                                            "%s",
                                    HttpMethod.class.getName(),
                                    toRead));
                        }

                        if ((methodEnum == NON_DEFINED) && (isBlank(methodStr))) {
                            throw new UnsupportedOperationException(format("One of 'httpMethod' or " +
                                            "'httpMethodStr' should be defined in %s of " +
                                            "%s",
                                    HttpMethod.class.getName(),
                                    toRead));
                        }

                        var requestURI = ofNullable(path)
                                .map(s -> URI.create(uri.toString() + "/" + path))
                                .orElse(uri);

                        if (methodEnum != NON_DEFINED) {
                            return methodEnum.prepareRequestBuilder(requestURI, body);
                        }

                        return ofNullable(body)
                                .map(b -> METHOD(methodStr, requestURI, b))
                                .orElseGet(() -> METHOD(methodStr, requestURI));
                    })
                    .orElseThrow(() -> new UnsupportedOperationException(format("Method %s is not annotated " +
                                    "by %s. Can't instantiate %s",
                            toRead,
                            HttpMethod.class.getName(),
                            RequestBuilder.class.getName())));
        }
    }
}
