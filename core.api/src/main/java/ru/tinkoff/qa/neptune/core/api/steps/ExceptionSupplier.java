package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.function.Supplier;

import static java.util.Arrays.stream;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefaultGetParameterReader.getExceptionMessageStartMetadata;

public class ExceptionSupplier implements Supplier<RuntimeException> {

    final SequentialGetStepSupplier<?, ?, ?, ?, ?> stepThatCanBeFailed;
    private Throwable cause;

    ExceptionSupplier(SequentialGetStepSupplier<?, ?, ?, ?, ?> stepThatCanBeFailed) {
        this.stepThatCanBeFailed = stepThatCanBeFailed;
    }

    @Override
    public RuntimeException get() {
        try {
            var toThrow = getExceptionMessageStartMetadata(
                    stepThatCanBeFailed.getClass(),
                    true);

            var msg = getExceptionMessage(translate(toThrow));

            var throwableClass = toThrow
                    .getDeclaringClass()
                    .getAnnotation(ThrowWhenNoData.class)
                    .toThrow();

            var constructors = throwableClass
                    .getDeclaredConstructors();

            var msgConstructor = getConstructorWithOnlyMessage(constructors);
            var rootCauseConstructor = getConstructorWithMessageAndRootCause(constructors);

            if (msgConstructor == null && rootCauseConstructor == null) {
                throw new NoSuchMethodException("There is no constructor that can take String or String and Throwable " +
                        "as parameters. " + throwableClass.getName());
            }

            if (cause != null && rootCauseConstructor != null) {
                rootCauseConstructor.setAccessible(true);
                if (rootCauseConstructor.getParameters()[0].getType().equals(String.class)) {
                    return (RuntimeException) rootCauseConstructor.newInstance(msg, cause);
                } else {
                    return (RuntimeException) rootCauseConstructor.newInstance(cause, msg);
                }
            }

            if (msgConstructor == null) {
                throw new NoSuchMethodException("There is no constructor that can take String as a parameter. "
                        + throwableClass.getName());
            }

            msgConstructor.setAccessible(true);
            return (RuntimeException) msgConstructor.newInstance(msg);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> getConstructorWithOnlyMessage(Constructor<?>[] constructors) {
        return stream(constructors)
                .filter(c -> {
                    var p = c.getParameters();
                    return p.length == 1 && p[0].getType().equals(String.class);
                })
                .findFirst()
                .orElse(null);
    }

    private Constructor<?> getConstructorWithMessageAndRootCause(Constructor<?>[] constructors) {
        return stream(constructors)
                .filter(c -> {
                    var p = c.getParameters();
                    return p.length == 2 && stream(p)
                            .map(Parameter::getType)
                            .collect(toList())
                            .containsAll(of(String.class, Throwable.class));

                })
                .findFirst()
                .orElse(null);
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    String getExceptionMessage(String messageStarting) {
        var stringBuilder = new StringBuilder(messageStarting)
                .append(SPACE)
                .append(stepThatCanBeFailed.getDescription());
        stepThatCanBeFailed.getParameters().forEach((key, value) -> stringBuilder.append("\r\n")
                .append("- ")
                .append(key)
                .append(":")
                .append(value));
        return stringBuilder.toString();
    }
}
