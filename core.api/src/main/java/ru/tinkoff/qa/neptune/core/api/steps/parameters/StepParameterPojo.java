package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter.ParameterValueReader.getParameterForStep;

/**
 * A POJO that wraps parameters of steps
 */
public interface StepParameterPojo {

    /**
     * Transforms an object to a map of step parameters and their values. It uses fields of type
     * that extends {@link StepParameterPojo} or fields annotated by {@link StepParameter}
     *
     * @return a map where key is a name of a parameter and value is a value of a parameter
     */
    default Map<String, String> getParameters() {
        var result = new LinkedHashMap<String, String>();
        Class<?> cls = this.getClass();
        while (!cls.equals(Object.class)) {
            stream(cls.getDeclaredFields())
                    .filter(field -> (field.getAnnotation(StepParameter.class) != null
                            || StepParameterPojo.class.isAssignableFrom(field.getType()))
                            && !isStatic(field.getModifiers()))
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            var value = field.get(this);
                            var param = field.getAnnotation(StepParameter.class);

                            if (param != null) {
                                ofNullable(value).ifPresentOrElse(
                                        o -> result.put(translate(field), getParameterForStep(value, param.makeReadableBy())),
                                        () -> {
                                            if (!param.doNotReportNullValues()) {
                                                result.put(translate(field), valueOf((Object) null));
                                            }
                                        });
                            } else {
                                if (value != null && StepParameterPojo.class.isAssignableFrom(field.getType())) {
                                    result.putAll(((StepParameterPojo) value).getParameters());
                                }
                            }

                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
            cls = cls.getSuperclass();
        }
        return result;
    }
}
