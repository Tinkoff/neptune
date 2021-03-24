package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakesCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
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
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultParameterNames.DefaultActionParameterReader.getImperative;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultParameterNames.DefaultActionParameterReader.getPerformOnPseudoField;
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
@SequentialActionSupplier.DefaultParameterNames
public abstract class SequentialActionSupplier<T, R, THIS extends SequentialActionSupplier<T, R, THIS>> implements Supplier<StepAction<T>>,
        MakesCapturesOnFinishing<THIS>, StepParameterPojo {

    private String actionDescription;
    private final List<CaptorFilterByProducedType> captorFilters = new ArrayList<>();

    Object toBePerformedOn;

    protected SequentialActionSupplier() {
        MakesCapturesOnFinishing.makeCaptureSettings(this);
    }

    protected SequentialActionSupplier<T, R, THIS> setDescription(String actionDescription) {
        this.actionDescription = actionDescription;
        return this;
    }

    @Override
    public Map<String, String> getParameters() {
        var cls = (Class<?>) this.getClass();

        var result = new LinkedHashMap<String, String>();
        if (isLoggable(toBePerformedOn) && nonNull(toBePerformedOn)) {
            result.put(translate(getPerformOnPseudoField(cls)), valueOf(toBePerformedOn));
        }

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

        var description = translate(getImperative(this.getClass())) + " " + actionDescription;
        var action = ofNullable(function).map(function1 ->
                action(description, (Consumer<T>) t -> {
                    R r = function1.apply(t);
                    performActionOn(r);
                }))
                .orElseGet(() -> action(description, t ->
                        performActionOn((R) functionOrObject)));

        action.addCaptorFilters(captorFilters);
        return action.setParameters(getParameters());
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

    /**
     * Marks that it is needed to produce a {@link java.awt.image.BufferedImage} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} on built resulted {@link java.util.function.Consumer}.
     * This image is produced by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This image is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.awt.image.BufferedImage}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values {@code T}
     * on success/on failure.
     *
     * @return self-reference
     */
    @Override
    public THIS makeImageCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(BufferedImage.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce a {@link java.io.File} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} on built resulted {@link java.util.function.Consumer}.
     * This file is produced by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This file is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.io.File}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values {@code T}
     * on success/on failure.
     *
     * @return self-reference
     */
    @Override
    public THIS makeFileCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(File.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce a {@link java.lang.StringBuilder} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} on built resulted {@link java.util.function.Consumer}.
     * This string builder is produced by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This string builder is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.lang.StringBuilder}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values {@code T}
     * on success/on failure.
     *
     * @return self-reference
     */
    @Override
    public THIS makeStringCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(StringBuilder.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce some value after invocation of
     * {@link java.util.function.Consumer#accept(Object)} on built resulted {@link java.util.function.Consumer}.
     * This value is produced by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This value is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor}
     * that may produce something of type defined by {@param typeOfCapture}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values {@code T}
     * on success/on failure.
     *
     * @param typeOfCapture is a type of a value to produce by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     * @return self-reference
     */
    @Override
    public THIS onFinishMakeCaptureOfType(Class<?> typeOfCapture) {
        captorFilters.add(new CaptorFilterByProducedType(typeOfCapture));
        return (THIS) this;
    }

    /**
     * This annotation is designed to mark subclasses of {@link SequentialActionSupplier}. It is
     * used for the reading of values to perform an action on and for the forming of parameters
     * of a resulted step-action.
     *
     * @see SequentialActionSupplier#performOn(Object)
     * @see SequentialActionSupplier#performOn(Function)
     * @see SequentialActionSupplier#performOn(SequentialGetStepSupplier)
     */
    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefaultParameterNames {

        /**
         * Defines name of imperative of a step
         *
         * @return imperative of a step
         */
        String imperative() default "Perform:";

        /**
         * Defines name of the perform on-parameter
         *
         * @return Defined name of the perform on-parameter
         * @see SequentialActionSupplier#performOn(Object)
         * @see SequentialActionSupplier#performOn(Function)
         * @see SequentialActionSupplier#performOn(SequentialGetStepSupplier)
         */
        String performOn() default "Perform action on";

        final class DefaultActionParameterReader {

            private DefaultActionParameterReader() {
                super();
            }

            public static PseudoField getPerformOnPseudoField(Class<?> toRead) {
                if (!SequentialActionSupplier.class.isAssignableFrom(toRead)) {
                    return null;
                }
                return new PseudoField(toRead, "performOn", getDefaultParameters(toRead).performOn());
            }

            public static PseudoField getImperative(Class<?> toRead) {
                if (!SequentialActionSupplier.class.isAssignableFrom(toRead)) {
                    return null;
                }
                return new PseudoField(toRead, "imperative", getDefaultParameters(toRead).imperative());
            }

            private static DefaultParameterNames getDefaultParameters(Class<?> toRead) {
                return ofNullable(toRead.getAnnotation(DefaultParameterNames.class))
                        .orElseGet(() -> {
                            DefaultParameterNames parameterNames = null;
                            var clazz = toRead;
                            while (parameterNames == null) {
                                clazz = clazz.getSuperclass();
                                parameterNames = clazz.getAnnotation(DefaultParameterNames.class);
                            }
                            return parameterNames;
                        });
            }
        }
    }
}
