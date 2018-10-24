package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.exception.management.IgnoresThrowable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.utils.IsDescribedUtil.isDescribed;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * This class is designed to typify functions which get required value.
 *
 * @param <T> is a type of an input value.
 * @param <R> is a type of a returned value.
 * @param <THIS> is self-type. It is necessary for the {@link #set(Function)} method.
 */
@SuppressWarnings("unchecked")
public abstract class GetStepSupplier<T, R, THIS extends GetStepSupplier<T, R, THIS>> implements Supplier<Function<T, R>>,
        IgnoresThrowable<THIS> {

    private StepFunction<T, R> function;
    protected final Set<Class<? extends Throwable>> ignored = new HashSet<>();

    /**
     * Sets a functions and returns self-reference.
     * It is supposed to be overridden or overloaded/used by custom method.
     *
     * @param function which returns a goal value.
     * @return self-reference.
     */
    protected THIS set(Function<T, R> function) {
        checkNotNull(function);
        checkArgument(isDescribed(function),
                "It seems given function doesn't describe any value to get. Use method " +
                        "StoryWriter.toGet to describe it or override the toString method");
        StepFunction stepFunction;
        if (StepFunction.class.isAssignableFrom(function.getClass())) {
            stepFunction = (StepFunction) function;
        }
        else {
            stepFunction = (StepFunction) toGet(function.toString(), function);
        }
        this.function = stepFunction
                .addIgnored(new ArrayList<>(ignored));
        return (THIS) this;
    }

    @Override
    public Function<T, R> get() {
        return function;
    }

    @Override
    public final THIS addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        ofNullable(function).ifPresent(function1 ->
                ((IgnoresThrowable) function1).addIgnored(toBeIgnored));
        return (THIS) this;
    }

    @Override
    public String toString() {
        return ofNullable(function).map(Object::toString).orElse(EMPTY);
    }
}
