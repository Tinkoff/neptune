package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;

import static com.google.common.base.Preconditions.checkArgument;
import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Designed to create a request with defined common or customized http method name
 * and with body publisher
 */
public final class MethodRequest extends RequestBuilder {

    private MethodRequest(URI endPoint, String method, HttpRequest.BodyPublisher bodyPublisher) {
        super(endPoint);
        defineMethod(method, bodyPublisher);
    }

    private MethodRequest(URL url, String method, HttpRequest.BodyPublisher bodyPublisher) {
        super(url);
        defineMethod(method, bodyPublisher);
    }

    private MethodRequest(String uriExpression, String method, HttpRequest.BodyPublisher bodyPublisher) {
        super(uriExpression);
        defineMethod(method, bodyPublisher);
    }

    private void defineMethod(String method, HttpRequest.BodyPublisher bodyPublisher) {
        checkArgument(isNotBlank(method), "Method name should not be null or empty string");
        checkArgument(nonNull(bodyPublisher), "Body publisher should not be a null value");
        builder.method(method, bodyPublisher);
    }

    /**
     * Creates an instance that builds a request with defined common or customized http method name
     * and with body publisher
     *
     * @param endPoint is a request end point
     * @param method  name of the method to use
     * @param bodyPublisher the body publisher
     * @return new {@link MethodRequest}
     */
    public static MethodRequest METHOD(URI endPoint, String method, HttpRequest.BodyPublisher bodyPublisher) {
        return new MethodRequest(endPoint, method, bodyPublisher);
    }

    /**
     * Creates an instance that builds a request with defined common or customized http method name
     *
     * @param endPoint is a request end point
     * @param method  name of the method to use
     * @return new {@link MethodRequest}
     */
    public static MethodRequest METHOD(URI endPoint, String method) {
        return METHOD(endPoint, method, noBody());
    }

    /**
     * Creates an instance that builds a request with defined common or customized http method name
     * and with body publisher
     *
     * @param url is a request end point
     * @param method  name of the method to use
     * @param bodyPublisher the body publisher
     * @return new {@link MethodRequest}
     */
    public static MethodRequest METHOD(URL url, String method, HttpRequest.BodyPublisher bodyPublisher) {
        return new MethodRequest(url, method, bodyPublisher);
    }

    /**
     * Creates an instance that builds a request with defined common or customized http method name
     *
     * @param url is a request end point
     * @param method  name of the method to use
     * @return new {@link MethodRequest}
     */
    public static MethodRequest METHOD(URL url, String method) {
        return METHOD(url, method, noBody());
    }

    /**
     * Creates an instance that builds a request with defined common or customized http method name
     * and with body publisher
     *
     * @param uriExpression is a request end point
     * @param method  name of the method to use
     * @param bodyPublisher the body publisher
     * @return new {@link MethodRequest}
     */
    public static MethodRequest METHOD(String uriExpression, String method, HttpRequest.BodyPublisher bodyPublisher) {
        return new MethodRequest(uriExpression, method, bodyPublisher);
    }

    /**
     * Creates an instance that builds a request with defined common or customized http method name
     *
     * @param uriExpression is a request end point
     * @param method  name of the method to use
     * @return new {@link MethodRequest}
     */
    public static MethodRequest METHOD(String uriExpression, String method) {
        return METHOD(uriExpression, method, noBody());
    }
}
