package ru.tinkoff.qa.neptune.spring.data.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public final class SpringDataPartition extends LocalizationBundlePartition {

    public SpringDataPartition() {
        super("spring.data",
                "ru.tinkoff.qa.neptune.spring.data");
    }
}
