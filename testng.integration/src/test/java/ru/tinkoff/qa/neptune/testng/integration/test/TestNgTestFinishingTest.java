package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.annotations.BeforeMethod;
import ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore;
import ru.tinkoff.qa.neptune.testng.integration.test.ignored.IgnoredStubTest;
import ru.tinkoff.qa.neptune.testng.integration.test.ignored.entries.IgnoredStubTest2;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import ru.tinkoff.qa.neptune.testng.integration.properties.TestNGRefreshStrategyProperty;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNgTestFinishingTest {

    private void runBeforeTheChecking() {
        TestNG testNG=new TestNG();

        List<XmlSuite> testSuites=new ArrayList<>();

        XmlSuite suite=new XmlSuite();
        suite.setName("FinishSuite");

        XmlTest test = new XmlTest(suite);

        List<XmlClass> testClasses=new ArrayList<>();
        testClasses.add(new XmlClass(TestNgInstantiationTest.class.getName()));
        testClasses.add(new XmlClass(TestNgStubTest.class.getName()));
        testClasses.add(new XmlClass(IgnoredStubTest2.class.getName()));
        testClasses.add(new XmlClass(IgnoredStubTest.class.getName()));

        test.setXmlClasses(testClasses);
        testSuites.add(suite);

        testNG.setXmlSuites(testSuites);
        testNG.run();
    }

    @BeforeMethod
    public void refresh() {
        StepClass2.countsToZero();
    }

    @Test
    public void shuttingDownTest() {
        runBeforeTheChecking();
        assertThat(StepClass2.getStopCount(), is(1));
    }

    @Test
    public void whenNoRefreshingStrategyIsDefined() {
        runBeforeTheChecking();
        assertThat(StepClass2.getRefreshCount(), is(8));
    }

    @Test
    public void whenRefreshingStrategyIsBeforeSuite() {
        TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.accept(RefreshEachTimeBefore.SUITE_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(StepClass2.getRefreshCount(), is(1));
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
            assertThat(StepClass2.getRefreshCount(), is(1));
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
            assertThat(StepClass2.getRefreshCount(), is(2));
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
            assertThat(StepClass2.getRefreshCount(), is(9));
        }
        finally {
            System.getProperties().remove(TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsCombined() {
        TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.accept(String.join(",", stream(RefreshEachTimeBefore.values())
                .map(Enum::name).collect(toList())));
        try {
            runBeforeTheChecking();
            assertThat(StepClass2.getRefreshCount(), is(9));
        }
        finally {
            System.getProperties().remove(TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }
}
