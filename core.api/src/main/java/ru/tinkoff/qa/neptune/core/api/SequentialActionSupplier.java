package ru.tinkoff.qa.neptune.core.api;

import com.google.common.annotations.Beta;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.action;

/**
 * This class is designed to build actions to be performed on different objects.
 * Also it may be used to build chains of same actions on different objects.
 *
 * @param <T> is the type of an input value.
 * @param <R> is the type of an object to perform action on.
 * @param <THIS> is self-type.
 */
@SuppressWarnings("unchecked")
public abstract class SequentialActionSupplier<T, R, THIS extends SequentialActionSupplier<T, R, THIS>> implements Supplier<Consumer<T>>{

    private final String actionDescription;

    Consumer<T> wrappedConsumer;

    protected SequentialActionSupplier(String description) {
        checkArgument(!isBlank(description), "Description of the action should not be blank or null string value");
        this.actionDescription = description;
    }

    private THIS performOnPrivate(Object functionOrObject) {
        Function<T, ? extends R> function;
        if (nonNull(functionOrObject) && Function.class.isAssignableFrom(functionOrObject.getClass())) {
            function = (Function) functionOrObject;
        }
        else {
            function = null;
        }

        var action = ofNullable(function).map(function1 ->
                action(actionDescription, (Consumer<T>) t -> {
                    R r = function1.apply(t);
                    performActionOn(r);
                }))
                .orElseGet(() -> action(actionDescription, t ->
                        performActionOn((R) functionOrObject)));

        wrappedConsumer = ofNullable(wrappedConsumer).map(tConsumer -> tConsumer.andThen(action))
                .orElse(action);
        return (THIS) this;
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
        return performOnPrivate(function);
    }

    /**
     * This is the helping method that is designed to add the action to the sequiance of actions. A new one action is
     * supposed to be performed on an object returned by the defined function. The method is supposed to be overridden
     * or overloaded/used by custom method.
     *
     * @param supplier that supplies a function to get a target object
     * @return self-reference.
     */
    protected THIS performOn(GetStepSupplier<T, ? extends R, ?> supplier) {
        checkArgument(nonNull(supplier), "Supplier of a function which gets value " +
                "to perform action is not defined");
        return performOnPrivate(supplier.get());
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
        return performOnPrivate(value);
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
        ofNullable(wrappedConsumer)
                .ifPresentOrElse(tConsumer -> {
                            checkArgument(nonNull(mergeFrom.wrappedConsumer), "There is nothing to merge because actions are not built");
                            wrappedConsumer = tConsumer.andThen(mergeFrom.wrappedConsumer);
                        },
                        () -> wrappedConsumer = mergeFrom.wrappedConsumer);
        return (THIS) this;
    }

    @Override
    public Consumer<T> get() {
        return wrappedConsumer;
    }

    @Override
    public String toString() {
        return ofNullable(wrappedConsumer).map(Object::toString).orElse(EMPTY);
    }
}
