package ru.tinkoff.qa.neptune.rabbit.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
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
import ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.publish.ParametersForPublish;
import ru.tinkoff.qa.neptune.rabbit.mq.function.purge.RabbitMqPurgeQueueSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.unbind.exchange.RabbitMqExchangeUnbindSupplier;
import ru.tinkoff.qa.neptune.rabbit.mq.function.unbind.queue.RabbitMqQueueUnbindSupplier;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.publish;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultMapper.RABBIT_MQ_DEFAULT_MAPPER;


@CreateWith(provider = RabbitMqParameterProvider.class)
public class RabbitMqStepContext extends Context<RabbitMqStepContext> {

    private static final RabbitMqStepContext context = getInstance(RabbitMqStepContext.class);
    private final Channel channel;

    public RabbitMqStepContext(Address[] addresses, String username, String password) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        if (!username.isBlank()) {
            factory.setUsername(username);
        }
        if (!password.isBlank()) {
            factory.setPassword(password);
        }

        Connection connection = factory.newConnection(addresses);
        this.channel = connection.createChannel();
    }

    public static RabbitMqStepContext rabbitMq() {
        return context;
    }

    public Channel getChannel() {
        return channel;
    }

    public RabbitMqStepContext deleteQueue(String queue){
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

    public AMQP.Exchange.UnbindOk ExchangeUnbind(String destination, String source, String routingKey, AdditionalArguments arguments) {
        return get(RabbitMqExchangeUnbindSupplier.exchangeUnbind(destination, source, routingKey).setArguments(arguments));
    }

    public AMQP.Exchange.UnbindOk ExchangeUnbind(String destination, String source, String routingKey) {
        return get(RabbitMqExchangeUnbindSupplier.exchangeUnbind(destination, source, routingKey));
    }

    public AMQP.Queue.DeclareOk queueDeclare() {
        return get(RabbitMqQueueDeclareSupplier.queueDeclare());
    }

    public AMQP.Queue.DeclareOk queueDeclarePassive(String queue) {
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

    public AMQP.Exchange.BindOk exchangeBind(String destination, String source, String routingKey){
        return get(RabbitMqExchangeBindSupplier.exchangeBind(destination, source, routingKey));
    }

    public AMQP.Exchange.BindOk exchangeBind(String destination, String source, String routingKey, AdditionalArguments arguments){
        return get(RabbitMqExchangeBindSupplier.exchangeBind(destination, source, routingKey).setArguments(arguments));
    }

    public AMQP.Queue.BindOk queueBind(String destination, String source, String routingKey){
        return get(RabbitMqQueueBindSupplier.queueBind(destination, source, routingKey));
    }

    public AMQP.Queue.BindOk queueBind(String destination, String source, String routingKey, AdditionalArguments arguments){
        return get(RabbitMqQueueBindSupplier.queueBind(destination, source, routingKey).setArguments(arguments));
    }

    public RabbitMqStepContext exchangeDelete(String exchange) {
        return perform(RabbitMqExchangeDeleteSupplier.exchangeDelete(exchange, false));
    }

    public RabbitMqStepContext exchangeDelete(String exchange, boolean ifUnused) {
        return perform(RabbitMqExchangeDeleteSupplier.exchangeDelete(exchange, ifUnused));
    }

    public <T> T read(RabbitMqBasicGetSupplier<T> basicGet, ObjectMapper mapper) {
        return get(basicGet.setObjectMapper(mapper));
    }

    public <T> T read(RabbitMqBasicGetSupplier<T> basicGet) {
        return read(basicGet, RABBIT_MQ_DEFAULT_MAPPER.get());
    }

    public RabbitMqStepContext publishMessage(String exchange, String routingKey, Object toSerialize) {
        return perform(publish(exchange, routingKey, toSerialize, RABBIT_MQ_DEFAULT_MAPPER.get()));
    }

    public RabbitMqStepContext publishMessage(String exchange, String routingKey, Object toSerialize, ObjectMapper mapper) {
        return perform(publish(exchange, routingKey, toSerialize, mapper));
    }

    public RabbitMqStepContext publishMessage(String exchange, String routingKey, ParametersForPublish params, Object toSerialize) {
        return perform(publish(exchange, routingKey, toSerialize, RABBIT_MQ_DEFAULT_MAPPER.get()).setParams(params));
    }

    public RabbitMqStepContext publishMessage(String exchange, String routingKey, ParametersForPublish params, Object toSerialize, ObjectMapper mapper) {
        return perform(publish(exchange, routingKey, toSerialize, mapper).setParams(params));
    }
}
