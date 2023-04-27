package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

import java.util.List;

import static org.apache.commons.lang3.LocaleUtils.toLocale;

/**
 * @deprecated should be removed after the end of the supporting spring boot 2.x
 */
@Deprecated(forRemoval = true)
public final class SpringResultMatcherPartitionTemporal extends LocalizationBundlePartition {

    public SpringResultMatcherPartitionTemporal() {
        super("spring.result.matchers.for.compatibility",
                List.of(toLocale("en_US")),
                "fake.package");
    }
}
