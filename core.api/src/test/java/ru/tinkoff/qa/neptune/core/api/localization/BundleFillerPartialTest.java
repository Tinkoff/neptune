package ru.tinkoff.qa.neptune.core.api.localization;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.ClassA;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.ClassB;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.ClassD;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.additional.classes.described.ClassC;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.bundle.extensions.TestExtension;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.bundle.partitions.DefinedPackagesPartition;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.instanceOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;

public class BundleFillerPartialTest {

    private final BundleFiller bundleFiller =
            new BundleFiller(new DefinedPackagesPartition(), null, new Properties());

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {StepBundleFilter.class, emptyIterable()},

                {
                        CriteriaBundleFilter.class,
                        emptyIterable()
                },

                {
                        AttachmentsBundleFilter.class,
                        emptyIterable()
                },

                {
                        MatchersBundleFilter.class,
                        emptyIterable()
                },

                {
                        MismatchDescriptionBundleFilter.class,
                        emptyIterable()
                },

                {
                        MatchedObjectsBundleFilter.class,
                        emptyIterable()
                },

                {
                        ParameterPojoBundleFilter.class,
                        emptyIterable()
                },

                {
                        TestExtension.class,
                        iterableInOrder(
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.additional.classes.ClassA.class,
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.additional.classes.ClassB.class,
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.additional.classes.ClassD.class,
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.some.classes.ClassA.class,
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.some.classes.ClassB.class,
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.some.classes.other.classes.ClassD.class,
                                ClassA.class,
                                ClassB.class,
                                ClassD.class
                        ),
                },

                {
                        OtherObjectsBundleFilter.class,
                        iterableInOrder(
                                ClassC.class,
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.some.classes.other.classes.described.ClassC.class
                        ),
                },
        };
    }

    @Test
    public void extensionTest() {
        assertThat(bundleFiller.getBundleFillerExtensions(),
                iterableInOrder(
                        instanceOf(StepBundleFilter.class),
                        instanceOf(CriteriaBundleFilter.class),
                        instanceOf(AttachmentsBundleFilter.class),
                        instanceOf(MatchersBundleFilter.class),
                        instanceOf(MismatchDescriptionBundleFilter.class),
                        instanceOf(MatchedObjectsBundleFilter.class),
                        instanceOf(ParameterPojoBundleFilter.class),
                        instanceOf(TestExtension.class),
                        instanceOf(OtherObjectsBundleFilter.class)
                ));
    }

    @Test(dependsOnMethods = "extensionTest", dataProvider = "data")
    public void checkProcessedClasses(Class<? extends BundleFillerExtension> extensionClass,
                                      Matcher<? super Iterable<? extends Class<?>>> matcher) {
        var extension = bundleFiller
                .getBundleFillerExtensions()
                .stream()
                .filter(e -> e.getClass().equals(extensionClass))
                .findFirst()
                .orElseThrow();

        assertThat(extension.getProcessedClasses(),
                matcher);
    }
}
