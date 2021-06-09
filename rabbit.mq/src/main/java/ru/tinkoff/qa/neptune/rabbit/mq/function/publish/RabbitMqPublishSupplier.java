package ru.tinkoff.qa.neptune.rabbit.mq.function.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rabbitmq.client.AMQP;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitAMQPProperty.RABBIT_AMQP_PROPERTY;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Publish:")
@MaxDepthOfReporting(0)
public class RabbitMqPublishSupplier extends SequentialActionSupplier<RabbitMqStepContext, RabbitMqStepContext,RabbitMqPublishSupplier> {
    private final String exchange;
    private final String routingKey;
    private final byte[] body;
    private final AMQP.BasicProperties.Builder propertyBuilder;
    private  ParametersForPublish params;

    public RabbitMqPublishSupplier(String exchange, String routingKey, String body) {
        super();
        this.exchange = exchange;
        this.routingKey = routingKey;
        checkNotNull(body);
        this.body = body.getBytes(UTF_8);
        performOn(rabbitStepContext -> rabbitStepContext);
        propertyBuilder = ofNullable(RABBIT_AMQP_PROPERTY.get())
                .orElseGet(AMQP.BasicProperties.Builder::new);
    }

    public RabbitMqPublishSupplier setParams(ParametersForPublish params) {
        this.params = params;
        return this;
    }

    @Description("a message.\r\n" +
            "Params:\r\n" +
            "exchange – {exchange}\r\n" +
            "routingKey – {routingKey}")
    public static RabbitMqPublishSupplier publish(@DescriptionFragment("exchange") String exchange, @DescriptionFragment("routingKey") String routingKey, ObjectMapper mapper, Object toSerialize){
        try {
            return new RabbitMqPublishSupplier(exchange, routingKey, mapper.writeValueAsString(toSerialize));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static RabbitMqPublishSupplier publishJson(String exchange, String routingKey, Object toSerialize){
        return publish(exchange, routingKey,new JsonMapper(), toSerialize);
    }

    public static RabbitMqPublishSupplier publishXml(String exchange, String routingKey, Object toSerialize){
        return publish(exchange, routingKey,new XmlMapper(), toSerialize);
    }


    @Override
    protected void howToPerform(RabbitMqStepContext value) {
        var channel = value.getChannel();
        var props = propertyBuilder.build();
        try{
            if(params == null){
                channel.basicPublish(exchange,routingKey,false,false,props, body);
            }
            channel.basicPublish(exchange,routingKey,params.isMandatory(),params.isImmediate(),props, body);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public RabbitMqPublishSupplier contentType(String contentType) {
        propertyBuilder.contentType(contentType);
        return this;
    }

    public RabbitMqPublishSupplier setContentEncoding(String contentEncoding) {
        propertyBuilder.contentEncoding(contentEncoding);
        return this;
    }

    public RabbitMqPublishSupplier headers(Map<String, Object> headers) {
        propertyBuilder.headers(headers);
        return this;
    }

    public RabbitMqPublishSupplier deliveryMode(Integer deliveryMode) {
        propertyBuilder.deliveryMode(deliveryMode);
        return this;
    }

    public RabbitMqPublishSupplier priority(Integer priority) {
        propertyBuilder.priority(priority);
        return this;
    }

    public RabbitMqPublishSupplier correlationId(String correlationId) {
        propertyBuilder.correlationId(correlationId);
        return this;
    }

    public RabbitMqPublishSupplier replyTo(String replyTo) {
        propertyBuilder.replyTo(replyTo);
        return this;
    }

    public RabbitMqPublishSupplier expiration(String expiration) {
        propertyBuilder.expiration(expiration);
        return this;
    }

    public RabbitMqPublishSupplier messageId(String messageId) {
        propertyBuilder.messageId(messageId);
        return this;
    }

    public RabbitMqPublishSupplier timestamp(Date timestamp) {
        propertyBuilder.timestamp(timestamp);
        return this;
    }

    public RabbitMqPublishSupplier type(String type) {
        propertyBuilder.type(type);
        return this;
    }

    public RabbitMqPublishSupplier userId(String userId) {
        propertyBuilder.userId(userId);
        return this;
    }

    public RabbitMqPublishSupplier appId(String appId) {
        propertyBuilder.appId(appId);
        return this;
    }

    public RabbitMqPublishSupplier clusterId(String clusterId) {
        propertyBuilder.clusterId(clusterId);
        return this;
    }
}
