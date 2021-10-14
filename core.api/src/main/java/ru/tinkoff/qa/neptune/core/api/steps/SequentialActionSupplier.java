package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.AdditionalMetadata;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.valueOf;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure.CaptureOnFailureReader.readCaptorsOnFailure;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess.CaptureOnSuccessReader.readCaptorsOnSuccess;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting.MaxDepthOfReportingReader.getMaxDepth;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultActionParameterReader.getImperativeMetadata;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultActionParameterReader.getPerformOnMetadata;
import static ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter.StepParameterCreator.createStepParameter;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

/**
 * This class is designed to build and to supply actions to be performed on different objects.
 *
 * @param <T>    is the type of input value.
 * @param <R>    is the type of object to perform action on.
 * @param <THIS> is self-type.
 */
@SuppressWarnings("unchecked")
@SequentialActionSupplier.DefinePerformImperativeParameterName
public abstract class SequentialActionSupplier<T, R, THIS extends SequentialActionSupplier<T, R, THIS>> implements Supplier<Action<T>>,
        StepParameterPojo {

    private String actionDescription;
    Object toBePerformedOn;

    protected SequentialActionSupplier() {
    }

    @SuppressWarnings("unused")
    SequentialActionSupplier<T, R, THIS> setDescription(String actionDescription) {
        this.actionDescription = actionDescription;
        return this;
    }

    @Override
    public Map<String, String> getParameters() {
        var cls = (Class<?>) this.getClass();

        var result = new LinkedHashMap<>(StepParameterPojo.super.getParameters());
        ofNullable(getPerformOnMetadata(cls, true))
                .ifPresent(metaData -> {
                    if (isLoggable(toBePerformedOn) && nonNull(toBePerformedOn)) {
                        result.put(translate(metaData), valueOf(toBePerformedOn));
                    }
                });

        if (toBePerformedOn instanceof SequentialGetStepSupplier
                && this.getClass().getAnnotation(IncludeParamsOfInnerGetterStep.class) != null) {
            var get = (SequentialGetStepSupplier<?, ?, ?, ?, ?>) toBePerformedOn;
            get.fillCustomParameters(result);
            get.fillCriteriaParameters(result);
            get.fillTimeParameters(result);
        }

        return result;
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
        toBePerformedOn = supplier;
        return (THIS) this;
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
     * Some additional action on start of the performing step-function
     *
     * @param t is an input value is used to perform the step
     */
    protected void onStart(T t) {
    }

    /**
     * Some additional action if the step is failed
     *
     * @param t         is an input value is used to perform the step
     * @param throwable is a thrown exception/error
     */
    protected void onFailure(T t, Throwable throwable) {
    }

    /**
     * This abstract method describes actions that should be performed on some
     * value.
     *
     * @param value is an object to perform the action on.
     */
    protected abstract void howToPerform(R value);

    /**
     * Returns additional parameters calculated during step execution
     *
     * @return additional parameters calculated during step execution
     */
    protected Map<String, String> additionalParameters() {
        return null;
    }

    @Override
    public Action<T> get() {
        checkArgument(nonNull(toBePerformedOn), "An object should be defined to perform the action on");

        Function<T, R> function;
        if (Function.class.isAssignableFrom(toBePerformedOn.getClass())) {
            function = (Function<T, R>) toBePerformedOn;
        } else if (toBePerformedOn instanceof SequentialGetStepSupplier) {
            function = ((SequentialGetStepSupplier<T, R, ?, ?, ?>) toBePerformedOn).get();
        } else {
            function = t -> (R) toBePerformedOn;
        }

        var description = translate(translate(getImperativeMetadata(this.getClass(), true)) + " " + actionDescription).trim();
        var toBeReturned = new ActionImpl<>(description, this, function);

        if (catchSuccessEvent()) {
            var successCaptors = new ArrayList<Captor<Object, Object>>();
            readCaptorsOnSuccess(this.getClass(), successCaptors);
            toBeReturned
                    .addOnSuccessAdditional(of(FieldValueCaptureMaker.onSuccess(this)))
                    .addSuccessCaptors(successCaptors);
        }

        if (catchFailureEvent()) {
            var failureCaptors = new ArrayList<Captor<Object, Object>>();
            readCaptorsOnFailure(this.getClass(), failureCaptors);
            toBeReturned
                    .addOnFailureAdditional(of(FieldValueCaptureMaker.onFailure(this)))
                    .addFailureCaptors(failureCaptors);
        }

        return toBeReturned.setParameters(getParameters())
                .setMaxDepth(getMaxDepth(this.getClass()))
                .setAdditionalParams(this::additionalParameters);
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
         * Defines name of performOn-parameter
         *
         * @return name of performOn-parameter
         * @see SequentialActionSupplier#performOn(SequentialGetStepSupplier)
         * @see SequentialActionSupplier#performOn(Function)
         * @see SequentialActionSupplier#performOn(Object)
         */
        String value() default "Perform action on";
    }

    public static final class DefaultActionParameterReader {

        private DefaultActionParameterReader() {
            super();
        }

        public static AdditionalMetadata<StepParameter> getPerformOnMetadata(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefinePerformOnParameterName.class, "performOn", useInheritance);
        }

        public static AdditionalMetadata<StepParameter> getImperativeMetadata(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefinePerformImperativeParameterName.class, "imperative", useInheritance);
        }

        private static AdditionalMetadata<StepParameter> readAnnotation(Class<?> toRead, Class<? extends Annotation> annotationClass,
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
                        return new AdditionalMetadata<>(cls, name, StepParameter.class, () -> {
                            try {
                                return createStepParameter((String) valueMethod.invoke(annotation));
                            } catch (Exception t) {
                                throw new RuntimeException(t);
                            }
                        });
                    } catch (NoSuchMethodException e) {
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
