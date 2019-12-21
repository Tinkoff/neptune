package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.annotations.Beta;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.StepAction.action;

/**
 * This class is designed to build actions to be performed on different objects.
 * Also it may be used to build chains of same actions on different objects.
 *
 * @param <T> is the type of an input value.
 * @param <R> is the type of an object to perform action on.
 * @param <THIS> is self-type.
 */
@SuppressWarnings("unchecked")
public abstract class SequentialActionSupplier<T, R, THIS extends SequentialActionSupplier<T, R, THIS>> implements Supplier<Consumer<T>>,
        MakesCapturesOnFinishing<THIS> {

    private final String actionDescription;
    private final List<CaptorFilterByProducedType> captorFilters = new ArrayList<>();
    private List<Object> toBePerformedOn = new ArrayList<>();
    private List<THIS> mergeFrom = new ArrayList<>();

    protected SequentialActionSupplier(String description) {
        checkArgument(!isBlank(description), "Description of the action should not be blank or null string value");
        this.actionDescription = description;
        MakesCapturesOnFinishing.makeCaptureSettings(this);
    }

    private Consumer<T> performOnPrivate(Object functionOrObject) {
        Function<T, ? extends R> function;
        if (nonNull(functionOrObject) && Function.class.isAssignableFrom(functionOrObject.getClass())) {
            function = (Function) functionOrObject;
        }
        else {
            function = null;
        }

        var action = (StepAction) ofNullable(function).map(function1 ->
                action(actionDescription, (Consumer<T>) t -> {
                    R r = function1.apply(t);
                    performActionOn(r);
                }))
                .orElseGet(() -> action(actionDescription, t ->
                        performActionOn((R) functionOrObject)));
        action.addCaptorFilters(captorFilters);
        return action;
    }

    /**
     * This is the helping method that is designed to add the action to the sequiance of actions. A new one action is
     * supposed to be performed on an object returned by the defined function. The method is supposed to be overridden
     * or overloaded/used by custom method.
     *
     * @param function that gets a target object
     * @return self-reference.
     */
    protected THIS performOn(Function<T, ? extends R> function) {
        checkArgument(nonNull(function), "Function that gets value to perform action is not defined");
        toBePerformedOn.add(function);
        return (THIS) this;
    }

    /**
     * This is the helping method that is designed to add the action to the sequiance of actions. A new one action is
     * supposed to be performed on an object returned by the defined function. The method is supposed to be overridden
     * or overloaded/used by custom method.
     *
     * @param supplier that supplies a function to get a target object
     * @return self-reference.
     */
    protected THIS performOn(SequentialGetStepSupplier<T, ? extends R, ?, ?, ?> supplier) {
        checkArgument(nonNull(supplier), "Supplier of a function that gets value " +
                "to perform action is not defined");
        return performOn(supplier.get());
    }

    /**
     * This is the helping method that is designed to add the action to the sequiance of actions. A new one action is
     * supposed to be performed on the defined object. The method is supposed to be overridden or overloaded/used by
     * custom method.
     *
     * @param value is a target object to perform the action on
     * @return self-reference.
     */
    protected THIS performOn(R value) {
        toBePerformedOn.add(value);
        return (THIS) this;
    }

    /**
     * This abstract method describes actions that should be performed on some
     * value.
     *
     * @param value is an object to perform the action on.
     */
    protected abstract void performActionOn(R value);


    /**
     * Adds built actions from another {@link SequentialActionSupplier} of the same type as the instance that invokes
     * the method.
     *
     * @param mergeFrom is the instance of {@link SequentialActionSupplier} that has got built actions to be added to the
     *                  current sequence of actions
     * @return self-reference.
     */
    @Beta
    protected THIS mergeActionSequenceFrom(THIS mergeFrom) {
        checkArgument(nonNull(mergeFrom), "Action builder should be defined");
        this.mergeFrom.add(mergeFrom);
        return (THIS) this;
    }

    @Override
    public Consumer<T> get() {
        checkArgument(toBePerformedOn.size() > 0, "At least one object should be defined to perform the action");
        Consumer<T> action = null;
        for (Object o : toBePerformedOn) {
            action = ofNullable(action).map(tConsumer -> tConsumer.andThen(performOnPrivate(o))).orElse(performOnPrivate(o));
        }

        for (THIS t : mergeFrom) {
            action = action.andThen(t.get());
        }

        return action;
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
    public THIS onFinishMakeCaptureOfType(Class typeOfCapture) {
        captorFilters.add(new CaptorFilterByProducedType(typeOfCapture));
        return (THIS) this;
    }
}
