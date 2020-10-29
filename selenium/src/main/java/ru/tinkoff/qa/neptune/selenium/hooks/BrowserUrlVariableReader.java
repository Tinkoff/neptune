package ru.tinkoff.qa.neptune.selenium.hooks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.*;
import static ru.tinkoff.qa.neptune.core.api.utils.URLEncodeUtil.encodePathSubstring;
import static ru.tinkoff.qa.neptune.core.api.utils.URLEncodeUtil.encodeQuerySubstring;

/**
 * Util class that reads metadata of some class and field values of an objects and returns URL to navigate to
 */
final class BrowserUrlVariableReader {

    private static List<String> getVariables(String segment) {
        return ofNullable(substringsBetween(segment, "{", "}"))
                .map(strings -> stream(strings)
                        .distinct()
                        .collect(toList()))
                .orElseGet(List::of);
    }

    /**
     * Reads metadata of some class and field values of an objects and returns URL to navigate to.
     *
     * @param o        is an object to read URL-variables from
     * @param rawRoot  is as string value of a root URL where should be inserted values of URL-variables
     * @param rawPath  is as string value of a path of an URL where should be inserted values of URL-variables
     * @param rawQuery is as string value of a query of an URL where should be inserted values of URL-variables
     * @return a string value with inserted values of URL-variables
     */
    static String pageToNavigate(Object o, String rawRoot, String rawPath, String rawQuery) {
        List<String> patternParams = new ArrayList<>();
        patternParams.addAll(getVariables(rawRoot));
        patternParams.addAll(getVariables(rawPath));
        patternParams.addAll(getVariables(rawQuery));

        patternParams = patternParams.stream()
                .distinct()
                .collect(toList());

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
                                                "field of %s with superclasses. Given root URL with variables: [%s]. " +
                                                "Given path value with variables: [%s]. " +
                                                "Given query value with variables: [%s].",
                                        p,
                                        cls.getName(),
                                        rawRoot,
                                        rawPath,
                                        rawQuery));
                            });
        }

        var resultRoot = rawRoot;
        var resultPath = rawPath;
        var resultQuery = rawQuery;

        for (var v : browserUrlVars) {
            resultRoot = v.replace(resultRoot, Styles.ROOT_URL);
            resultPath = v.replace(resultPath, Styles.PATH);
            resultQuery = v.replace(resultQuery, Styles.QUERY);
        }

        var result = resultRoot;
        if (isNotBlank(resultPath)) {
            result = result + (resultPath.startsWith("/") ? resultPath : "/" + resultPath);
        }
        if (isNotBlank(resultQuery)) {
            result = result + (resultQuery.startsWith("?") ? resultQuery : "?" + resultQuery);
        }
        return result;
    }

    private enum Styles {
        ROOT_URL(false) {
            @Override
            String encode(String toEncode) {
                return toEncode;
            }
        },
        PATH(true) {
            @Override
            String encode(String toEncode) {
                return encodePathSubstring(toEncode);
            }
        },
        QUERY(null) {
            @Override
            String encode(String toEncode) {
                return encodeQuerySubstring(toEncode, false);
            }
        };


        private final Boolean toEncode;

        Styles(Boolean toEncode) {
            this.toEncode = toEncode;
        }

        private Boolean getToEncode() {
            return toEncode;
        }

        abstract String encode(String toEncode);
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

        private String replace(String rawURL, Styles style) {
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

                var toEncode = ofNullable(style.getToEncode())
                        .orElse(this.toEncode);

                var variableValue = toEncode ? style.encode(valueOf(value)) : valueOf(value);
                return rawURL.replace("{" + name + "}", variableValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
