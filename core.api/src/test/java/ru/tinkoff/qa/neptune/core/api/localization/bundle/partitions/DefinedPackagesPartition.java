package ru.tinkoff.qa.neptune.core.api.localization.bundle.partitions;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public class DefinedPackagesPartition extends LocalizationBundlePartition {

    public DefinedPackagesPartition() {
        super("defined.packages",
                "ru.tinkoff.qa.neptune.core.api.localization.additional",
                "ru.tinkoff.qa.neptune.core.api.localization.some");
    }
}
