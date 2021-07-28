package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.kafka.functions.send.ParametersForSend;

import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.send;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultDataTransformer.KAFKA_DEFAULT_DATA_TRANSFORMER;


@CreateWith(provider = KafkaParameterProvider.class)
@SuppressWarnings("unchecked")
public class KafkaStepContext extends Context<KafkaStepContext> {
    private static final KafkaStepContext context = getInstance(KafkaStepContext.class);
    private final KafkaProducer<?, ?> producer;
    private final KafkaConsumer<?, ?> consumer;

    public KafkaStepContext(KafkaProducer<?, ?> producer, KafkaConsumer<?, ?> consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public static KafkaStepContext kafka() {
        return context;
    }

    public <K, V> KafkaProducer<K, V> getProducer() {
        return (KafkaProducer<K, V>) producer;
    }

    public <K, V> KafkaConsumer<K, V> getConsumer() {
        return (KafkaConsumer<K, V>) consumer;
    }

    public KafkaStepContext sendMessage(String topic, Object value, DataTransformer dataTransformer) {
        return perform(send(topic, value, dataTransformer));
    }

    public KafkaStepContext sendMessage(String topic, Object value, Callback callback, DataTransformer dataTransformer) {
        return perform(send(topic, value, dataTransformer).setCallback(callback));
    }

    public KafkaStepContext sendMessage(String topic, Object value) {
        return sendMessage(topic, value, KAFKA_DEFAULT_DATA_TRANSFORMER.get());
    }

    public KafkaStepContext sendMessage(String topic, Object value, Callback callback) {
        return sendMessage(topic, value, callback, KAFKA_DEFAULT_DATA_TRANSFORMER.get());
    }

    public KafkaStepContext sendMessage(String topic, Object value, ParametersForSend parametersForSend) {
        return perform(send(topic, value, KAFKA_DEFAULT_DATA_TRANSFORMER.get()).setParametersForSend(parametersForSend));
    }

    public KafkaStepContext sendMessage(String topic, Object value, ParametersForSend parametersForSend, DataTransformer dataTransformer) {
        return perform(send(topic, value, dataTransformer).setParametersForSend(parametersForSend));
    }

    public KafkaStepContext sendMessage(String topic, Object value, ParametersForSend parametersForSend, Callback callback) {
        return perform(send(topic, value, KAFKA_DEFAULT_DATA_TRANSFORMER.get()).setCallback(callback).setParametersForSend(parametersForSend));
    }

    public KafkaStepContext sendMessage(String topic, Object value, ParametersForSend parametersForSend, Callback callback, DataTransformer dataTransformer) {
        return perform(send(topic, value, dataTransformer).setCallback(callback).setParametersForSend(parametersForSend));
    }
}
