package ru.tinkoff.qa.neptune.kafka.functions.poll;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.kafka.properties.DefaultDataTransformers.KAFKA_DEFAULT_DATA_TRANSFORMER;

@Deprecated(forRemoval = true)
final class Conversion<K, V, M, R> implements Function<ConsumerRecord<K, V>, R> {

    private final Function<M, R> conversion;
    private final Class<M> cls;
    private final TypeReference<M> typeRef;
    private DataTransformer transformer;

    Conversion(Function<M, R> conversion, Class<M> cls, TypeReference<M> typeRef) {
        this.conversion = conversion;
        this.cls = cls;
        this.typeRef = typeRef;
    }

    @Override
    public R apply(ConsumerRecord<K, V> kvConsumerRecord) {
        var value = kvConsumerRecord.value();
        if (isNull(value)) {
            return null;
        }

        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Values of class " + value.getClass().getName() + " are not supported. Only strings");
        }

        var newTransformer = ofNullable(this.transformer)
            .orElseGet(KAFKA_DEFAULT_DATA_TRANSFORMER);
        checkState(nonNull(newTransformer), "Data transformer is not defined. Please invoke "
            + "the '#withDataTransformer(DataTransformer)' method or define '"
            + KAFKA_DEFAULT_DATA_TRANSFORMER.getName()
            + "' property/env variable");

        if (isNull(typeRef)) {
            return conversion.apply(newTransformer.deserialize((String) value, cls));
        }

        return conversion.apply(newTransformer.deserialize((String) value, typeRef));
    }

    void setTransformer(DataTransformer transformer) {
        checkNotNull(transformer);
        this.transformer = transformer;
    }
}
