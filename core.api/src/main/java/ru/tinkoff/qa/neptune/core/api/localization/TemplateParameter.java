package ru.tinkoff.qa.neptune.core.api.localization;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.add;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter.ParameterValueReader.getParameterForStep;

public final class TemplateParameter {

    private final String name;
    private final String value;

    private TemplateParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private static List<TemplateParameter> getTemplateParameters(String template, AnnotatedElement[] elements, Object[] values) {
        var result = new LinkedList<TemplateParameter>();
        int i = 0;
        for (var e : elements) {
            var fragment = e.getAnnotation(DescriptionFragment.class);
            if (nonNull(fragment)) {
                result.add(new TemplateParameter(fragment.value(), getParameterForStep(values[i], fragment.makeReadableBy())));
                i++;
                continue;
            }

            if (e instanceof Parameter) {
                if (template.contains("{" + i + "}")) {
                    result.add(new TemplateParameter(valueOf(i), valueOf(values[i])));
                }
            }
            i++;
        }

        return result;
    }

    static List<TemplateParameter> getTemplateParameters(String template, Method m, Object[] invocationParameters) {
        return getTemplateParameters(template, m.getParameters(), invocationParameters);
    }

    static List<TemplateParameter> getTemplateParameters(String template, Object o) {
        var clz = o.getClass();
        var fields = new AnnotatedElement[]{};
        var values = new Object[]{};

        var requiredFields = new LinkedList<Field>();

        while (!clz.equals(Object.class)) {
            requiredFields.addAll(stream(clz.getDeclaredFields())
                    .filter(field -> !isStatic(field.getModifiers()) && field.getAnnotation(DescriptionFragment.class) != null)
                    .collect(toList()));

            clz = clz.getSuperclass();
        }

        for (var f : requiredFields) {
            f.setAccessible(true);
            try {
                var value = f.get(o);
                fields = add(fields, f);
                values = add(values, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return getTemplateParameters(template, fields, values);
    }

    public static String buildTextByTemplate(String template, List<TemplateParameter> parameters) {
        var strContainer = new Object() {
            String contained;

            public String getContained() {
                return contained;
            }

            public void setContained(String s) {
                this.contained = s;
            }
        };

        strContainer.setContained(template);
        parameters.forEach(p -> {
            var s = strContainer.getContained();
            s = s.replace("{" + p.getName() + "}", p.getValue());
            strContainer.setContained(s);
        });

        return strContainer.getContained();
    }

    /**
     * @return name of template parameter
     */
    public String getName() {
        return name;
    }

    /**
     * @return value of template parameter
     */
    public String getValue() {
        return value;
    }
}
