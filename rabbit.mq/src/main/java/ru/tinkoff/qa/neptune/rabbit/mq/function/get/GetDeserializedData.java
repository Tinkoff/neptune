package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.GetResponse;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultDataTransformer.RABBIT_MQ_DEFAULT_DATA_TRANSFORMER;

final class GetDeserializedData<T> implements Function<List<GetResponse>, List<T>>, StepParameterPojo {

    private final Charset charset;

    @StepParameter(value = "Class to deserialize to", doNotReportNullValues = true)
    private final Class<T> cls;

    private final TypeReference<T> typeRef;

    @StepParameter(value = "Type to deserialize to", doNotReportNullValues = true)
    final Type type;

    private DataTransformer transformer;

    private final LinkedList<String> readMessages = new LinkedList<>();

    private GetDeserializedData(Class<T> cls, TypeReference<T> typeRef, Charset charset) {
        checkArgument(!(isNull(cls) && isNull(typeRef)), "Any class or type reference should be defined");
        this.cls = cls;
        this.typeRef = typeRef;
        this.type = ofNullable(typeRef).map(TypeReference::getType).orElse(null);
        this.charset = charset;
    }


    GetDeserializedData(Class<T> cls) {
        this(cls, null, UTF_8);
    }

    GetDeserializedData(TypeReference<T> typeRef) {
        this(null, typeRef, UTF_8);
    }

    static GetDeserializedData<String> getStringResult(Charset charset) {
        return new GetDeserializedData<>(String.class, null, charset);
    }

    @Override
    public List<T> apply(List<GetResponse> responses) {
        var dataTransformer = ofNullable(this.transformer)
                .orElseGet(RABBIT_MQ_DEFAULT_DATA_TRANSFORMER);
        checkState(nonNull(dataTransformer), "Data transformer is not defined. Please invoke "
                + "the '#withDataTransformer(DataTransformer)' method or define '"
                + RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.getName()
                + "' property/env variable");

        return responses.stream().map(getResponse -> {
            try {
                var msg = new String(getResponse.getBody(), charset);
                if (!readMessages.contains(msg)) {
                    readMessages.addLast(msg);
                }

                if (cls != null) {
                    return dataTransformer.deserialize(msg, cls);
                }
                return dataTransformer.deserialize(msg, typeRef);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(toList());
    }

    void setTransformer(DataTransformer transformer) {
        this.transformer = transformer;
    }

    public LinkedList<String> getMessages() {
        return readMessages;
    }
}
