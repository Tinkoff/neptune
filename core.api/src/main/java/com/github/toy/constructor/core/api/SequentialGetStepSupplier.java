package com.github.toy.constructor.core.api;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.utils.IsDescribedUtil.isDescribed;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * It is designed to typify functions which get required value and to restrict chains of
 * the applying of functions.
 *
 * @param <T> is a type of an input value.
 * @param <R> is a type of a returned value.
 * @param <Q> is a type of a mediator value which is used to get the required result.
 * @param <THIS> is self-type. It is necessary for the {@link #set(Function)} method.
 */
@SuppressWarnings("unchecked")
public abstract class SequentialGetStepSupplier<T, R, Q, THIS extends SequentialGetStepSupplier<T, R, Q, THIS>>
        extends GetStepSupplier<T, R, THIS> {

    /**
     * This method is designed to represent a chain of result calculation and restrict it.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param mediatorFunction a function which returns a mediate value to get expected result.
     * @return self-reference.
     */
    protected THIS from(Function<T, ? extends Q> mediatorFunction) {
        checkArgument(mediatorFunction != null,
                "Function to get value from was not defined");

        Function<Q, R> result = getEndFunction();
        checkArgument(result != null,
                "The result function to get value is not defined");
        checkArgument(isDescribed(result),
                "The result function should be described. Use method " +
                        "StoryWriter.toGet to describe it or override the toString method");

        StepFunction<Q, R> stepFunction;
        if (StepFunction.class.isAssignableFrom(result.getClass())) {
            stepFunction = StepFunction.class
                    .cast(result);
        }
        else {
            stepFunction = StepFunction.class
                    .cast(toGet(result.toString(), result));
        }

        return set(stepFunction.compose(mediatorFunction));
    }

    /**
     * This method is designed to represent a chain of result calculation and restrict it.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param supplier of a wrapper of the function which returns a mediate value to get expected result.
     * @return self-reference.
     */
    protected THIS from(GetStepSupplier<T, ? extends Q, ?> supplier) {
        checkArgument(supplier != null, "The supplier of the function is not defined");
        return from(supplier.get());
    }

    /**
     * This method is designed to represent a chain of result calculation and restrict it.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param value is a mediate value to get expected result. It is supposed that this object is got from the
     *              input value {@code T} firstly.
     * @return self-reference.
     */
    protected THIS from(Q value) {
        checkArgument(value != null, "The object to get value from is not defined");
        return from(t -> value);
    }

    /**
     * Returns a function. Warning!!!! It is necessary to describe the result function by the
     * {@link StoryWriter#toGet(String, Function)} method or by the overriding of toString method.
     *
     * @return a functions which returns required result on the applying.
     */
    protected abstract Function<Q, R> getEndFunction();
}
