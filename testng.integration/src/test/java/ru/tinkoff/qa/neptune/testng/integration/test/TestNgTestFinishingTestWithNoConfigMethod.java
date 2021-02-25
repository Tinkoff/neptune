package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore.*;
import static ru.tinkoff.qa.neptune.testng.integration.properties.TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;

public class TestNgTestFinishingTestWithNoConfigMethod {

    private void runBeforeTheChecking() {
        ContextClass2.refreshCountToZero();
        TestNG testNG = new TestNG();

        List<XmlSuite> testSuites = new ArrayList<>();

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

    @Test
    public void whenNoRefreshingStrategyIsDefined() {
        runBeforeTheChecking();
        assertThat(ContextClass2.getRefreshCount(), is(3));
    }

    @Test
    public void whenRefreshingStrategyIsBeforeSuite() {
        REFRESH_STRATEGY_PROPERTY.accept(of(SUITE_STARTING));
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(0));
        } finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeTest() {
        REFRESH_STRATEGY_PROPERTY.accept(of(TEST_STARTING));
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(0));
        } finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeClass() {
        REFRESH_STRATEGY_PROPERTY.accept(of(CLASS_STARTING));
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(0));
        } finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeMethod() {
        REFRESH_STRATEGY_PROPERTY.accept(of(BEFORE_METHOD_STARTING));
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(0));
        } finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsCombined() {
        REFRESH_STRATEGY_PROPERTY.accept(asList(values()));
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(3));
        } finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getName());
        }
    }
}
