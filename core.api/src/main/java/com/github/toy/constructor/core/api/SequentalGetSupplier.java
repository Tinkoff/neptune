package com.github.toy.constructor.core.api;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * It is designed to typify functions which get required value and to restrict chains of
 * the applying of functions.
 *
 * @param <T> is a type of an input value.
 * @param <R> is a type of a returned value.
 * @param <Q> is a type of a mediator value which is used to get the required result.
 * @param <THIS> is self-type. It is necessary for the {@link #set(String, Function)} method.
 */
public abstract class SequentalGetSupplier<T, R, Q, THIS extends SequentalGetSupplier<T, R, Q, THIS>>
        extends GetSupplier<T, R, THIS> {

    /**
     * This method is designed to represent a chain of result calculation and restrict it.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param mediatorFunction a function which returns a mediate value to get expected result.
     * @return self-reference.
     */
    protected THIS from(Function<T, Q> mediatorFunction) {
        checkArgument(mediatorFunction != null, "Function to get value from was not " +
                "defined");
        checkArgument(DescribedFunction.class.isAssignableFrom(mediatorFunction.getClass()),
                "Function to get value from is not described. " +
                        "Use method StoryWriter.toGet to describe it.");
        Function<T, R> resultFunction = mediatorFunction.andThen(getEndFunction());
        return set(resultFunction.toString(), resultFunction);
    }

    /**
     * This method is designed to represent a chain of result calculation and restrict it.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param supplier of a function which returns a mediate value to get expected result.
     * @return self-reference.
     */
    protected THIS from(GetSupplier<T, Q, ?> supplier) {
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
        return from(toGet(value.toString(), t -> value));
    }

    /**
     * Returns a function. Warning!!!! It is necessary to describe the result function
     * by the {@link StoryWriter#toGet(String, Function)} method.
     *
     * @return a functions which returns required result on the applying.
     */
    protected abstract Function<Q, R> getEndFunction();
}
