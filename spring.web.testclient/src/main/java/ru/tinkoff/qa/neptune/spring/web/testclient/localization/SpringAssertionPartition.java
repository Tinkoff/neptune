package ru.tinkoff.qa.neptune.spring.web.testclient.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public final class SpringAssertionPartition extends LocalizationBundlePartition {

    public SpringAssertionPartition() {
        super("spring.assertions", "org.springframework.test.web.reactive.server");
    }
}
