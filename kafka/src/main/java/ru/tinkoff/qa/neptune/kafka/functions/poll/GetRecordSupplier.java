package ru.tinkoff.qa.neptune.kafka.functions.poll;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.kafka.KafkaStepContext;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.kafkaArray;

public class GetRecordSupplier extends SequentialGetStepSupplier.GetListStepSupplier<KafkaStepContext, List<ConsumerRecord<String, String>>, ConsumerRecord<String, String>, GetRecordSupplier> {

    final GetRecords function;

    protected GetRecordSupplier(GetRecords originalFunction) {
        super(originalFunction);
        this.function = originalFunction;
    }

    @Description("{description}")
    public static GetRecordSupplier consumerRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            String... topics) {
        checkArgument(isNotBlank(description), "Description should be defined");
        return new GetRecordSupplier(new GetRecords(topics));
    }

    public <T> KafkaPollArraySupplier.Mapped<T> thenGetArray(
            String description,
            Class<T> classT,
            String... topics) {
        return kafkaArray(description, classT, topics).from(this);
    }

    public void thenGetList() {

    }

    public void thenGetItem() {

    }
}
