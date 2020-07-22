package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body;

import ru.tinkoff.qa.neptune.http.api.dto.DTObject;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineContentType;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineFileName;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormParam;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
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
import static ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter.formParameter;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterUtil.getFromMethod;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.NONE;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.FORM;

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

                    if (URLEncodedParameter.class.isAssignableFrom(aClass)) {
                        return getForm(toRead, parameters);
                    }

                    return getMultipartBody(toRead, parameters);
                })
                .orElse(null);
    }

    private static Object formatObject(Object o, Parameter p) {
        var f = p.getAnnotation(BodyParamFormat.class);

        var format = ofNullable(f)
                .map(BodyParamFormat::format)
                .orElse(NONE);

        var mixIns = ofNullable(f)
                .map(BodyParamFormat::mixIns)
                .orElseGet(() -> new Class[]{});

        return format.format(o, mixIns);
    }

    @SuppressWarnings("unchecked")
    private static RequestBody<?> getBody(Method toRead, Object[] parameters) {
        return getFromMethod(toRead,
                Body.class,
                parameters,
                (ps, params) -> {
                    var a = ps[0].getAnnotation(Body.class);
                    return ofNullable(params[0])
                            .map(o -> {
                                var bodyValue = formatObject(o, ps[0]);
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

                                if (org.jsoup.nodes.Document.class.isAssignableFrom(cls)) {
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
                                            .collect(toMap(entry -> valueOf(entry.getKey()), Map.Entry::getValue)));
                                }

                                if (FormParameter[].class.isAssignableFrom(cls)) {
                                    return body((FormParameter[]) bodyValue);
                                }

                                if (FormParameter.class.isAssignableFrom(cls)) {
                                    return body((FormParameter) bodyValue);
                                }

                                return body(valueOf(bodyValue));
                            })
                            .orElseGet(() -> {
                                if (a.isRequired()) {
                                    throw new IllegalArgumentException(format("Method %s requires body that differs from null", toRead));
                                }
                                return null;
                            });
                });
    }

    private static RequestBody<?> getForm(Method toRead, Object[] parameters) {
        return getFromMethod(toRead,
                URLEncodedParameter.class,
                parameters,
                (ps, params) -> {
                    var form = new LinkedList<FormParameter>();

                    for (int i = 0; i < ps.length; i++) {
                        var urlEncodedParam = ps[i].getAnnotation(URLEncodedParameter.class);

                        var value = ofNullable(params[i])
                                .orElseGet(() -> {
                                    if (urlEncodedParam.isRequired()) {
                                        throw new IllegalArgumentException(format("Method %s requires form parameter %s that differs from null",
                                                toRead,
                                                urlEncodedParam.name()));
                                    }

                                    return null;
                                });

                        if (value == null) {
                            continue;
                        }

                        var format = ps[i].getAnnotation(BodyParamFormat.class);
                        var formParam = ps[i].getAnnotation(FormParam.class);

                        if (format != null && formParam != null) {
                            throw new IllegalArgumentException(format("Only one of %s and %s is allowed. Method %s. " +
                                            "Parameter %s is annotated by both annotations",
                                    BodyParamFormat.class.getSimpleName(),
                                    FormParam.class.getSimpleName(),
                                    toRead,
                                    urlEncodedParam.name()));
                        }

                        if (format != null) {
                            form.add(formParameter(urlEncodedParam.name(), formatObject(value, ps[i])));
                        }

                        if (formParam != null || format == null) {
                            ofNullable(formParam)
                                    .map(FormParam::style)
                                    .orElse(FORM)
                                    .getFormParameters(value,
                                            urlEncodedParam.name(),
                                            formParam == null || formParam.explode(),
                                            formParam != null && formParam.allowReserved())
                                    .forEach(p -> {
                                        if (p.isToExpand()) {
                                            form.add(formParameter(urlEncodedParam.name(),
                                                    p.isAllowReserved(),
                                                    p.getValues()));
                                        } else {
                                            form.add(formParameter(urlEncodedParam.name(),
                                                    p.getDelimiter(),
                                                    p.isAllowReserved(),
                                                    p.getValues()));
                                        }
                                    });
                        }
                    }

                    if (form.size() > 0) {
                        return body(form.toArray(new FormParameter[]{}));
                    }
                    return null;
                });
    }

    private static void verifyFileDefinition(DefineFileName d, Parameter param, Method toRead) {
        if (d.useGivenFileName() && isNotBlank(d.fileName())) {
            throw new UnsupportedOperationException(format("Only one of 'useGivenFileName' or 'fileName' " +
                            "(not both) should " +
                            "be defined by '%s' for the parameter '%s' " +
                            "of the method '%s'",
                    DefineFileName.class.getSimpleName(),
                    param.toString(),
                    toRead));
        }
    }

    private static RequestBody<?> getMultipartBody(Method toRead, Object[] parameters) {
        return getFromMethod(toRead,
                MultiPartBody.class,
                parameters,
                (ps, params) -> {
                    var partList = new ArrayList<BodyPart>();

                    for (int i = 0; i < ps.length; i++) {
                        var param = ps[i];
                        var part = param.getAnnotation(MultiPartBody.class);
                        var defineFile = param.getAnnotation(DefineFileName.class);
                        var defineContent = param.getAnnotation(DefineContentType.class);
                        var index = i;

                        ofNullable(params[i])
                                .ifPresentOrElse(o -> {
                                            var bodyValue = formatObject(o, ps[index]);
                                            var cls = bodyValue.getClass();

                                            BodyPart bp;
                                            if (String.class.isAssignableFrom(cls)) {
                                                bp = ofNullable(defineFile)
                                                        .map(d -> {
                                                            var f = d.fileName();
                                                            if (isNotBlank(f)) {
                                                                return bodyPart((String) bodyValue, part.name(), f);
                                                            }
                                                            return bodyPart((String) bodyValue, part.name(), randomAlphanumeric(20));
                                                        })
                                                        .orElseGet(() -> bodyPart((String) bodyValue, part.name()));

                                            } else if (byte[].class.isAssignableFrom(cls)) {
                                                bp = ofNullable(defineFile)
                                                        .map(d -> {
                                                            var f = d.fileName();
                                                            if (isNotBlank(f)) {
                                                                return bodyPart((byte[]) bodyValue, part.name(), f);
                                                            }
                                                            return bodyPart((byte[]) bodyValue, part.name(), randomAlphanumeric(20));
                                                        })
                                                        .orElseGet(() -> bodyPart((byte[]) bodyValue, part.name()));

                                            } else if (InputStream.class.isAssignableFrom(cls)) {
                                                bp = ofNullable(defineFile)
                                                        .map(d -> {
                                                            var f = d.fileName();
                                                            if (isNotBlank(f)) {
                                                                return bodyPart((InputStream) bodyValue, part.name(), f);
                                                            }
                                                            return bodyPart((InputStream) bodyValue, part.name(), randomAlphanumeric(20));
                                                        })
                                                        .orElseGet(() -> bodyPart((InputStream) bodyValue, part.name()));

                                            } else if (DTObject.class.isAssignableFrom(cls)) {
                                                bp = ofNullable(defineFile)
                                                        .map(d -> {
                                                            var f = d.fileName();
                                                            if (isNotBlank(f)) {
                                                                return bodyPart((DTObject) bodyValue, part.name(), f);
                                                            }
                                                            return bodyPart((DTObject) bodyValue, part.name(), randomAlphanumeric(20));
                                                        })
                                                        .orElseGet(() -> bodyPart((DTObject) bodyValue, part.name()));

                                            } else if (File.class.isAssignableFrom(cls)) {
                                                bp = ofNullable(defineFile)
                                                        .map(d -> {
                                                            verifyFileDefinition(d, param, toRead);

                                                            if (d.useGivenFileName()) {
                                                                return bodyPart((File) bodyValue, part.name(), true, nonNull(defineContent));
                                                            }

                                                            if (isNotBlank(d.fileName())) {
                                                                return bodyPart((File) bodyValue, part.name(), d.fileName(), nonNull(defineContent));
                                                            }

                                                            return bodyPart((File) bodyValue, part.name(), randomAlphanumeric(20), nonNull(defineContent));
                                                        })
                                                        .orElseGet(() -> bodyPart((File) bodyValue, part.name(), false, nonNull(defineContent)));

                                            } else if (Path.class.isAssignableFrom(cls)) {
                                                bp = ofNullable(defineFile)
                                                        .map(d -> {
                                                            verifyFileDefinition(d, param, toRead);

                                                            if (d.useGivenFileName()) {
                                                                return bodyPart((Path) bodyValue, part.name(), true, nonNull(defineContent));
                                                            }

                                                            if (isNotBlank(d.fileName())) {
                                                                return bodyPart((Path) bodyValue, part.name(), d.fileName(), nonNull(defineContent));
                                                            }

                                                            return bodyPart((Path) bodyValue, part.name(), randomAlphanumeric(20), nonNull(defineContent));
                                                        })
                                                        .orElseGet(() -> bodyPart((Path) bodyValue, part.name(), false, nonNull(defineContent)));

                                            } else {
                                                bp = ofNullable(defineFile)
                                                        .map(d -> {
                                                            var f = d.fileName();
                                                            if (isNotBlank(f)) {
                                                                return bodyPart(bodyValue, part.name(), f);
                                                            }
                                                            return bodyPart(bodyValue, part.name(), randomAlphanumeric(20));
                                                        })
                                                        .orElseGet(() -> bodyPart(bodyValue, part.name()));
                                            }

                                            if (nonNull(defineContent) && isNotBlank(defineContent.contentType())) {
                                                bp = bp.setContentType(defineContent.contentType());
                                            }

                                            partList.add(bp.setContentTransferEncoding(part.contentTransferEncoding()));
                                        },
                                        () -> {
                                            if (part.isRequired()) {
                                                throw new IllegalArgumentException("%s requires value of the body part %s " +
                                                        "that differs from null");
                                            }
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
                URLEncodedParameter.class,
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
                    URLEncodedParameter.class.getName(),
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
            return URLEncodedParameter.class;
        }

        if (nonNull(multiParts)) {
            return MultiPartBody.class;
        }

        return null;
    }
}
