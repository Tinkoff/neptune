package ru.tinkoff.qa.neptune.http.api.service.mapping;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;
import static java.util.Arrays.asList;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod.HttpMethodFactory.createRequestBuilder;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyParameterAnnotationReader.readBodies;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathParameter.PathParameterReader.readPathParameters;

class HttpAPIProxyHandler implements InvocationHandler {

    private static final String USE_FOR_REQUEST_BUILDING = "useForRequestBuilding";

    private final List<Object> requestTuners = new LinkedList<>();
    private final URI rootURI;

    HttpAPIProxyHandler(URI rootURI) {
        checkNotNull(rootURI);
        this.rootURI = rootURI;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        method.setAccessible(true);

        var paramTypes = method.getParameterTypes();
        if (USE_FOR_REQUEST_BUILDING.equals(method.getName()) &&
                paramTypes.length == 1
                && (paramTypes[0].equals(RequestTuner[].class) || (paramTypes[0].equals(Class.class)))) {
            if (RequestTuner[].class.isAssignableFrom(paramTypes[0])) {
                requestTuners.addAll(asList((RequestTuner[]) args[0]));
            } else {
                requestTuners.add(args[0]);
            }
            return proxy;
        }

        if (RequestBuilder.class.isAssignableFrom(method.getReturnType())) {

            if (method.isDefault()) {
                Class<?> declaringClass = method.getDeclaringClass();
                return privateLookupIn(declaringClass, lookup())
                        .in(declaringClass)
                        .unreflectSpecial(method, declaringClass)
                        .bindTo(proxy)
                        .invokeWithArguments(args);
            }

            var path = readPathParameters(method, args);
            var body = readBodies(method, args);
            var request = createRequestBuilder(method, rootURI, path, body);

            request.tuneWith(new ProxyRequestTuner(method, args));
            requestTuners.forEach(o -> {
                var cls = o.getClass();
                if (RequestTuner.class.isAssignableFrom(cls)) {
                    request.tuneWith((RequestTuner) o);
                } else if (Class.class.isAssignableFrom(cls)) {
                    request.tuneWith((Class<RequestTuner>) o);
                }
            });

            return request;
        }

        throw new UnsupportedOperationException(format("Only methods that return %s or " +
                        "default methods are supported. Method %s is not supported",
                RequestBuilder.class.getName(),
                method));

    }
}
