package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.steps.StepFunction.toGet;

class EvaluateAndMatchAction<T, R> extends MatchAction<T, R> {

    private final Function<T, R> eval;

    EvaluateAndMatchAction(String description, Matcher<? super R> criteria, Function<T, R> eval) {
        super(description, criteria);
        checkArgument(nonNull(eval), "Function to evaluate a value should be defined");
        var describedFunction = toGet(description, eval);
        describedFunction.onFinishMakeCaptureOfType(Object.class);
        this.eval = describedFunction;
    }

    @Override
    public void accept(T t) {
        var r = eval.apply(t);
        assertThat(r, new InnerMatcher<>(getDescription(), getCriteria()));
    }
}
