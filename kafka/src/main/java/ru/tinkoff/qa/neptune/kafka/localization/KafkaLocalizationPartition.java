package ru.tinkoff.qa.neptune.kafka.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public class KafkaLocalizationPartition extends LocalizationBundlePartition {

    public KafkaLocalizationPartition() {
        super("kafka", "ru.tinkoff.qa.neptune.kafka");
    }
}
