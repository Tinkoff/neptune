package ru.tinkoff.qa.neptune.rabbit.mq.function.publish;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;
import ru.tinkoff.qa.neptune.rabbit.mq.captors.MessageCaptor;
import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.getCaptors;
import static ru.tinkoff.qa.neptune.rabbit.mq.GetChannel.getChannel;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_ROUTING_KEY_NAME;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAMQPProperty.RABBIT_AMQP_PROPERTY;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultDataTransformer.RABBIT_MQ_DEFAULT_DATA_TRANSFORMER;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Publish:")
@MaxDepthOfReporting(0)
@Description("message")
@SuppressWarnings("unchecked")
public abstract class RabbitMqPublishSupplier<T extends RabbitMqPublishSupplier<T>> extends SequentialActionSupplier<RabbitMqStepContext, Channel, T> {

    private final Map<String, Object> headers = new LinkedHashMap<>();
    String body;
    @StepParameter("exchange")
    private String exchange = "";
    @StepParameter("routingKey")
    private String routingKey = "";
    @StepParameter("mandatory")
    private boolean mandatory;
    @StepParameter("immediate")
    private boolean immediate;

    private final AMQP.BasicProperties.Builder propertyBuilder;

    private RabbitMqPublishSupplier() {
        super();
        performOn(getChannel());
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

    /**
     * Publishes an object serialized into text message
     *
     * @param toSerialize is an object to be serialized into string message
     * @return a new instance of {@link RabbitMqPublishSupplier}
     */
    public static RabbitMqPublishSupplier.Mapped rabbitSerializedMessage(Object toSerialize) {
        return new Mapped(toSerialize);
    }

    /**
     * Publishes a text message
     *
     * @param toPublish is a text to publish
     * @param charset   is a required charset
     * @return new instance of {@link RabbitMqPublishSupplier}
     */
    public static RabbitMqPublishSupplier.StringMessage rabbitTextMessage(String toPublish, Charset charset) {
        return new StringMessage(toPublish, charset);
    }

    /**
     * Publishes a text message
     *
     * @param toPublish is a text to publish
     * @return new instance of {@link RabbitMqPublishSupplier}
     */
    public static RabbitMqPublishSupplier.StringMessage rabbitTextMessage(String toPublish) {
        return rabbitTextMessage(toPublish, UTF_8);
    }

    @Override
    protected void howToPerform(Channel value) {
        var props = propertyBuilder.build();
        try {
            value.basicPublish(exchange, routingKey, mandatory, immediate, props,
                body.getBytes());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public T contentType(String contentType) {
        propertyBuilder.contentType(contentType);
        return (T) this;
    }

    public T contentEncoding(String contentEncoding) {
        propertyBuilder.contentEncoding(contentEncoding);
        return (T) this;
    }

    public T header(String name, Object value) {
        headers.put(name, value);
        propertyBuilder.headers(headers);
        return (T) this;
    }

    public T deliveryMode(Integer deliveryMode) {
        propertyBuilder.deliveryMode(deliveryMode);
        return (T) this;
    }

    public T priority(Integer priority) {
        propertyBuilder.priority(priority);
        return (T) this;
    }

    public T correlationId(String correlationId) {
        propertyBuilder.correlationId(correlationId);
        return (T) this;
    }

    public T replyTo(String replyTo) {
        propertyBuilder.replyTo(replyTo);
        return (T) this;
    }

    public T expiration(String expiration) {
        propertyBuilder.expiration(expiration);
        return (T) this;
    }

    public T messageId(String messageId) {
        propertyBuilder.messageId(messageId);
        return (T) this;
    }

    public T timestamp(Date timestamp) {
        propertyBuilder.timestamp(timestamp);
        return (T) this;
    }

    public T type(String type) {
        propertyBuilder.type(type);
        return (T) this;
    }

    public T userId(String userId) {
        propertyBuilder.userId(userId);
        return (T) this;
    }

    public T appId(String appId) {
        propertyBuilder.appId(appId);
        return (T) this;
    }

    public T clusterId(String clusterId) {
        propertyBuilder.clusterId(clusterId);
        return (T) this;
    }

    /**
     * Makes the publishing mandatory
     *
     * @return self-reference
     */
    public T mandatory() {
        this.mandatory = true;
        return (T) this;
    }

    /**
     * Makes the publishing immediate
     *
     * @return self-reference
     */
    public T immediate() {
        this.immediate = true;
        return (T) this;
    }

    /**
     * Defines exchange to publish.
     *
     * @param exchange is the name of exchange
     * @return self-reference
     */
    public T exchange(String exchange) {
        this.exchange = exchange;
        return (T) this;
    }

    /**
     * Defines the necessity to publish to default named exchange.
     * Value of the {@link RabbitMQRoutingProperties#DEFAULT_EXCHANGE_NAME} is used
     * as exchange.
     *
     * @return self-reference
     */
    public T toDefaultExchange() {
        return exchange(DEFAULT_EXCHANGE_NAME.get());
    }

    /**
     * Defines routing key.
     *
     * @param routingKey is the name of routing key
     * @return self-reference
     */
    public T routingKey(String routingKey) {
        this.routingKey = routingKey;
        return (T) this;
    }

    /**
     * Defines the necessity to use default routing key.
     * Value of the {@link RabbitMQRoutingProperties#DEFAULT_ROUTING_KEY_NAME} is used
     * as routing key.
     *
     * @return self-reference
     */
    public T useDefaultRoutingKey() {
        return routingKey(DEFAULT_ROUTING_KEY_NAME.get());
    }

    @Override
    protected void onStart(RabbitMqStepContext rabbitMqStepContext) {
        catchValue(body, getCaptors(new Class[]{MessageCaptor.class}));
    }

    public static final class Mapped extends RabbitMqPublishSupplier<Mapped> {

        private final Object toPublish;
        private DataTransformer transformer;

        private Mapped(Object toPublish) {
            super();
            checkNotNull(toPublish);
            this.toPublish = toPublish;
        }

        public Mapped withDataTransformer(DataTransformer dataTransformer) {
            this.transformer = dataTransformer;
            return this;
        }

        @Override
        protected void onStart(RabbitMqStepContext rabbitMqStepContext) {
            var transformer = ofNullable(this.transformer)
                    .orElseGet(RABBIT_MQ_DEFAULT_DATA_TRANSFORMER);
            checkState(nonNull(transformer), "Data transformer is not defined. Please invoke "
                    + "the '#withDataTransformer(DataTransformer)' method or define '"
                    + RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.getName()
                    + "' property/env variable");
            body = transformer.serialize(toPublish);
            super.onStart(rabbitMqStepContext);
        }
    }

    public static final class StringMessage extends RabbitMqPublishSupplier<StringMessage> {

        private StringMessage(String message, Charset charset) {
            checkNotNull(charset);
            checkArgument(isNotBlank(message), "Message to publish should not be blank");
            body = new String(message.getBytes(), charset);
        }
    }
}
