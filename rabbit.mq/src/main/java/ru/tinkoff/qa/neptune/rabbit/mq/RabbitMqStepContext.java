package ru.tinkoff.qa.neptune.rabbit.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.rabbit.mq.function.bind.exchange.RabbitMqExchangeBindSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.bind.queue.RabbitMqQueueBindSupplier;
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
import ru.tinkoff.qa.neptune.rabbit.mq.function.unbind.exchange.RabbitMqExchangeUnbindSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.unbind.queue.RabbitMqQueueUnbindSupplier;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.DataTransformerSetter.setDataTransformer;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.publish;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaulDataTransformer.RABBIT_MQ_DEFAULT_DATA_TRANSFORMER;


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

    public RabbitMqStepContext publishMessage(String exchange, String routingKey, Object toSerialize) {
        return publishMessage(exchange, routingKey, toSerialize, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

    public RabbitMqStepContext publishMessage(String exchange, String routingKey, Object toSerialize, DataTransformer mapper) {
        return perform(publish(exchange, routingKey, toSerialize, mapper));
    }

    public RabbitMqStepContext publishMessage(String exchange, String routingKey, ParametersForPublish params, Object toSerialize) {
        return publishMessage(exchange, routingKey, params, toSerialize, RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.get());
    }

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

    public AMQP.Queue.UnbindOk queueUnbind(String queue, String exchange, String routingKey) {
        return get(RabbitMqQueueUnbindSupplier.queueUnbind(queue, exchange, routingKey));
    }

    public AMQP.Queue.UnbindOk queueUnbind(String queue, String exchange, String routingKey, AdditionalArguments arguments) {
        return get(RabbitMqQueueUnbindSupplier.queueUnbind(queue, exchange, routingKey).setArguments(arguments));
    }

    public AMQP.Exchange.UnbindOk exchangeUnbind(String destination, String source, String routingKey, AdditionalArguments arguments) {
        return get(RabbitMqExchangeUnbindSupplier.exchangeUnbind(destination, source, routingKey).setArguments(arguments));
    }

    public AMQP.Exchange.UnbindOk exchangeUnbind(String destination, String source, String routingKey) {
        return get(RabbitMqExchangeUnbindSupplier.exchangeUnbind(destination, source, routingKey));
    }

    public AMQP.Queue.DeclareOk queueDeclare() {
        return get(RabbitMqQueueDeclareSupplier.queueDeclare());
    }

    public AMQP.Queue.DeclareOk queueDeclare(String queue) {
        return get(RabbitMqQueueDeclareSupplier.queueDeclarePassive(queue));
    }

    public AMQP.Queue.DeclareOk queueDeclare(String queue, ParametersForDeclareQueue params) {
        return get(RabbitMqQueueDeclareSupplier.queueDeclare(queue, params));
    }

    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange) {
        return get(RabbitMqExchangeDeclareSupplier.exchangeDeclarePassive(exchange));
    }

    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, String type){
        return get(RabbitMqExchangeDeclareSupplier.exchangeDeclare(exchange, type));
    }

    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, String type, ParametersForDeclareExchange params){
        return get(RabbitMqExchangeDeclareSupplier.exchangeDeclare(exchange, type, params));
    }

    public AMQP.Exchange.BindOk exchangeBind(String destination, String source, String routingKey) {
        return get(RabbitMqExchangeBindSupplier.exchangeBind(destination, source, routingKey));
    }

    public AMQP.Exchange.BindOk exchangeBind(String destination, String source, String routingKey, AdditionalArguments arguments) {
        return get(RabbitMqExchangeBindSupplier.exchangeBind(destination, source, routingKey).setArguments(arguments));
    }

    public AMQP.Queue.BindOk queueBind(String queue, String exchange, String routingKey) {
        return get(RabbitMqQueueBindSupplier.queueBind(queue, exchange, routingKey));
    }

    public AMQP.Queue.BindOk queueBind(String queue, String exchange, String routingKey, AdditionalArguments arguments) {
        return get(RabbitMqQueueBindSupplier.queueBind(queue, exchange, routingKey).setArguments(arguments));
    }
}
