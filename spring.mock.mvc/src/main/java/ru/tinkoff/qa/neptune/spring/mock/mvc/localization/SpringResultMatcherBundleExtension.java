package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import ru.tinkoff.qa.neptune.core.api.localization.BindToPartition;

import static ru.tinkoff.qa.neptune.spring.mock.mvc.localization.IncludeSpringResultMatchers.CLASSES;

@BindToPartition("spring.result.matchers")
@BindToPartition("spring.mock.mvc")
public class SpringResultMatcherBundleExtension extends AbstractResultMatcherBundleExtension {

    public SpringResultMatcherBundleExtension() {
        super(CLASSES,
                "SPRING RESULT MATCHERS",
                new IncludeSpringResultMatchers());
    }
}
