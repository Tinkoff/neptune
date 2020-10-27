package ru.tinkoff.qa.neptune.selenium.hooks;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substringsBetween;

/**
 * Util class that reads metadata of some class and field values of an objects and returns URL to navigate to
 */
final class BrowserUrlVariableReader {

    /**
     * Reads metadata of some class and field values of an objects and returns URL to navigate to.
     *
     * @param o      is an object to read URL-variables from
     * @param rawUrl is as string value of an URL where should be inserted values of URL-variables
     * @return a string value with inserted values of URL-variables
     */
    static String pageToNavigate(Object o, String rawUrl) {
        var patternParams = ofNullable(substringsBetween(rawUrl, "{", "}"))
                .map(strings -> stream(strings)
                        .distinct()
                        .collect(toList()))
                .orElse(of());

        var cls = o.getClass();
        var clz = cls;
        var fields = new ArrayList<Field>();

        while (!clz.equals(Object.class)) {
            fields.addAll(stream(clz.getDeclaredFields())
                    .filter(field -> field.getAnnotationsByType(BrowserUrlVariable.class).length > 0)
                    .collect(toList()));
            clz = clz.getSuperclass();
        }

        var browserUrlVars = new ArrayList<BrowserUrlVar>();

        for (var p : patternParams) {
            fields.stream()
                    .filter(field -> {
                        var variables = field.getAnnotationsByType(BrowserUrlVariable.class);
                        return stream(variables).anyMatch(browserUrlVariable -> browserUrlVariable.name().equals(p)
                                || (isBlank(browserUrlVariable.name()) && field.getName().equals(p)));
                    })
                    .findFirst()
                    .ifPresentOrElse(field -> {
                                field.setAccessible(true);
                                try {
                                    var variables = field.getAnnotationsByType(BrowserUrlVariable.class);
                                    var variable = stream(variables)
                                            .filter(browserUrlVariable -> browserUrlVariable.name().equals(p)
                                                    || (isBlank(browserUrlVariable.name()) && field.getName().equals(p)))
                                            .findFirst()
                                            .orElseThrow(RuntimeException::new);

                                    if (isBlank(variable.name())) {
                                        browserUrlVars.add(new BrowserUrlVar(p,
                                                field,
                                                isStatic(field.getModifiers()) ? field.getDeclaringClass() : o,
                                                variable.toEncode()));
                                    } else {
                                        if (isBlank(variable.field())) {
                                            browserUrlVars.add(new BrowserUrlVar(p,
                                                    field,
                                                    isStatic(field.getModifiers()) ? field.getDeclaringClass() : o,
                                                    variable.toEncode()));
                                        } else {
                                            browserUrlVars.add(new BrowserUrlVar(p,
                                                    field.getType().getDeclaredField(variable.field()),
                                                    isStatic(field.getModifiers()) ? field.get(field.getDeclaringClass()) : field.get(o),
                                                    variable.toEncode()));
                                        }
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            () -> {
                                throw new UnsupportedOperationException(format("URL-variable %s is not mapped by any " +
                                                "field of %s with superclasses. Given URL value with variables: %s",
                                        p,
                                        cls.getName(),
                                        rawUrl));
                            });
        }

        var result = rawUrl;

        for (var v : browserUrlVars) {
            result = v.replace(result);
        }

        return result;
    }

    private static class BrowserUrlVar {
        private final String name;
        private final Field get;
        private final Object from;
        private final boolean toEncode;

        private BrowserUrlVar(String name, Field get, Object from, boolean toEncode) {
            this.name = name;
            this.get = get;
            this.from = from;
            this.toEncode = toEncode;
        }

        private String replace(String rawURL) {
            if (from == null) {
                throw new UnsupportedOperationException(format("Null values can't be used " +
                                "for the getting of values of URL-variables. " +
                                "Variable: %s. Field: %s. Raw URL with not replaced variable: %s",
                        name,
                        get,
                        rawURL));
            }

            get.setAccessible(true);
            try {
                var value = get.get(from);
                if (value == null) {
                    throw new UnsupportedOperationException(format("Null values can't be used " +
                                    "for the getting of values of URL-variables. " +
                                    "Variable: %s. Field: %s. Raw URL with not replaced variable: %s",
                            name,
                            get,
                            rawURL));
                }

                var variableValue = toEncode ? encode(valueOf(value), UTF_8) : valueOf(value);
                return rawURL.replace("{" + name + "}", variableValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
