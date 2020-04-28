package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.lang.System.lineSeparator;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.tinkoff.qa.neptune.core.api.steps.StepParameter.ParameterValueReader.getParameterForStep;

final class DefaultReportStepParameterFactory {

    private static final String LINE_SEPARATOR = lineSeparator();

    private DefaultReportStepParameterFactory() {
        super();
    }

    static <Q> String fromParameterBySupplier(SequentialGetStepSupplier<?, ?, ?, Q, ?> from) {
        var strBuilder = new StringBuilder();

        var supplier2 = (SequentialGetStepSupplier<?, ?, ?, Q, ?>) from;
        var params = supplier2.formCriteriaReportParams(supplier2.conditions);
        ofNullable(from.timeToGet).ifPresent(duration -> params.putAll(from.formTimeoutForReport(duration)));
        ofNullable(from.sleepingTime).ifPresent(duration -> params.putAll(from.formSleepTimeForReport(duration)));
        params.putAll(supplier2.formParameters());

        strBuilder.append(supplier2.toString());
        if (params.size() > 0) {
            strBuilder.append(". Parameters:");
            params.forEach((s, s2) -> strBuilder
                    .append(LINE_SEPARATOR)
                    .append(SPACE)
                    .append(SPACE)
                    .append(SPACE)
                    .append(SPACE)
                    .append(SPACE)
                    .append(s)
                    .append(" : ")
                    .append(s2));
        }

        return strBuilder.toString();
    }

    static String fromParameterByStepFunction(StepFunction<?, ?> from) {
        var strBuilder = new StringBuilder();
        var cls = from.getClass();

        Map<String, String> params;
        if (StepFunction.SequentialStepFunction.class.isAssignableFrom(cls)) {
            params = ((StepFunction<?, ?>) ((StepFunction.SequentialStepFunction<?, ?>) from).sequence.getLast()).getParameters();
        } else {
            params = from.getParameters();
        }

        strBuilder.append(from.toString());
        if (params.size() > 0) {
            strBuilder.append(". Parameters:");
            params.forEach((s, s2) -> strBuilder
                    .append(LINE_SEPARATOR)
                    .append(SPACE)
                    .append(SPACE)
                    .append(SPACE)
                    .append(SPACE)
                    .append(SPACE)
                    .append(s)
                    .append(" : ")
                    .append(s2));
        }

        return strBuilder.toString();
    }

    static Map<String, String> readParameters(Object toRead, Class<?> rootClassToStop) {
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
}
