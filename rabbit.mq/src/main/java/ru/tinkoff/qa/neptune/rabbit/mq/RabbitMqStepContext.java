package ru.tinkoff.qa.neptune.rabbit.mq;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.publishJson;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.publishXml;


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

    public RabbitMqStepContext deleteQueue(String queue, ParametersForDelete parametersForDelete){
        return perform(RabbitMqQueueDeleteSupplier.deleteQueue(queue).setParametersForDelete(parametersForDelete));
    }

    public RabbitMqStepContext purgeQueue(String queue){
        return perform(RabbitMqPurgeQueueSupplier.purgeQueue(queue));
    }

    public AMQP.Queue.UnbindOk queueUnbind(RabbitMqQueueUnbindSupplier rabbitQueueUnbindSupplier){
        return rabbitQueueUnbindSupplier.get().apply(this);
    }

    public AMQP.Exchange.UnbindOk ExchangeUnbind(String destination, String source, String routingKey,AdditionalArguments arguments){
        return get(RabbitMqExchangeUnbindSupplier.exchangeUnbind(destination, source, routingKey).setArguments(arguments));
    }

    public AMQP.Queue.DeclareOk queueDeclare(){
        return get(RabbitMqQueueDeclareSupplier.queueDeclare());
    }

    public AMQP.Queue.DeclareOk queueDeclarePassive(String queue){
        return get(RabbitMqQueueDeclareSupplier.queueDeclarePassive(queue));
    }

    public AMQP.Queue.DeclareOk queueDeclare(String queue, ParametersForDeclareQueue params){
        return get(RabbitMqQueueDeclareSupplier.queueDeclare(queue, params));
    }

    public AMQP.Exchange.DeclareOk exchangeDeclarePassive(String exchange){
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

    public RabbitMqStepContext exchangeDelete(String exchange){
        return perform(RabbitMqExchangeDeleteSupplier.exchangeDelete(exchange, false));
    }

    public RabbitMqStepContext exchangeDelete(String exchange, boolean ifUnused){
        return perform(RabbitMqExchangeDeleteSupplier.exchangeDelete(exchange, ifUnused));
    }

    public <T> T readJson(String queue, boolean autoAck, Class<T> classT){
        return get(RabbitMqBasicGetSupplier.read(queue, autoAck, classT).setObjectMapper(new JsonMapper()));
    }

    public <T> T readXml(String queue, boolean autoAck, Class<T> classT){
        return get(RabbitMqBasicGetSupplier.read(queue, autoAck, classT).setObjectMapper(new XmlMapper()));
    }

    public RabbitMqStepContext publishJsonMessage(String exchange, String routingKey , Object toSerialize){
        return perform(publishJson(exchange, routingKey , toSerialize));
    }
    public RabbitMqStepContext publishJsonMessage(String exchange, String routingKey, ParametersForPublish params , Object toSerialize){
        return perform(publishJson(exchange, routingKey , toSerialize).setParams(params));
    }

    public RabbitMqStepContext publishXmlMessage(String exchange, String routingKey , Object toSerialize){
        return perform(publishXml(exchange, routingKey , toSerialize));
    }

    public RabbitMqStepContext publishXmlMessage(String exchange, String routingKey, ParametersForPublish params , Object toSerialize){
        return perform(publishXml(exchange, routingKey , toSerialize).setParams(params));
    }

}
