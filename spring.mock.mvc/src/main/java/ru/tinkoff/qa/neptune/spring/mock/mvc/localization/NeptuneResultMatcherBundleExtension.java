package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import ru.tinkoff.qa.neptune.core.api.localization.BindToPartition;
import ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@BindToPartition("spring.result.matchers")
@BindToPartition("spring.mock.mvc")
public class NeptuneResultMatcherBundleExtension extends AbstractResultMatcherBundleExtension {

    public NeptuneResultMatcherBundleExtension() {
        super(of(NeptuneForwardedUrlResultMatchers.class,
                        NeptuneJsonPathResultMatchers.class,
                        NeptuneRedirectedUrlResultMatchers.class,
                        NeptuneXpathResultMatchers.class,
                        NeptuneExceptionResultMatchers.class).sorted(comparing(Class::getName)).collect(toList()),
                "NEPTUNE RESULT MATCHERS");
    }
}
