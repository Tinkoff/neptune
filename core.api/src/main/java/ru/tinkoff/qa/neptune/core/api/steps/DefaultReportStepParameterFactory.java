package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;
import static ru.tinkoff.qa.neptune.core.api.steps.StepParameter.ParameterValueReader.getParameterForStep;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

/**
 * This is util class that forms step parameters for logs and reporting tools
 */
public final class DefaultReportStepParameterFactory {

    private DefaultReportStepParameterFactory() {
        super();
    }

    private static Map<String, String> readParameters(Object toRead, Class<?> rootClassToStop) {
        var result = new LinkedHashMap<String, String>();
        Class<?> cls = toRead.getClass();
        while (!cls.equals(rootClassToStop)) {
            stream(cls.getDeclaredFields())
                    .filter(field -> field.getAnnotation(StepParameter.class) != null && !isStatic(field.getModifiers()))
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            var value = field.get(toRead);
                            var param = field.getAnnotation(StepParameter.class);
                            ofNullable(value).ifPresentOrElse(
                                    o -> result.put(param.value(), getParameterForStep(value, param)),
                                    () -> {
                                        if (!param.doNotReportNullValues()) {
                                            result.put(param.value(), valueOf((Object) null));
                                        }
                                    });
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
            cls = cls.getSuperclass();
        }
        return result;
    }

    public static Map<String, String> getParameters(SequentialGetStepSupplier<?, ?, ?, ?, ?> supplier) {
        var cls = (Class<?>) supplier.getClass();
        var defaultParameters = ofNullable(cls.getAnnotation(SequentialGetStepSupplier.DefaultParameterNames.class))
                .orElseGet(() -> {
                    SequentialGetStepSupplier.DefaultParameterNames result = null;
                    var clazz = cls;
                    while (result == null) {
                        clazz = clazz.getSuperclass();
                        result = clazz.getAnnotation(SequentialGetStepSupplier.DefaultParameterNames.class);
                    }
                    return result;
                });

        var result = new LinkedHashMap<>(readParameters(supplier, SequentialGetStepSupplier.class));
        int i = 0;
        for (var c : supplier.conditions) {
            var name = i == 0 ? defaultParameters.criteria() : defaultParameters.criteria() + " " + (i + 1);
            result.put(name, c.toString());
            i++;
        }

        ofNullable(supplier.timeToGet).ifPresent(duration -> {
            if (duration.toMillis() > 0) {
                result.put(defaultParameters.timeOut(), formatDurationHMS(duration.toMillis()));
            }
        });
        ofNullable(supplier.sleepingTime).ifPresent(duration -> {
            if (duration.toMillis() > 0) {
                result.put(defaultParameters.pollingTime(), formatDurationHMS(duration.toMillis()));
            }
        });

        if (isLoggable(supplier.from)) {
            var fromCls = supplier.from.getClass();
            if (Function.class.isAssignableFrom(fromCls) || SequentialGetStepSupplier.class.isAssignableFrom(fromCls)) {
                result.put(defaultParameters.from(), supplier.from + " (is calculated while the step is executed)");
            } else {
                result.put(defaultParameters.from(), valueOf(supplier.from));
            }
        }

        return result;
    }

    public static Map<String, String> getParameters(SequentialActionSupplier<?, ?, ?> supplier) {
        var cls = (Class<?>) supplier.getClass();
        var defaultParameters = ofNullable(cls.getAnnotation(SequentialActionSupplier.DefaultParameterNames.class))
                .orElseGet(() -> {
                    SequentialActionSupplier.DefaultParameterNames result = null;
                    var clazz = cls;
                    while (result == null) {
                        clazz = clazz.getSuperclass();
                        result = clazz.getAnnotation(SequentialActionSupplier.DefaultParameterNames.class);
                    }
                    return result;
                });

        var result = new LinkedHashMap<String, String>();
        if (isLoggable(supplier.toBePerformedOn)) {
            var fromCls = supplier.toBePerformedOn.getClass();
            if (Function.class.isAssignableFrom(fromCls) || SequentialGetStepSupplier.class.isAssignableFrom(fromCls)) {
                result.put(defaultParameters.performOn(), supplier.toBePerformedOn + " (is calculated while the step is executed)");
            } else {
                result.put(defaultParameters.performOn(), valueOf(supplier.toBePerformedOn));
            }
        }
        result.putAll(readParameters(supplier, SequentialActionSupplier.class));
        return result;
    }
}
