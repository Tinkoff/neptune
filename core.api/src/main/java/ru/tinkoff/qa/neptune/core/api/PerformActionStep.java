package ru.tinkoff.qa.neptune.core.api;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.action;
import static ru.tinkoff.qa.neptune.core.api.utils.IsDescribedUtil.isDescribed;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unchecked")
public interface PerformActionStep<THIS extends PerformActionStep<THIS>> {

    default THIS perform(Consumer<THIS> actionConsumer) {
        checkArgument(actionConsumer != null, "Action is not defined");
        checkArgument(isDescribed(actionConsumer),
                "Action should be described by the StoryWriter.action method." +
                        "Also you can override the toString method");

        StepAction<THIS> stepAction;
        if (StepAction.class.isAssignableFrom(actionConsumer.getClass())) {
            stepAction = StepAction.class.cast(actionConsumer);
        }
        else {
            stepAction = StepAction.class.cast(action(actionConsumer.toString(), actionConsumer));
        }

        stepAction.accept((THIS) this);
        return (THIS) this;
    }

    default THIS perform(Supplier<Consumer<THIS>> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return perform(actionSupplier.get());
    }
}
