package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public final class SpringResultMatcherPartition extends LocalizationBundlePartition {

    public SpringResultMatcherPartition() {
        super("spring.result.matchers", "org.springframework.test.web.servlet.result");
    }
}
