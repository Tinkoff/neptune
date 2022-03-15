package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

@Description("getTestGetStepSupplier3")
public class TestGetStepSupplier3 extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<Object, Object, Object, TestGetStepSupplier3> {


    protected TestGetStepSupplier3() {
        super(o -> new Object[]{new Object()});
    }

    public static TestGetStepSupplier3 getTestGetStepSupplier3() {
        return new TestGetStepSupplier3();
    }

    @Override
    protected TestGetStepSupplier3 from(Object from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier3 from(Function<Object, ?> from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier3 from(SequentialGetStepSupplier<Object, ?, ?, ?, ?> from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier3 pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected TestGetStepSupplier3 timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    protected Map<String, String> additionalParameters() {
        return Map.of();
    }
}
