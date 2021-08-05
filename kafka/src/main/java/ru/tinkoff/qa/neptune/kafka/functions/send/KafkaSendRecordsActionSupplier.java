package ru.tinkoff.qa.neptune.kafka.functions.send;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;
import ru.tinkoff.qa.neptune.kafka.captors.MessageCaptor;

import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Send:")
@MaxDepthOfReporting(0)
@SuppressWarnings("unchecked")
public class KafkaSendRecordsActionSupplier<K, V> extends SequentialActionSupplier<KafkaStepContext, KafkaStepContext, KafkaSendRecordsActionSupplier<K, V>> {
    private final String topic;
    @StepParameter("value")
    private final Object value;

    private Callback callback;
    private ParametersForSend parametersForSend;
    private DataTransformer dataTransformer;

    public KafkaSendRecordsActionSupplier(String topic, Object value, DataTransformer dataTransformer) {
        super();
        this.topic = topic;
        this.value = value;
        this.dataTransformer = dataTransformer;
        performOn(kafkaStepContext -> kafkaStepContext);
    }

    public KafkaSendRecordsActionSupplier<?, ?> setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public KafkaSendRecordsActionSupplier<?, ?> setParametersForSend(ParametersForSend parametersForSend) {
        this.parametersForSend = parametersForSend;
        return this;
    }

    @Description("message to topic '{topic}'.")
    public static KafkaSendRecordsActionSupplier<?, ?> send(@DescriptionFragment("topic") String topic,
                                                            Object value,
                                                            DataTransformer dataTransformer) {
        return new KafkaSendRecordsActionSupplier<>(topic, value, dataTransformer);
    }

    @Override
    protected void howToPerform(KafkaStepContext kafkaStepContext) {
        KafkaProducer<K, V> producer = kafkaStepContext.getProducer();

        var records = new ProducerRecord<>(
                topic,
                parametersForSend.getPartition(),
                parametersForSend.getTimestamp(),
                parametersForSend.getKey(),
                dataTransformer.serialize(value),
                parametersForSend.getHeaders());

        if (callback == null) {
            producer.send((ProducerRecord<K, V>) records);
        } else {
            producer.send((ProducerRecord<K, V>) records, callback);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(KafkaStepContext kafkaStepContext) {
        catchValue(dataTransformer.serialize(value), createCaptors(new Class[]{MessageCaptor.class}));
    }
}
