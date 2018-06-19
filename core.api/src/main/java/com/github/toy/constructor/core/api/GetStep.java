package com.github.toy.constructor.core.api;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StaticRecorder.recordResult;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public interface GetStep<THIS extends GetStep<THIS>> {

    @StepMark(constantMessagePart = "Get:")
    default  <T> T get(Function<THIS, T> function) {
        checkArgument(function != null,
                "The function is not defined");
        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "The function which returns the goal value should be described " +
                        "by the StoryWriter.toGet method.");

        DescribedFunction<THIS, T> describedFunction = DescribedFunction.class.cast(function);
        LinkedList<Function<Object, Object>> functionSequence = describedFunction.getSequence();

        if (functionSequence.size() == 0) {
            if (!describedFunction.isSecondary()) {
                return log(recordResult((THIS) this, function));
            }
            else {
                return log(function.apply((THIS) this));
            }
        }

        LinkedList<Function<Object, Object>> sequence = new LinkedList<>(functionSequence);
        Function<Object, Object> first = sequence.get(0);
        sequence.removeFirst();
        Object value = get(((DescribedFunction) toGet(first.toString(),
                thisParam -> recordResult(this, first))).setSecondary());

        for (Function<Object, Object> function1: sequence) {
            Object from = value;
            value = get(((DescribedFunction) toGet(format("from %s get %s", valueOf(from), function1), thisParam ->
                    ofNullable(from).map(o -> recordResult(o, function1))
                            .orElse(null)))
                    .setSecondary());
        }
        return (T) value;
    }

    default  <T> T get(Supplier<Function<THIS, T>> functionSupplier) {
        checkNotNull(functionSupplier, "Supplier of the value to get was not defined");
        return get(functionSupplier.get());
    }

    @StepMark(constantMessagePart = "Returned value:")
    default  <T> T log(T value) {
        return value;
    }
}
