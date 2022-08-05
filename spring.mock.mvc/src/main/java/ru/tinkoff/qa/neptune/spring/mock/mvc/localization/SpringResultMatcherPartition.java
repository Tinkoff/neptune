package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

import java.util.List;

import static org.apache.commons.lang3.LocaleUtils.toLocale;

public final class SpringResultMatcherPartition extends LocalizationBundlePartition {

    public SpringResultMatcherPartition() {
        super("spring.result.matchers",
                List.of(toLocale("en_US")),
                "org.springframework.test.web.servlet.result",
                "ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers");
    }
}
