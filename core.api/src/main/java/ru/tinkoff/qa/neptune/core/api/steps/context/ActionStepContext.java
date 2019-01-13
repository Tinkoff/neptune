package ru.tinkoff.qa.neptune.core.api.steps.context;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.action;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unchecked")
public interface ActionStepContext<THIS extends ActionStepContext<THIS>> {

    default THIS perform(Consumer<THIS> actionConsumer) {
        checkArgument(nonNull(actionConsumer), "Action is not defined");
        actionConsumer.accept((THIS) this);
        return (THIS) this;
    }

    default THIS perform(Supplier<Consumer<THIS>> actionSupplier) {
        checkNotNull(actionSupplier, "Supplier of the action was not defined");
        return perform(actionSupplier.get());
    }

    default THIS perform(String description, Consumer<THIS> actionConsumer) {
        checkArgument(!isBlank(description), "Description should not be null or an empty string");
        return perform(action(description, actionConsumer));
    }
}
