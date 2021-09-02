package ru.tinkoff.qa.neptune.rabbit.mq.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public class RabbitMqLocalizationPartition extends LocalizationBundlePartition {

    public RabbitMqLocalizationPartition() {
        super("rabbit.mq", "ru.tinkoff.qa.neptune.rabbit.mq");
    }
}
