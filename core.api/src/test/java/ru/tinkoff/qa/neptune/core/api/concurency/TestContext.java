package ru.tinkoff.qa.neptune.core.api.concurency;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.ProviderOfEmptyParameters;

import java.util.function.Consumer;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class TestContext implements GetStepContext<TestContext>, ActionStepContext<TestContext>, Stoppable {

    private boolean isActive = true;

    public TestContext perform(String description, Consumer<TestContext> actionConsumer) {
        isActive = true;
        return ActionStepContext.super.perform(description, actionConsumer);
    }

    public  <T> T get(String description, Function<TestContext, T> function) {
        isActive = true;
        return GetStepContext.super.get(toGet(description, function));
    }

    @Override
    public void stop() {
        isActive = false;
    }

    boolean isActive() {
        return isActive;
    }
}
