package ru.tinkoff.qa.neptune.rabbit.mq.function.publish;

import com.rabbitmq.client.AMQP;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;
import ru.tinkoff.qa.neptune.rabbit.mq.captors.MessageCaptor;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAMQPProperty.RABBIT_AMQP_PROPERTY;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Publish:")
@MaxDepthOfReporting(0)
@Description("message. exchange = '{exchange}', routingKey = '{routingKey}'")
public class RabbitMqPublishSupplier extends SequentialActionSupplier<RabbitMqStepContext, RabbitMqStepContext, RabbitMqPublishSupplier> {

    @DescriptionFragment("exchange")
    private final String exchange;

    @DescriptionFragment("routingKey")
    private final String routingKey;

    private final String body;

    private final AMQP.BasicProperties.Builder propertyBuilder;
    private ParametersForPublish params;

    public RabbitMqPublishSupplier(String exchange, String routingKey, String body) {
        super();
        this.exchange = exchange;
        this.routingKey = routingKey;
        checkNotNull(body);
        this.body = body;
        performOn(rabbitStepContext -> rabbitStepContext);
        propertyBuilder = ofNullable(RABBIT_AMQP_PROPERTY.get())
                .orElseGet(AMQP.BasicProperties.Builder::new);
    }

    @Override
    public Map<String, String> getParameters() {
        var map = super.getParameters();
        var props = propertyBuilder.build();
        ofNullable(props.getAppId()).ifPresent(s -> map.put("appId", s));
        ofNullable(props.getContentType()).ifPresent(s -> map.put("contentType", s));
        ofNullable(props.getContentEncoding()).ifPresent(s -> map.put("contentEncoding", s));
        ofNullable(props.getDeliveryMode()).ifPresent(s -> map.put("deliveryMode", String.valueOf(s)));
        ofNullable(props.getPriority()).ifPresent(s -> map.put("priority", String.valueOf(s)));
        ofNullable(props.getCorrelationId()).ifPresent(s -> map.put("correlationId", s));
        ofNullable(props.getReplyTo()).ifPresent(s -> map.put("replyTo", s));
        ofNullable(props.getExpiration()).ifPresent(s -> map.put("expiration", s));
        ofNullable(props.getMessageId()).ifPresent(s -> map.put("messageId", s));
        ofNullable(props.getType()).ifPresent(s -> map.put("type", s));
        ofNullable(props.getUserId()).ifPresent(s -> map.put("userId", s));
        ofNullable(props.getClusterId()).ifPresent(s -> map.put("clusterId", s));
        ofNullable(props.getHeaders()).ifPresent(s -> map.put("headers", s.toString()));
        return map;
    }

    public RabbitMqPublishSupplier setParams(ParametersForPublish params) {
        this.params = params;
        return this;
    }

    public static RabbitMqPublishSupplier publish(String exchange,
                                                  String routingKey,
                                                  Object toSerialize,
                                                  DataTransformer mapper) {
        return new RabbitMqPublishSupplier(exchange, routingKey, mapper.serialize(toSerialize));
    }

    @Override
    protected void howToPerform(RabbitMqStepContext value) {
        var channel = value.getChannel();
        var props = propertyBuilder.build();
        try {
            if (params == null) {
                channel.basicPublish(exchange, routingKey, false, false, props,
                        body.getBytes(UTF_8));
            } else {
                channel.basicPublish(exchange, routingKey, params.isMandatory(), params.isImmediate(), props,
                        body.getBytes(UTF_8));
            }
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

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(RabbitMqStepContext rabbitMqStepContext) {
        catchValue(body, createCaptors(new Class[] {MessageCaptor.class}));
    }
}
