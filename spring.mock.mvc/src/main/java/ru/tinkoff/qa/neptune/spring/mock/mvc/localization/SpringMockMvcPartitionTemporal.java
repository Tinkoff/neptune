package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

/**
 * @deprecated should be removed after the end of the supporting spring boot 2.x
 */
@Deprecated(forRemoval = true)
public final class SpringMockMvcPartitionTemporal extends LocalizationBundlePartition {

    public SpringMockMvcPartitionTemporal() {
        super("spring.mock.mvc.for.compatibility",
            "fake.package");
    }
}
