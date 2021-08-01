package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

final class GetFromQueue<T> implements Function<RabbitMqStepContext, T>, StepParameterPojo {

    @StepParameter("queue")
    private final String queue;
    @StepParameter("autoAck")
    private final boolean autoAck;

    @StepParameter(value = "Class to deserialize to", doNotReportNullValues = true)
    private final Class<T> cls;

    private final TypeReference<T> typeRef;

    @StepParameter(value = "Type to deserialize to", doNotReportNullValues = true)
    final Type type;

    private DataTransformer transformer;

    private final LinkedList<String> readMessages = new LinkedList<>();

    private GetFromQueue(String queue, boolean autoAck, Class<T> cls, TypeReference<T> typeRef) {
        checkArgument(isNotBlank(queue), "Queue should be defined");
        checkArgument(!(isNull(cls) && isNull(typeRef)), "Any class or type reference should be defined");
        this.queue = queue;
        this.autoAck = autoAck;
        this.cls = cls;
        this.typeRef = typeRef;
        this.type = ofNullable(typeRef).map(TypeReference::getType).orElse(null);
    }

    GetFromQueue(String queue, boolean autoAck, Class<T> cls) {
        this(queue, autoAck, cls, null);
    }

    GetFromQueue(String queue, boolean autoAck, TypeReference<T> typeRef) {
        this(queue, autoAck, null, typeRef);
    }

    @Override
    public T apply(RabbitMqStepContext input) {
        try {
            Channel channel = input.getChannel();
            GetResponse getResponse = channel.basicGet(queue, autoAck);

            var msg = new String(getResponse.getBody(), UTF_8);
            if (!readMessages.contains(msg)) {
                readMessages.addLast(msg);
            }

            if (cls != null) {
                return transformer.deserialize(msg, cls);
            }
            return transformer.deserialize(msg, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void setTransformer(DataTransformer transformer) {
        this.transformer = transformer;
    }

    LinkedList<String> getMessages() {
        return readMessages;
    }
}
