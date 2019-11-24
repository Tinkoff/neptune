package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;

class EvaluateAndMatchAction<T, R> extends MatchAction<T, R> {

    private final Function<T, R> eval;

    EvaluateAndMatchAction(Matcher<? super R> criteria, String description, Function<T, R> eval) {
        super(description, criteria);
        checkArgument(nonNull(eval), "Function that evaluates value to be checked should be defined");
        var describedFunction = toGet(description, eval);
        describedFunction.onFinishMakeCaptureOfType(Object.class);
        this.eval = describedFunction;
    }

    @Override
    public void accept(T t) {
        var r = eval.apply(t);
        assertThat(r, new InnerMatcher<>(description, criteria));
    }
}
