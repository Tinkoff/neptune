package ru.tinkoff.qa.neptune.rabbit.mq.function.delete;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

@SuppressWarnings("unchecked")
public abstract class DeleteParameters<T extends DeleteParameters<T>> extends SelfDescribed implements StepParameterPojo {

    @StepParameter("ifUnused")
    private boolean ifUnused;

    abstract void delete(Channel channel);

    boolean isIfUnused() {
        return ifUnused;
    }

    /**
     * Sets a flag which means to delete queue/exchange if it is unused.
     *
     * @return self-reference
     */
    public T ifUnused() {
        this.ifUnused = true;
        return (T) this;
    }
}
