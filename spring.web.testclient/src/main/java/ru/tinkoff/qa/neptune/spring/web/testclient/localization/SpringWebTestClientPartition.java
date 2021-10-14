package ru.tinkoff.qa.neptune.spring.web.testclient.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public final class SpringWebTestClientPartition extends LocalizationBundlePartition {

    public SpringWebTestClientPartition() {
        super("spring.web.testclient",
                "ru.tinkoff.qa.neptune.spring.web.testclient",
                "org.springframework.test.web.reactive.server");
    }
}
