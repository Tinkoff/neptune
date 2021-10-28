package ru.tinkoff.qa.neptune.spring.data.select.common;

import org.springframework.data.domain.ExampleMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.util.LinkedList;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.data.domain.ExampleMatcher.matching;

public class ProbePojo<R> implements StepParameterPojo {

    @StepParameter(value = "probe", makeReadableBy = ProbeSerializingParameterValueGetter.class)
    private final R probe;
    private final LinkedList<Function<ExampleMatcher, ExampleMatcher>> matcherBuilder = new LinkedList<>();
    @StepParameter(value = "matcher", makeReadableBy = ProbeSerializingParameterValueGetter.class)
    private ExampleMatcher exampleMatcher = matching();

    public ProbePojo(R probe) {
        this.probe = probe;
    }

    void initialMatcher(ExampleMatcher exampleMatcher) {
        checkNotNull(exampleMatcher);
        this.exampleMatcher = exampleMatcher;
        matcherBuilder.forEach(f -> this.exampleMatcher = f.apply(this.exampleMatcher));
    }

    void addMatcher(Function<ExampleMatcher, ExampleMatcher> f) {
        checkNotNull(f);
        exampleMatcher = f.apply(exampleMatcher);
        matcherBuilder.addLast(f);
    }

    ExampleMatcher getExampleMatcher() {
        return exampleMatcher;
    }

    R getProbe() {
        return probe;
    }
}
