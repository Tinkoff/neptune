package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.TestNG;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore;
import ru.tinkoff.qa.neptune.testng.integration.properties.TestNGRefreshStrategyProperty;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestNgTestFinishingTestWithNoConfigMethod {

    private void runBeforeTheChecking() {
        TestNG testNG=new TestNG();

        List<XmlSuite> testSuites=new ArrayList<>();

        XmlSuite suite = new XmlSuite();
        XmlSuite parent = new XmlSuite();
        suite.setParentSuite(parent);
        suite.setName("FinishSuiteWithNoConfigMethod");

        XmlTest test = new XmlTest(suite);

        List<XmlClass> testClasses = new ArrayList<>();
        testClasses.add(new XmlClass(TestWithoutConfigurationMethod.class.getName()));

        test.setXmlClasses(testClasses);
        //testNG.setAnnotationTransformer();
        testSuites.add(suite);

        testNG.setXmlSuites(testSuites);
        testNG.run();
    }

    @BeforeMethod
    public void refresh() {
        StepClass2.refreshCountToZero();
    }

    @Test
    public void whenNoRefreshingStrategyIsDefined() {
        runBeforeTheChecking();
        assertThat(StepClass2.getRefreshCount(), is(3));
    }

    @Test
    public void whenRefreshingStrategyIsBeforeSuite() {
        TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.accept(RefreshEachTimeBefore.SUITE_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(StepClass2.getRefreshCount(), is(0));
        }
        finally {
            System.getProperties().remove(TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeTest() {
        TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.accept(RefreshEachTimeBefore.ALL_TEST_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(StepClass2.getRefreshCount(), is(0));
        }
        finally {
            System.getProperties().remove(TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeClass() {
        TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.accept(RefreshEachTimeBefore.CLASS_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(StepClass2.getRefreshCount(), is(0));
        }
        finally {
            System.getProperties().remove(TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeMethod() {
        TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.accept(RefreshEachTimeBefore.BEFORE_METHOD_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(StepClass2.getRefreshCount(), is(0));
        }
        finally {
            System.getProperties().remove(TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsCombined() {
        TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.accept(stream(RefreshEachTimeBefore.values())
                .map(Enum::name).collect(joining(",")));
        try {
            runBeforeTheChecking();
            assertThat(StepClass2.getRefreshCount(), is(3));
        }
        finally {
            System.getProperties().remove(TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }
}
