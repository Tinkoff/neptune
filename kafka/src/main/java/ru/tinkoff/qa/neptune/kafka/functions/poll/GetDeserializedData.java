package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

final class GetDeserializedData<T> implements Function<List<ConsumerRecord<String, String>>, List<T>>, StepParameterPojo {

    @StepParameter(value = "Class to deserialize to", doNotReportNullValues = true)
    private final Class<T> cls;

    private final TypeReference<T> typeRef;

    @StepParameter(value = "Type to deserialize to", doNotReportNullValues = true)
    final Type type;

    private DataTransformer transformer;

    GetDeserializedData(Class<T> cls, TypeReference<T> typeRef) {
        checkArgument(!(isNull(cls) && isNull(typeRef)), "Any class or type reference should be defined");
        this.cls = cls;
        this.typeRef = typeRef;
        this.type = ofNullable(typeRef).map(TypeReference::getType).orElse(null);

    }

    GetDeserializedData(Class<T> cls) {
        this(cls, null);
    }

    GetDeserializedData(TypeReference<T> typeRef, String... topics) {
        this(null, typeRef);
    }

    @Override
    public List<T> apply(List<ConsumerRecord<String, String>> records) {
        return records
                .stream()
                .map(record -> {
                    try {
                        T t;
                        var value = record.value();
                        if (cls != null) {
                            t = transformer.deserialize(value, cls);
                        } else {
                            t = transformer.deserialize(value, typeRef);
                        }

                        return t;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(toList());
    }

    void setTransformer(DataTransformer transformer) {
        this.transformer = transformer;
    }
}
