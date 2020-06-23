package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body;

import ru.tinkoff.qa.neptune.http.api.dto.DTObject;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart.DefineContentType;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart.DefineFileName;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.url.encoded.FormParameter;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.http.api.request.body.RequestBodyFactory.body;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.ParameterAnnotationTransformer.getFromMethod;

/**
 * Util class that reads parameters of a {@link java.lang.reflect.Method} and
 * creates an object of {@link ru.tinkoff.qa.neptune.http.api.request.body.RequestBody}
 */
public final class BodyParameterAnnotationReader {

    private BodyParameterAnnotationReader() {
        super();
    }

    /**
     * Reads parameters of a {@link java.lang.reflect.Method} and parameters of its current invocation
     * and then creates an object of {@link ru.tinkoff.qa.neptune.http.api.request.body.RequestBody}.
     *
     * @param toRead     is a method to be read
     * @param parameters parameters of current invocation of the method
     * @return a request body
     */
    public static RequestBody<?> readBodies(Method toRead, Object[] parameters) {
        return ofNullable(validateAndGetBodyStrategy(toRead, parameters))
                .map(aClass -> {
                    if (Body.class.isAssignableFrom(aClass)) {
                        return getBody(toRead, parameters);
                    }

                    if (FormParameter.class.isAssignableFrom(aClass)) {
                        return getForm(toRead, parameters);
                    }

                    return getMultipartBody(toRead, parameters);
                })
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static RequestBody<?> getBody(Method toRead, Object[] parameters) {
        return getFromMethod(toRead,
                Body.class,
                parameters,
                (ps, params) -> ofNullable(params[0])
                        .map(o -> {
                            var bodyValue = ps[0].getAnnotation(Body.class).format().format(o);
                            var cls = bodyValue.getClass();

                            if (String.class.isAssignableFrom(cls)) {
                                return body((String) bodyValue);
                            }

                            if (byte[].class.isAssignableFrom(cls)) {
                                return body((byte[]) bodyValue);
                            }

                            if (File.class.isAssignableFrom(cls)) {
                                return body((File) bodyValue);
                            }

                            if (Path.class.isAssignableFrom(cls)) {
                                return body((Path) bodyValue);
                            }

                            if (InputStream.class.isAssignableFrom(cls)) {
                                return body((InputStream) bodyValue);
                            }

                            if (Supplier.class.isAssignableFrom(cls)) {
                                return body((Supplier<InputStream>) bodyValue);
                            }

                            if (org.w3c.dom.Document.class.isAssignableFrom(cls)) {
                                return body((org.w3c.dom.Document) bodyValue);
                            }

                            if (org.w3c.dom.Document.class.isAssignableFrom(cls)) {
                                return body((org.jsoup.nodes.Document) bodyValue);
                            }

                            if (org.w3c.dom.Document.class.isAssignableFrom(cls)) {
                                return body((org.jsoup.nodes.Document) bodyValue);
                            }

                            if (DTObject.class.isAssignableFrom(cls)) {
                                return body((DTObject) bodyValue);
                            }

                            if (Map.class.isAssignableFrom(cls)) {
                                return body(((Map<?, ?>) bodyValue)
                                        .entrySet()
                                        .stream()
                                        .collect(toMap(entry -> valueOf(entry.getKey()),
                                                entry -> valueOf(entry.getValue()))));
                            }

                            return body(valueOf(bodyValue));
                        })
                        .orElse(null));
    }

    private static RequestBody<?> getForm(Method toRead, Object[] parameters) {
        return getFromMethod(toRead,
                FormParameter.class,
                parameters,
                (ps, params) -> {
                    var form = new LinkedHashMap<String, String>();
                    for (int i = 0; i < ps.length; i++) {
                        var formParameter = ps[i].getAnnotation(FormParameter.class);
                        ofNullable(params[i]).ifPresent(o -> form.put(formParameter.name(), valueOf(o)));
                    }
                    if (form.size() > 0) {
                        return body(form);
                    }
                    return null;
                });
    }

    private static RequestBody<?> getMultipartBody(Method toRead, Object[] parameters) {
        return getFromMethod(toRead,
                MultiPartBody.class,
                parameters,
                (ps, params) -> {
                    var partList = new ArrayList<BodyPart>();

                    for (int i = 0; i < ps.length; i++) {
                        var part = ps[i].getAnnotation(MultiPartBody.class);
                        var defineFile = ps[i].getAnnotation(DefineFileName.class);
                        var defineContent = ps[i].getAnnotation(DefineContentType.class);


                        ofNullable(params[i])
                                .ifPresent(o -> {
                                    var bodyValue = part.format().format(o);
                                    var cls = bodyValue.getClass();

                                    BodyPart bp;
                                    if (String.class.isAssignableFrom(cls)) {
                                        bp = defineFile != null
                                                ? bodyPart((String) bodyValue, part.name(), randomAlphanumeric(20))
                                                : bodyPart((String) bodyValue, part.name());
                                    } else if (byte[].class.isAssignableFrom(cls)) {
                                        bp = defineFile != null
                                                ? bodyPart((byte[]) bodyValue, part.name(), randomAlphanumeric(20))
                                                : bodyPart((byte[]) bodyValue, part.name());
                                    } else if (InputStream.class.isAssignableFrom(cls)) {
                                        bp = defineFile != null
                                                ? bodyPart((InputStream) bodyValue, part.name(), randomAlphanumeric(20))
                                                : bodyPart((InputStream) bodyValue, part.name());
                                    } else if (DTObject.class.isAssignableFrom(cls)) {
                                        bp = defineFile != null
                                                ? bodyPart((DTObject) bodyValue, part.name(), randomAlphanumeric(20))
                                                : bodyPart((DTObject) bodyValue, part.name());
                                    } else if (File.class.isAssignableFrom(cls)) {
                                        bp = ofNullable(defineFile)
                                                .map(d -> {
                                                    if (d.useGivenFileName()) {
                                                        return bodyPart((File) bodyValue, part.name(), true, nonNull(defineContent));
                                                    }
                                                    return bodyPart((File) bodyValue, part.name(), randomAlphanumeric(20), nonNull(defineContent));
                                                })
                                                .orElseGet(() -> bodyPart((File) bodyValue, part.name(), false, nonNull(defineContent)));
                                    } else if (Path.class.isAssignableFrom(cls)) {
                                        bp = ofNullable(defineFile)
                                                .map(d -> {
                                                    if (d.useGivenFileName()) {
                                                        return bodyPart((Path) bodyValue, part.name(), true, nonNull(defineContent));
                                                    }
                                                    return bodyPart((Path) bodyValue, part.name(), randomAlphanumeric(20), nonNull(defineContent));
                                                })
                                                .orElseGet(() -> bodyPart((Path) bodyValue, part.name(), false, nonNull(defineContent)));
                                    } else {
                                        bp = defineFile != null
                                                ? bodyPart(bodyValue, part.name(), randomAlphanumeric(20))
                                                : bodyPart(bodyValue, part.name());
                                    }

                                    if (nonNull(defineContent) && isNotBlank(defineContent.contentType())) {
                                        bp = bp.setContentType(defineContent.contentType());
                                    }

                                    partList.add(bp.setContentTransferEncoding(part.contentTransferEncoding()));
                                });
                    }

                    if (partList.size() > 0) {
                        return body(randomAlphanumeric(15), partList.toArray(new BodyPart[]{}));
                    }
                    return null;
                });
    }

    private static Class<? extends Annotation> validateAndGetBodyStrategy(Method m, Object[] parameterValues) {
        var bodies = getFromMethod(m,
                Body.class,
                parameterValues, (ps, ignored) -> ps);

        var formParameters = getFromMethod(m,
                FormParameter.class,
                parameterValues, (ps, ignored) -> ps);

        var multiParts = getFromMethod(m,
                MultiPartBody.class,
                parameterValues, (ps, ignored) -> ps);

        if ((nonNull(bodies) && nonNull(formParameters))
                || (nonNull(formParameters) && nonNull(multiParts))
                || (nonNull(bodies) && nonNull(multiParts))) {
            throw new IllegalStateException(format("Only one of %s, %s or %s should annotate parameters of the %s. Combinations of " +
                            "listed annotations are not allowed",
                    Body.class.getName(),
                    FormParameter.class.getName(),
                    MultiPartBody.class.getName(),
                    m));
        }

        if (nonNull(bodies)) {
            if (bodies.length > 1) {
                throw new IllegalStateException(format("Only one parameter of the %s should be annotated by %s",
                        m,
                        Body.class.getName()));
            }

            return Body.class;
        }

        if (nonNull(formParameters)) {
            return FormParameter.class;
        }

        if (nonNull(multiParts)) {
            return MultiPartBody.class;
        }

        return null;
    }
}
