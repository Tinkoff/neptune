package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public final class SpringMockMvcPartition extends LocalizationBundlePartition {

    public SpringMockMvcPartition() {
        super("spring.mock.mvc",
                "org.springframework.test.web.servlet.result",
                "ru.tinkoff.qa.neptune.spring.mock.mvc");
    }
}
