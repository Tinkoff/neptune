package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.valueOf;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure.CaptureOnFailureReader.readCaptorsOnFailure;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess.CaptureOnSuccessReader.readCaptorsOnSuccess;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultActionParameterReader.getImperativePseudoField;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultActionParameterReader.getPerformOnPseudoField;
import static ru.tinkoff.qa.neptune.core.api.steps.StepAction.action;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

/**
 * This class is designed to build actions to be performed on different objects.
 * Also it may be used to build chains of same actions on different objects.
 *
 * @param <T>    is the type of an input value.
 * @param <R>    is the type of an object to perform action on.
 * @param <THIS> is self-type.
 */
@SuppressWarnings("unchecked")
@SequentialActionSupplier.DefinePerformImperativeParameterName
public abstract class SequentialActionSupplier<T, R, THIS extends SequentialActionSupplier<T, R, THIS>> implements Supplier<StepAction<T>>,
        StepParameterPojo {

    private String actionDescription;
    private final List<Captor<Object, Object>> successCaptors = new ArrayList<>();
    private final List<Captor<Object, Object>> failureCaptors = new ArrayList<>();

    Object toBePerformedOn;

    protected SequentialActionSupplier() {
        readCaptorsOnFailure(this.getClass(), failureCaptors);
        readCaptorsOnSuccess(this.getClass(), successCaptors);
    }

    protected SequentialActionSupplier<T, R, THIS> setDescription(String actionDescription) {
        this.actionDescription = actionDescription;
        return this;
    }

    @Override
    public Map<String, String> getParameters() {
        var cls = (Class<?>) this.getClass();

        var result = new LinkedHashMap<String, String>();
        ofNullable(getPerformOnPseudoField(cls, true))
                .ifPresent(pseudoField -> {
                    if (isLoggable(toBePerformedOn) && nonNull(toBePerformedOn)) {
                        result.put(translate(pseudoField), valueOf(toBePerformedOn));
                    }
                });

        result.putAll(StepParameterPojo.super.getParameters());
        return result;
    }

    private StepAction<T> performOnPrivate(Object functionOrObject) {
        Function<T, ? extends R> function;
        if (nonNull(functionOrObject) && Function.class.isAssignableFrom(functionOrObject.getClass())) {
            function = (Function<T, ? extends R>) functionOrObject;
        } else {
            function = null;
        }

        var description = (translate(getImperativePseudoField(this.getClass(), true)) + " " + actionDescription).trim();
        var action = ofNullable(function).map(function1 ->
                action(description, (Consumer<T>) t -> {
                    R r = function1.apply(t);
                    performActionOn(r);
                }))
                .orElseGet(() -> action(description, t ->
                        performActionOn((R) functionOrObject)));

        return action
                .addSuccessCaptors(successCaptors)
                .addFailureCaptors(failureCaptors)
                .setParameters(getParameters());
    }

    /**
     * This method is designed to define a value/object to perform the action on. The action is
     * supposed to be performed on an object that returned as a result of the function applying.
     * The method is designed to be overridden or overloaded/used by custom method when it is necessary.
     *
     * @param function that returns a target object on the applying
     * @return self-reference.
     */
    protected THIS performOn(Function<T, ? extends R> function) {
        checkArgument(nonNull(function), "Function that gets value to perform action is not defined");
        toBePerformedOn = function;
        return (THIS) this;
    }

    /**
     * This method is designed to define a value/object to perform the action on. The action is
     * supposed to be performed on an object that returned as a result of the function applying.
     * This function is built and supplied by {@link SequentialGetStepSupplier}
     * The method is designed to be overridden or overloaded/used by custom method when it is necessary.
     *
     * @param supplier that supplies a function. This function returns a target object on the applying.
     * @return self-reference.
     */
    protected THIS performOn(SequentialGetStepSupplier<T, ? extends R, ?, ?, ?> supplier) {
        checkArgument(nonNull(supplier), "Supplier of a function that gets value " +
                "to perform action is not defined");
        return performOn(supplier.get());
    }

    /**
     * This method is designed to define a value/object to perform the action on.The action is
     * supposed to be performed on a {@code value}. The method is designed to be overridden or
     * overloaded/used by custom method when it is necessary.
     *
     * @param value is a target object to perform the action on
     * @return self-reference.
     */
    protected THIS performOn(R value) {
        toBePerformedOn = value;
        return (THIS) this;
    }

    /**
     * This abstract method describes actions that should be performed on some
     * value.
     *
     * @param value is an object to perform the action on.
     */
    protected abstract void performActionOn(R value);


    @Override
    public StepAction<T> get() {
        checkArgument(nonNull(toBePerformedOn), "An object should be defined to perform the action on");
        return performOnPrivate(toBePerformedOn);
    }

    @Override
    public String toString() {
        return actionDescription;
    }

    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefinePerformImperativeParameterName {
        /**
         * Defines name of imperative of a step
         *
         * @return imperative of a step
         */
        String value() default "Perform:";
    }

    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefinePerformOnParameterName {
        /**
         * Defines name of imperative of a step
         *
         * @return imperative of a step
         */
        String value() default "Perform action on";
    }

    public static final class DefaultActionParameterReader {

        private DefaultActionParameterReader() {
            super();
        }

        public static PseudoField getPerformOnPseudoField(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefinePerformOnParameterName.class, "performOn", useInheritance);
        }

        public static PseudoField getImperativePseudoField(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefinePerformImperativeParameterName.class, "imperative", useInheritance);
        }

        private static PseudoField readAnnotation(Class<?> toRead, Class<? extends Annotation> annotationClass,
                                                  String name, boolean useInheritance) {
            if (!SequentialActionSupplier.class.isAssignableFrom(toRead)) {
                return null;
            }

            var cls = toRead;
            while (!cls.equals(Object.class)) {
                var annotation = cls.getAnnotation(annotationClass);
                if (annotation != null) {
                    try {
                        var valueMethod = annotation.annotationType().getMethod("value");
                        valueMethod.setAccessible(true);
                        return new PseudoField(toRead, name, (String) valueMethod.invoke(annotation));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (useInheritance) {
                    cls = cls.getSuperclass();
                } else {
                    return null;
                }
            }

            return null;
        }
    }
}
