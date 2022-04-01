package ru.tinkoff.qa.neptune.core.api.localization;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.ClassA;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.ClassB;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.ClassC;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.ClassD;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.bundle.extensions.TestExtension;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.bundle.partitions.AllPackagePartition;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.hamcrest.SomeMatchedObject;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.hamcrest.SomeMatcher;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.hamcrest.SomeMismatch;
import ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.io.File.createTempFile;
import static java.nio.file.Files.readAllLines;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItems;

public class BundleFillerFullTest {

    private final ResourceBundleGenerator.BundleFiller bundleFiller;
    private final File propertyFile;

    public BundleFillerFullTest() throws IOException {
        propertyFile = createTempFile(randomAlphanumeric(15), ".properties");
        propertyFile.deleteOnExit();

        Properties properties = new Properties();
        properties.setProperty("A", "AValue");
        properties.setProperty("B", "BValue");

        try (var fileOutput = new FileOutputStream(propertyFile)) {
            properties.store(fileOutput, "");
        }

        bundleFiller = new ResourceBundleGenerator.BundleFiller(new AllPackagePartition(),
                propertyFile,
                properties);
    }

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {StepBundleFilter.class, iterableInOrder(
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
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.additional.classes.described.ClassC.class,
                                ru.tinkoff.qa.neptune.core.api.localization.data.generation.some.classes.other.classes.described.ClassC.class
                        ),
                },
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {StepBundleFilter.class, iterableInOrder(
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialActionWithAnnotations.imperative",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialActionWithAnnotations.performOn",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialActionWithAnnotations.c",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialStepSupplierWithAnnotations.imperative",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialStepSupplierWithAnnotations.from",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialStepSupplierWithAnnotations.pollingTime",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialStepSupplierWithAnnotations.timeOut",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialStepSupplierWithAnnotations.criteria",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialStepSupplierWithAnnotations.resultDescription",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialStepSupplierWithAnnotations.errorMessageStartingOnEmptyResult",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeChildSequentialStepSupplierWithAnnotations.c",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialAction.imperative",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialAction.performOn",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialAction.a",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialAction.b",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.imperative",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.from",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.pollingTime",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.timeOut",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.criteria",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.resultDescription",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.errorMessageStartingOnEmptyResult",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.a",
                        "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.SomeSequentialStepSupplier.b"
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
                        iterableInOrder(
                                "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.AggregatedParams.a",
                                "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps.AggregatedParams.b"
                        ),
                },

                {
                        TestExtension.class,
                        empty(),
                },

                {
                        OtherObjectsBundleFilter.class,
                        iterableInOrder(
                                "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.ClassC.stepParameter",
                                "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.additional.classes.described.ClassC.stepParameter",
                                "class ru.tinkoff.qa.neptune.core.api.localization.data.generation.some.classes.other.classes.described.ClassC.stepParameter"
                        ),
                },

        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {StepBundleFilter.class,
                        iterableInOrder(
                                endsWith("SomeChildSequentialActionWithAnnotations.actionStep()"),
                                endsWith("SomeChildSequentialStepSupplierWithAnnotations.getAnotherStep(java.util.function.Function)"),
                                endsWith("SomeSequentialAction.actionStep()"),
                                endsWith("SomeSequentialStepSupplier.getStep(java.util.function.Function)")
                        ),
                },

                {
                        CriteriaBundleFilter.class,
                        iterableInOrder(
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
                        iterableInOrder(
                                endsWith("localization.data.generation.additional.classes.ClassA.doSomething(java.lang.String[])"),
                                endsWith("localization.data.generation.additional.classes.ClassB.doSomething(java.lang.String[])"),
                                endsWith("localization.data.generation.additional.classes.ClassD.doSomething(java.lang.String[])"),
                                endsWith("localization.data.generation.some.classes.ClassA.doSomething(java.lang.String[])"),
                                endsWith("localization.data.generation.some.classes.ClassB.doSomething(java.lang.String[])"),
                                endsWith("localization.data.generation.some.classes.other.classes.ClassD.doSomething(java.lang.String[])"),
                                endsWith("localization.data.generation.ClassA.doSomething(java.lang.String[])"),
                                endsWith("localization.data.generation.ClassB.doSomething(java.lang.String[])"),
                                endsWith("localization.data.generation.ClassD.doSomething(java.lang.String[])")
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

    @Test
    public void fillTest() throws Exception {
        bundleFiller.fillFile();

        var generated = readAllLines(propertyFile.toPath());
        var expected = readAllLines(new File("src/test/resources/generated_properties_to_compare").toPath());

        assertThat(generated, iterableInOrder(expected.toArray(new String[]{})));
    }
}
