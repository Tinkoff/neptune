package ru.tinkoff.qa.neptune.core.api;

import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.action;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.utils.IsDescribedUtil.isDescribed;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class is designed to restrict actions to perform on certain values.
 * Also it restricts chains of actions. It is supposed to supply chains of similar
 * actions.
 *
 * @param <T> is the type of an input value.
 * @param <R> is the type of a value which is supposed to be used by some action.
 * @param <THIS> is self-type. It is necessary for {@link #andThen(String, Function, Object...)} and
 *              {@link #andThen(String, Object, Object...)} methods.
 */
@SuppressWarnings("unchecked")
public abstract class SequentialActionSupplier<T, R, THIS extends SequentialActionSupplier<T, R, THIS>> implements Supplier<Consumer<T>>{

    private Consumer<T> wrappedConsumer;

    private THIS andThenPrivate(String actionDescription, Object functionOrObject, Object...additionalArguments) {
        checkArgument(!isBlank(actionDescription), "Description should not be blank");
        checkArgument(functionOrObject != null,
                "Function which gets value/an object to perform action is not defined");
        checkArgument(additionalArguments != null,
                "Array of additional arguments should not be a null value");

        String description;
        Function<T, ? extends R> function;
        if (Function.class.isAssignableFrom(functionOrObject.getClass())) {
            function = (Function) functionOrObject;
            if (isDescribed(function)) {
                description = format("%s. Target: %s", actionDescription, function);
            }
            else {
                description = actionDescription;
            }
        }
        else {
            description = format("%s. Target: %s", actionDescription, functionOrObject);
            function = null;
        }

        var fullDescription = additionalArguments.length == 0? description: format("%s. Action parameters: %s", description,
                ArrayUtils.toString(additionalArguments));

        var action = ofNullable(function).map(function1 ->
                action(fullDescription, (Consumer<T>) t -> {
                    R r = function1.apply(t);
                    performActionOn(r, additionalArguments);
                }))
                .orElseGet(() -> action(fullDescription, t ->
                        performActionOn((R) functionOrObject, additionalArguments)));

        wrappedConsumer = getActionSequence(action);
        return (THIS) this;
    }

    /**
     * This method creates/changes sequence of actions to be performed. It may be overridden/overloaded if is necessary.
     * @param action to be added to the sequence
     * @return created/changed sequence of actions
     */
    protected Consumer<T> getActionSequence(Consumer<T> action) {
        return ofNullable(wrappedConsumer).map(tConsumer -> tConsumer.andThen(action)).orElse(action);
    }

    /**
     * This is the helping method which designed to build chains of similar actions on some objects got from
     * the input value. It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param actionDescription is the description of the action
     * @param function which gets a target object
     * @param additionalArguments that are needed to perform the action. It may be ignored
     * @return self-reference.
     */
    protected THIS andThen(String actionDescription, Function<T, ? extends R> function, Object...additionalArguments) {
        return andThenPrivate(actionDescription, function, additionalArguments);
    }

    /**
     * This is the helping method which designed to build chains of similar actions on some objects got from
     * the input value. It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param actionDescription is the description of the action
     * @param supplier which contains function to get a target object from input value
     * @param additionalArguments that are needed to perform the action. It may be ignored
     * @return self-reference.
     */
    protected THIS andThen(String actionDescription, GetStepSupplier<T, ? extends R, ?> supplier, Object...additionalArguments) {
        checkArgument(supplier != null, "Supplier of a function which gets value " +
                "to perform action is not defined");
        return andThenPrivate(actionDescription, supplier.get(), additionalArguments);
    }

    /**
     * This is the helping method which designed to build chains of similar actions on some objects taken from
     * the input value. It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param actionDescription is the description of the action
     * @param descriptionOfAnObject is the description of an object to perform the action
     * @param value that is used to perform the action. It is supposed to be an object taken from the input
     *              value {@code T} firstly
     * @param additionalArguments that are needed to perform the action. It may be ignored
     * @return self-reference.
     */
    protected THIS andThen(String actionDescription, String descriptionOfAnObject,
                           R value, Object...additionalArguments) {
        checkArgument(!isBlank(descriptionOfAnObject),
                "Description of an object to perform the action should not be blank");
        checkArgument(value != null,
                "Object to perform action is not defined");
        return andThenPrivate(actionDescription, toGet(descriptionOfAnObject, t -> value),
                additionalArguments);
    }

    /**
     * This is the helping method which designed to build chains of similar actions on some objects got from
     * the input value. It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param actionDescription is the description of the action
     * @param value that is used to perform the action. It is supposed to be an object taken from the input
     *              value {@code T} firstly
     * @param additionalArguments that are needed to perform the action. It may be ignored
     * @return self-reference.
     */
    protected THIS andThen(String actionDescription, R value, Object...additionalArguments) {
        checkArgument(value != null,
                "Object to perform action is not defined");
        return andThenPrivate(actionDescription, value, additionalArguments);
    }

    /**
     * This abstract method describes actions that should be performed on some
     * value.
     *
     * @param value is an object for actions.
     * @param additionalArgument that needed to perform the action. It may be ignored.
     */
    protected abstract void performActionOn(R value, Object...additionalArgument);

    @Override
    public Consumer<T> get() {
        return wrappedConsumer;
    }

    @Override
    public String toString() {
        return ofNullable(wrappedConsumer).map(Object::toString).orElse(EMPTY);
    }
}
