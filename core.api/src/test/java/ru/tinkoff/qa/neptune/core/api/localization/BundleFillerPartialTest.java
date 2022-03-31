package ru.tinkoff.qa.neptune.core.api.localization;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.localization.bundle.extensions.TestExtension;
import ru.tinkoff.qa.neptune.core.api.localization.bundle.partitions.DefinedPackagesPartition;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.instanceOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;

public class BundleFillerPartialTest {

    private final ResourceBundleGenerator.BundleFiller bundleFiller =
            new ResourceBundleGenerator.BundleFiller(new DefinedPackagesPartition(), null, new Properties());

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
                                ru.tinkoff.qa.neptune.core.api.localization.additional.classes.ClassA.class,
                                ru.tinkoff.qa.neptune.core.api.localization.additional.classes.ClassB.class,
                                ru.tinkoff.qa.neptune.core.api.localization.additional.classes.ClassD.class,
                                ru.tinkoff.qa.neptune.core.api.localization.some.classes.ClassA.class,
                                ru.tinkoff.qa.neptune.core.api.localization.some.classes.ClassB.class,
                                ru.tinkoff.qa.neptune.core.api.localization.some.classes.other.classes.ClassD.class,
                                ClassA.class,
                                ClassB.class,
                                ClassD.class
                        ),
                },

                {
                        OtherObjectsBundleFilter.class,
                        iterableInOrder(
                                ru.tinkoff.qa.neptune.core.api.localization.additional.classes.described.ClassC.class,
                                ru.tinkoff.qa.neptune.core.api.localization.some.classes.other.classes.described.ClassC.class
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
