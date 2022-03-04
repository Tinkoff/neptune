package ru.tinkoff.qa.neptune.rabbit.mq;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.rabbit.mq.function.binding.BindUnbindParameters;
import ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareParameters;
import ru.tinkoff.qa.neptune.rabbit.mq.function.declare.ServerNamedQueueSequentialGetSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.delete.DeleteParameters;
import ru.tinkoff.qa.neptune.rabbit.mq.function.get.*;
import ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.purge.RabbitMqPurgeQueueSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.RabbitMqBindSupplier.bindAction;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.RabbitMqUnBindSupplier.unBindAction;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareActionSupplier.declareAction;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.DeleteActionSupplier.deleteAction;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;


@CreateWith(provider = RabbitMqParameterProvider.class)
public class RabbitMqStepContext extends Context<RabbitMqStepContext> {

    private final Channel channel;

    public RabbitMqStepContext(Channel channel) throws IOException, TimeoutException {
        this.channel = channel;
    }

    public static RabbitMqStepContext rabbitMq() {
        return getCreatedContextOrCreate(RabbitMqStepContext.class);
    }

    public Channel getChannel() {
        return channel;
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetSupplier<T, ?> basicGet) {
        return get(basicGet);
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetArrayItemSupplier<T> basicGet) {
        return get(basicGet);
    }

    /**
     * Reads some array value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T[] read(RabbitMqBasicGetArraySupplier<T> basicGet) {
        return get(basicGet);
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetIterableItemSupplier<T> basicGet) {
        return get(basicGet);
    }

    /**
     * Reads some iterable value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T, S extends Iterable<T>> List<T> read(RabbitMqBasicGetIterableSupplier<T, S> basicGet) {
        return get(basicGet);
    }

    /**
     * Publishes a message.
     *
     * @param toPublish is what to publish
     * @return self-reference
     */
    public RabbitMqStepContext publish(RabbitMqPublishSupplier<?> toPublish) {
        return perform(toPublish);
    }

    /**
     * Deletes a queue or exchange
     *
     * @param parameters parameters of the deleting
     * @return self-reference
     */
    public RabbitMqStepContext delete(DeleteParameters<?> parameters) {
        return perform(deleteAction(parameters));
    }

    /**
     * Purges defined queue.
     *
     * @param queue is a name of queue to purge
     * @return self-reference
     */
    public RabbitMqStepContext purgeQueue(String queue) {
        return perform(RabbitMqPurgeQueueSupplier.purgeQueue(queue));
    }

    /**
     * Purges the queue. Value of the {@link RabbitMQRoutingProperties#DEFAULT_QUEUE_NAME} is used
     * as name of the queue.
     *
     * @return self-reference
     */
    public RabbitMqStepContext purgeQueue() {
        return purgeQueue(DEFAULT_QUEUE_NAME.get());
    }

    /**
     * Performs the unbinding action.
     *
     * @param parameters parameters of the unbinding
     * @return self-reference
     */
    public RabbitMqStepContext unbind(BindUnbindParameters<?> parameters) {
        return perform(unBindAction(parameters));
    }

    /**
     * Performs the binding action.
     *
     * @param parameters parameters of the binding
     * @return self-reference
     */
    public RabbitMqStepContext bind(BindUnbindParameters<?> parameters) {
        return perform(bindAction(parameters));
    }

    /**
     * Declares a queue or exchange
     *
     * @param parameters parameters of the declaring
     * @return self-reference
     */
    public RabbitMqStepContext declare(DeclareParameters<?> parameters) {
        return perform(declareAction(parameters));
    }

    /**
     * Declares a server-named queue
     *
     * @param toDeclare object which signalizes to declare a server-named queue
     * @return name of the declared queue
     */
    public String declare(ServerNamedQueueSequentialGetSupplier toDeclare) {
        return get(toDeclare);
    }
}
