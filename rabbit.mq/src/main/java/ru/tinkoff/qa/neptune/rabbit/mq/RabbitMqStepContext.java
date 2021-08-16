package ru.tinkoff.qa.neptune.rabbit.mq;

import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.rabbit.mq.function.binding.BindUnbindParameters;
import ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange.ParametersForDeclareExchange;
import ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange.RabbitMqExchangeDeclareSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue.ParametersForDeclareQueue;
import ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue.RabbitMqQueueDeclareSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.delete.exchange.RabbitMqExchangeDeleteSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.delete.queue.ParametersForDelete;
import ru.tinkoff.qa.neptune.rabbit.mq.function.delete.queue.RabbitMqQueueDeleteSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.get.*;
import ru.tinkoff.qa.neptune.rabbit.mq.function.publish.ParametersForPublish;
import ru.tinkoff.qa.neptune.rabbit.mq.function.purge.RabbitMqPurgeQueueSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.RabbitMqBindSupplier.bindAction;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.RabbitMqUnBindSupplier.unBindAction;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.DataTransformerSetter.setDataTransformer;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.publish;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_ROUTING_KEY_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultDataTransformer.RABBIT_MQ_DEFAULT_DATA_TRANSFORMER;


@CreateWith(provider = RabbitMqParameterProvider.class)
public class RabbitMqStepContext extends Context<RabbitMqStepContext> {

    private static final RabbitMqStepContext context = getInstance(RabbitMqStepContext.class);
    private final Channel channel;

    public RabbitMqStepContext(Channel channel) throws IOException, TimeoutException {
        this.channel = channel;
    }

    public static RabbitMqStepContext rabbitMq() {
        return context;
    }

    public Channel getChannel() {
        return channel;
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param mapper   how to deserialize read message
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetSupplier<T> basicGet, DataTransformer mapper) {
        return get(setDataTransformer(mapper, basicGet));
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetSupplier<T> basicGet) {
        return read(basicGet, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param mapper   how to deserialize read message
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetArrayItemSupplier<T> basicGet, DataTransformer mapper) {
        return get(setDataTransformer(mapper, basicGet));
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetArrayItemSupplier<T> basicGet) {
        return read(basicGet, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

    /**
     * Reads some array value from queue
     *
     * @param basicGet how to read value
     * @param mapper   how to deserialize read message
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T[] read(RabbitMqBasicGetArraySupplier<T> basicGet, DataTransformer mapper) {
        return get(setDataTransformer(mapper, basicGet));
    }

    /**
     * Reads some array value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T[] read(RabbitMqBasicGetArraySupplier<T> basicGet) {
        return read(basicGet, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param mapper   how to deserialize read message
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetIterableItemSupplier<T> basicGet, DataTransformer mapper) {
        return get(setDataTransformer(mapper, basicGet));
    }

    /**
     * Reads some value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T> T read(RabbitMqBasicGetIterableItemSupplier<T> basicGet) {
        return read(basicGet, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

    /**
     * Reads some iterable value from queue
     *
     * @param basicGet how to read value
     * @param mapper   how to deserialize read message
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T, S extends Iterable<T>> S read(RabbitMqBasicGetIterableSupplier<T, S> basicGet, DataTransformer mapper) {
        return get(setDataTransformer(mapper, basicGet));
    }

    /**
     * Reads some iterable value from queue
     *
     * @param basicGet how to read value
     * @param <T>      is a type of desired value
     * @return read value
     */
    public <T, S extends Iterable<T>> S read(RabbitMqBasicGetIterableSupplier<T, S> basicGet) {
        return read(basicGet, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

    /**
     * Publishes a message using default routing key.
     *
     * @param toSerialize is an object to serialize into string message
     * @return self-reference
     * @see RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME
     */
    public RabbitMqStepContext publishMessage(Object toSerialize) {
        return publishMessage(DEFAULT_EXCHANGE_NAME.get(), DEFAULT_ROUTING_KEY_NAME.get(), toSerialize);
    }

    /**
     * Publishes a message using default routing key.
     *
     * @param exchange    is the name of exchange
     * @param toSerialize is an object to serialize into string message
     * @return self-reference
     * @see RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME
     */
    public RabbitMqStepContext publishMessage(String exchange, Object toSerialize) {
        return publishMessage(exchange, DEFAULT_ROUTING_KEY_NAME.get(), toSerialize);
    }

    /**
     * Publishes a message
     *
     * @param exchange    is the name of exchange
     * @param routingKey  is the name of routing key
     * @param toSerialize is an object to serialize into string message
     * @return self-reference
     */
    public RabbitMqStepContext publishMessage(String exchange, String routingKey, Object toSerialize) {
        return publishMessage(exchange, routingKey, toSerialize, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

    /**
     * Publishes a message using default routing key and exchange.
     *
     * @param toSerialize is an object to serialize into string message
     * @param mapper      performs serialization
     * @return self-reference
     * @see RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME
     * @see RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME
     */
    public RabbitMqStepContext publishMessage(Object toSerialize, DataTransformer mapper) {
        return publishMessage(DEFAULT_EXCHANGE_NAME.get(), DEFAULT_ROUTING_KEY_NAME.get(), toSerialize, mapper);
    }

    /**
     * Publishes a message using default routing key.
     *
     * @param exchange    is the name of exchange
     * @param toSerialize is an object to serialize into string message
     * @param mapper      performs serialization
     * @return self-reference
     * @see RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME
     */
    public RabbitMqStepContext publishMessage(String exchange, Object toSerialize, DataTransformer mapper) {
        return publishMessage(exchange, DEFAULT_ROUTING_KEY_NAME.get(), toSerialize, mapper);
    }


    /**
     * Publishes a message
     *
     * @param exchange    is the name of exchange
     * @param routingKey  is the name of routing key
     * @param toSerialize is an object to serialize into string message
     * @param mapper      performs serialization
     * @return self-reference
     */
    public RabbitMqStepContext publishMessage(String exchange, String routingKey, Object toSerialize, DataTransformer mapper) {
        return perform(publish(exchange, routingKey, toSerialize, mapper));
    }

    /**
     * Publishes a message using default routing key and exchange.
     *
     * @param params      parameters of the publishing
     * @param toSerialize is an object to serialize into string message
     * @return self-reference
     * @see RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME
     * @see RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME
     */
    public RabbitMqStepContext publishMessage(ParametersForPublish params, Object toSerialize) {
        return publishMessage(DEFAULT_EXCHANGE_NAME.get(), DEFAULT_ROUTING_KEY_NAME.get(), params, toSerialize);
    }

    /**
     * Publishes a message using default routing key.
     *
     * @param exchange    is the name of exchange
     * @param params      parameters of the publishing
     * @param toSerialize is an object to serialize into string message
     * @return self-reference
     * @see RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME
     */
    public RabbitMqStepContext publishMessage(String exchange, ParametersForPublish params, Object toSerialize) {
        return publishMessage(exchange, DEFAULT_ROUTING_KEY_NAME.get(), params, toSerialize);
    }


    /**
     * Publishes a message
     *
     * @param exchange    is the name of exchange
     * @param routingKey  is the name of routing key
     * @param params      parameters of the publishing
     * @param toSerialize is an object to serialize into string message
     * @return self-reference
     */
    public RabbitMqStepContext publishMessage(String exchange, String routingKey, ParametersForPublish params, Object toSerialize) {
        return publishMessage(exchange, routingKey, params, toSerialize, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

    /**
     * Publishes a message using default routing key and exchange.
     *
     * @param params      parameters of the publishing
     * @param toSerialize is an object to serialize into string message
     * @param mapper      performs serialization
     * @return self-reference
     * @see RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME
     * @see RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME
     */
    public RabbitMqStepContext publishMessage(ParametersForPublish params, Object toSerialize, DataTransformer mapper) {
        return publishMessage(DEFAULT_EXCHANGE_NAME.get(), DEFAULT_ROUTING_KEY_NAME.get(), params, toSerialize, mapper);
    }

    /**
     * Publishes a message using default routing key.
     *
     * @param exchange    is the name of exchange
     * @param params      parameters of the publishing
     * @param toSerialize is an object to serialize into string message
     * @param mapper      performs serialization
     * @return self-reference
     * @see RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME
     */
    public RabbitMqStepContext publishMessage(String exchange, ParametersForPublish params, Object toSerialize, DataTransformer mapper) {
        return publishMessage(exchange, DEFAULT_ROUTING_KEY_NAME.get(), params, toSerialize, mapper);
    }

    /**
     * Publishes a message
     *
     * @param exchange    is the name of exchange
     * @param routingKey  is the name of routing key
     * @param params      parameters of the publishing
     * @param toSerialize is an object to serialize into string message
     * @param mapper      performs serialization
     * @return self-reference
     */
    public RabbitMqStepContext publishMessage(String exchange, String routingKey, ParametersForPublish params, Object toSerialize, DataTransformer mapper) {
        return perform(publish(exchange, routingKey, toSerialize, mapper).setParams(params));
    }


    public RabbitMqStepContext exchangeDelete(String exchange) {
        return perform(RabbitMqExchangeDeleteSupplier.exchangeDelete(exchange, false));
    }

    public RabbitMqStepContext exchangeDelete(String exchange, boolean ifUnused) {
        return perform(RabbitMqExchangeDeleteSupplier.exchangeDelete(exchange, ifUnused));
    }

    public RabbitMqStepContext deleteQueue(String queue) {
        return perform(RabbitMqQueueDeleteSupplier.deleteQueue(queue));
    }

    public RabbitMqStepContext deleteQueue(String queue, ParametersForDelete parametersForDelete) {
        return perform(RabbitMqQueueDeleteSupplier.deleteQueue(queue).setParametersForDelete(parametersForDelete));
    }

    public RabbitMqStepContext purgeQueue(String queue) {
        return perform(RabbitMqPurgeQueueSupplier.purgeQueue(queue));
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


    public RabbitMqStepContext queueDeclare() {
        return perform(RabbitMqQueueDeclareSupplier.queueDeclare());
    }

    public RabbitMqStepContext queueDeclare(String queue) {
        return perform(RabbitMqQueueDeclareSupplier.queueDeclarePassive(queue));
    }

    public RabbitMqStepContext queueDeclare(String queue, ParametersForDeclareQueue params) {
        return perform(RabbitMqQueueDeclareSupplier.queueDeclare(queue, params));
    }

    public RabbitMqStepContext exchangeDeclare(String exchange) {
        return perform(RabbitMqExchangeDeclareSupplier.exchangeDeclarePassive(exchange));
    }

    public RabbitMqStepContext exchangeDeclare(String exchange, String type) {
        return perform(RabbitMqExchangeDeclareSupplier.exchangeDeclare(exchange, type));
    }

    public RabbitMqStepContext exchangeDeclare(String exchange, String type, ParametersForDeclareExchange params) {
        return perform(RabbitMqExchangeDeclareSupplier.exchangeDeclare(exchange, type, params));
    }
}
