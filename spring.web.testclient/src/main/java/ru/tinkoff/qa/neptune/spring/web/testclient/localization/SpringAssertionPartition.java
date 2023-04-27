package ru.tinkoff.qa.neptune.spring.web.testclient.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

import java.util.List;

import static org.apache.commons.lang3.LocaleUtils.toLocale;

public final class SpringAssertionPartition extends LocalizationBundlePartition {

    public SpringAssertionPartition() {
        super("spring.assertions",
            List.of(toLocale("en_US")),
            "org.springframework.test.web.reactive.server");
    }
}
