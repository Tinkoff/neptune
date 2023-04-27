package ru.tinkoff.qa.neptune.spring.web.testclient.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

/**
 * @deprecated should be removed after the end of the supporting spring boot 2.x
 */
@Deprecated(forRemoval = true)
public final class SpringWebTestClientPartitionTemporal extends LocalizationBundlePartition {

    public SpringWebTestClientPartitionTemporal() {
        super("spring.web.testclient.for.compatibility",
                "ru.tinkoff.qa.neptune.spring.web.testclient.for.compatibility",
                "fake.package");
    }
}
