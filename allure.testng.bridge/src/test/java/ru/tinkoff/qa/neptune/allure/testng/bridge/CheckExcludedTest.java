package ru.tinkoff.qa.neptune.allure.testng.bridge;

import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.model.TestResultContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.testng.TestNG;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.xml.XmlSuite.ParallelMode.METHODS;
import static ru.tinkoff.qa.neptune.allure.testng.bridge.NeptuneAllureExcludeTestListener.changeResultWriter;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CheckExcludedTest {

    @Mock
    private AllureResultsWriter mockWriter;
    private Field excludedField;

    @BeforeAll
    void beforeClass() throws Exception {
        openMocks(this);
        changeResultWriter(mockWriter);

        excludedField = ItemsToNotBeReported.class.getDeclaredField("EXCLUDED");
        excludedField.setAccessible(true);
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void beforeMethod() throws Exception {
        clearInvocations(mockWriter);

        var excluded = (Set<Object>) excludedField.get(ItemsToNotBeReported.class);
        excluded.clear();
    }

    static Stream<? extends Arguments> excludedClasses() {
        return Stream.of(
            arguments("ru.tinkoff.qa.neptune.allure.testng.bridge.fully.excluded"),
            arguments("ru.tinkoff.qa.neptune.allure.testng.bridge.excluded.clazz")
        );
    }

    @ParameterizedTest
    @MethodSource("excludedClasses")
    void excludedTest(String pack) {

        TestNG testNG = new TestNG();

        List<XmlSuite> testSuites = new ArrayList<>();

        XmlSuite suite = new XmlSuite();
        suite.setName("Test suite");
        suite.setParallel(METHODS);

        XmlTest test = new XmlTest(suite);

        List<XmlPackage> testClasses = new ArrayList<>();
        testClasses.add(new XmlPackage(pack));

        test.setXmlPackages(testClasses);
        testSuites.add(suite);

        testNG.setXmlSuites(testSuites);
        testNG.run();

        verify(mockWriter, never()).write(any(TestResult.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void excludedMethodsTest() throws Exception {
        TestNG testNG = new TestNG();

        List<XmlSuite> testSuites = new ArrayList<>();

        XmlSuite suite = new XmlSuite();
        suite.setName("Test suite");
        suite.setParallel(METHODS);

        XmlTest test = new XmlTest(suite);

        List<XmlPackage> testClasses = new ArrayList<>();
        testClasses.add(new XmlPackage("ru.tinkoff.qa.neptune.allure.testng.bridge.excluded.methods"));

        test.setXmlPackages(testClasses);
        testSuites.add(suite);

        testNG.setXmlSuites(testSuites);
        testNG.run();

        verify(mockWriter, times(9)).write(any(TestResult.class));
        verify(mockWriter, times(47)).write(any(TestResultContainer.class));

        var excluded = (Set<Object>) excludedField.get(ItemsToNotBeReported.class);

        var excludedResultsAndContainers = excluded
            .stream()
            .filter(o -> o instanceof TestResult)
            .map(o -> ((TestResult) o).getName())
            .distinct()
            .collect(toList());

        assertThat(excludedResultsAndContainers,
            iterableOf("test1", "test2", "test6", "test7", "test21", "test22"));

        var excludedFixtureNames = excluded
            .stream()
            .filter(o -> o instanceof ItemsToNotBeReported.ExcludedFixtureWrapper)
            .map(o -> ((ItemsToNotBeReported.ExcludedFixtureWrapper) o).getFixtureResult().getName())
            .distinct()
            .collect(toList());

        assertThat(excludedFixtureNames,
            iterableOf("beforeEach", "afterEachExcluded", "beforeEachNested", "beforeEach2", "afterEachExcluded2"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void notExcludedTest() throws Exception {
        TestNG testNG = new TestNG();

        List<XmlSuite> testSuites = new ArrayList<>();

        XmlSuite suite = new XmlSuite();
        suite.setName("Test suite");
        suite.setParallel(METHODS);

        XmlTest test = new XmlTest(suite);

        List<XmlPackage> testClasses = new ArrayList<>();
        testClasses.add(new XmlPackage("ru.tinkoff.qa.neptune.allure.testng.bridge.not.excluded"));

        test.setXmlPackages(testClasses);
        testSuites.add(suite);

        testNG.setXmlSuites(testSuites);
        testNG.run();

        verify(mockWriter, times(12)).write(any(TestResult.class));
        verify(mockWriter, times(48)).write(any(TestResultContainer.class));

        var excluded = (Set<Object>) excludedField.get(ItemsToNotBeReported.class);

        assertThat(excluded, emptyIterable());
    }
}
