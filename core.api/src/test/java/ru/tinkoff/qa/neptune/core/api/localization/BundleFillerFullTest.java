package ru.tinkoff.qa.neptune.core.api.localization;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.localization.bundle.extensions.TestExtension;
import ru.tinkoff.qa.neptune.core.api.localization.bundle.partitions.AllPackagePartition;
import ru.tinkoff.qa.neptune.core.api.localization.hamcrest.SomeMatchedObject;
import ru.tinkoff.qa.neptune.core.api.localization.hamcrest.SomeMatcher;
import ru.tinkoff.qa.neptune.core.api.localization.hamcrest.SomeMismatch;
import ru.tinkoff.qa.neptune.core.api.localization.steps.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.iterableIncludesInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItems;

public class BundleFillerFullTest {

    private final ResourceBundleGenerator.BundleFiller bundleFiller =
            new ResourceBundleGenerator.BundleFiller(new AllPackagePartition(), null, new Properties());

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {StepBundleFilter.class, iterableIncludesInOrder(
                        SomeChildSequentialActionWithAnnotations.class,
                        SomeChildSequentialActionWithoutAnnotations.class,
                        SomeChildSequentialStepSupplierWithAnnotations.class,
                        SomeChildSequentialStepSupplierWithoutAnnotations.class,
                        SomeSequentialAction.class,
                        SomeSequentialActionWithoutAnnotations.class,
                        SomeSequentialStepSupplier.class,
                        SomeSequentialStepSupplierWithoutAnnotations.class
                )},

                {
                        CriteriaBundleFilter.class,
                        iterableHasItems(1, SpecificCriteria.class)
                },

                {
                        AttachmentsBundleFilter.class,
                        iterableInOrder(SomeCaptor.class,
                                SomeChildCaptorWithAnnotations.class,
                                SomeChildCaptorWithoutAnnotations.class,
                                SomeNotDescribedCaptor.class)
                },

                {
                        MatchersBundleFilter.class,
                        iterableInOrder(SomeMatcher.class)
                },

                {
                        MismatchDescriptionBundleFilter.class,
                        iterableInOrder(SomeMismatch.class),
                },

                {
                        MatchedObjectsBundleFilter.class,
                        iterableInOrder(SomeMatchedObject.class),
                },

                {
                        ParameterPojoBundleFilter.class,
                        iterableInOrder(AggregatedParams.class),
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
                                ru.tinkoff.qa.neptune.core.api.localization.ClassA.class,
                                ru.tinkoff.qa.neptune.core.api.localization.ClassB.class,
                                ru.tinkoff.qa.neptune.core.api.localization.ClassD.class
                        ),
                },

                {
                        OtherObjectsBundleFilter.class,
                        iterableInOrder(
                                ru.tinkoff.qa.neptune.core.api.localization.ClassC.class,
                                ru.tinkoff.qa.neptune.core.api.localization.additional.classes.described.ClassC.class,
                                ru.tinkoff.qa.neptune.core.api.localization.some.classes.other.classes.described.ClassC.class
                        ),
                },
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {StepBundleFilter.class, iterableIncludesInOrder(
                        "class ru.tinkoff.qa.neptune.core.api.localization.BasicLocalizationTest$TestGetStepSupplier.criteria",
                        "class ru.tinkoff.qa.neptune.core.api.localization.OtherLocalisationEngineTest$GetStepSupplier.from",
                        "class ru.tinkoff.qa.neptune.core.api.localization.TranslateByResourceBundleTest$GetStepSupplier.criteria",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialActionWithAnnotations.imperative",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialActionWithAnnotations.performOn",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialActionWithAnnotations.c",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialStepSupplierWithAnnotations.imperative",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialStepSupplierWithAnnotations.from",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialStepSupplierWithAnnotations.pollingTime",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialStepSupplierWithAnnotations.timeOut",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialStepSupplierWithAnnotations.criteria",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialStepSupplierWithAnnotations.resultDescription",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialStepSupplierWithAnnotations.errorMessageStartingOnEmptyResult",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeChildSequentialStepSupplierWithAnnotations.c",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialAction.imperative",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialAction.performOn",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialAction.a",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialAction.b",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.imperative",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.from",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.pollingTime",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.timeOut",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.criteria",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.resultDescription",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.errorMessageStartingOnEmptyResult",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.a",
                        "class ru.tinkoff.qa.neptune.core.api.localization.steps.SomeSequentialStepSupplier.b"
                )},

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
                        iterableIncludesInOrder(
                                "class ru.tinkoff.qa.neptune.core.api.localization.steps.AggregatedParams.a",
                                "class ru.tinkoff.qa.neptune.core.api.localization.steps.AggregatedParams.b"
                        ),
                },

                {
                        TestExtension.class,
                        empty(),
                },

                {
                        OtherObjectsBundleFilter.class,
                        iterableIncludesInOrder(
                                "class ru.tinkoff.qa.neptune.core.api.localization.ClassC.stepParameter",
                                "class ru.tinkoff.qa.neptune.core.api.localization.additional.classes.described.ClassC.stepParameter",
                                "class ru.tinkoff.qa.neptune.core.api.localization.some.classes.other.classes.described.ClassC.stepParameter"
                        ),
                },

        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {StepBundleFilter.class,
                        iterableIncludesInOrder(
                                endsWith("SomeChildSequentialActionWithAnnotations.actionStep()"),
                                endsWith("SomeChildSequentialStepSupplierWithAnnotations.getAnotherStep(java.util.function.Function)"),
                                endsWith("SomeSequentialAction.actionStep()"),
                                endsWith("SomeSequentialStepSupplier.getStep(java.util.function.Function)")
                        ),
                },

                {
                        CriteriaBundleFilter.class,
                        iterableIncludesInOrder(
                                endsWith("SpecificCriteria.someCriteria()"),
                                endsWith("SpecificCriteria.someCriteria2()")
                        ),
                },

                {
                        AttachmentsBundleFilter.class,
                        emptyIterable(),
                },

                {
                        MatchersBundleFilter.class,
                        emptyIterable(),
                },

                {
                        MismatchDescriptionBundleFilter.class,
                        emptyIterable(),
                },

                {
                        MatchedObjectsBundleFilter.class,
                        emptyIterable(),
                },

                {
                        ParameterPojoBundleFilter.class,
                        emptyIterable(),
                },

                {
                        TestExtension.class,
                        iterableIncludesInOrder(
                                endsWith("localization.additional.classes.ClassA.doSomething(java.lang.String[])"),
                                endsWith("localization.additional.classes.ClassB.doSomething(java.lang.String[])"),
                                endsWith("localization.additional.classes.ClassD.doSomething(java.lang.String[])"),
                                endsWith("localization.some.classes.ClassA.doSomething(java.lang.String[])"),
                                endsWith("localization.some.classes.ClassB.doSomething(java.lang.String[])"),
                                endsWith("localization.some.classes.other.classes.ClassD.doSomething(java.lang.String[])"),
                                endsWith("localization.ClassA.doSomething(java.lang.String[])"),
                                endsWith("localization.ClassB.doSomething(java.lang.String[])"),
                                endsWith("localization.ClassD.doSomething(java.lang.String[])")
                        ),
                },

                {
                        OtherObjectsBundleFilter.class,
                        anything(),
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

    @Test(dependsOnMethods = "checkProcessedClasses", dataProvider = "data2")
    public void checkProcessedFields(Class<? extends BundleFillerExtension> extensionClass,
                                     Matcher<? super Iterable<String>> matcher) {
        var extension = bundleFiller
                .getBundleFillerExtensions()
                .stream()
                .filter(e -> e.getClass().equals(extensionClass))
                .findFirst()
                .orElseThrow();

        var processedClasses = extension.getProcessedClasses();
        var fieldList = new LinkedList<AnnotatedElement>();

        processedClasses.forEach(c -> fieldList.addAll(extension.addFields(c)));

        assertThat(fieldList
                        .stream()
                        .map(f -> ((Member) f).getDeclaringClass() + "." + ((Member) f).getName()).collect(Collectors.toList()),
                matcher);
    }

    @Test(dependsOnMethods = "checkProcessedClasses", dataProvider = "data3")
    public void checkProcessedMethods(Class<? extends BundleFillerExtension> extensionClass,
                                      Matcher<? super Iterable<String>> matcher) {
        var extension = bundleFiller
                .getBundleFillerExtensions()
                .stream()
                .filter(e -> e.getClass().equals(extensionClass))
                .findFirst()
                .orElseThrow();

        var processedClasses = extension.getProcessedClasses();
        var methodList = new LinkedList<Method>();

        processedClasses.forEach(c -> methodList.addAll(extension.addMethods(c)));

        assertThat(methodList
                        .stream()
                        .map(Method::toString)
                        .collect(Collectors.toList()),
                matcher);
    }
}
