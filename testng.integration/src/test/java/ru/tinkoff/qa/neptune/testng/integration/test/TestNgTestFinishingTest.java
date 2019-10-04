package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore;
import ru.tinkoff.qa.neptune.testng.integration.test.ignored.IgnoredStubTest;
import ru.tinkoff.qa.neptune.testng.integration.test.ignored.entries.IgnoredStubTest2;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.concurency.ObjectContainer.getAllObjects;
import static ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore.*;
import static ru.tinkoff.qa.neptune.testng.integration.properties.TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;

public class TestNgTestFinishingTest {

    private void runBeforeTheChecking() {
        TestNG testNG=new TestNG();

        List<XmlSuite> testSuites=new ArrayList<>();

        XmlSuite suite = new XmlSuite();
        XmlSuite parent = new XmlSuite();
        suite.setParentSuite(parent);
        suite.setName("FinishSuite");

        XmlTest test = new XmlTest(suite);

        List<XmlClass> testClasses = new ArrayList<>();
        testClasses.add(new XmlClass(TestNgInstantiationTest.class.getName()));
        testClasses.add(new XmlClass(TestNgStubTest.class.getName()));
        testClasses.add(new XmlClass(IgnoredStubTest2.class.getName()));
        testClasses.add(new XmlClass(IgnoredStubTest.class.getName()));

        test.setXmlClasses(testClasses);
        //testNG.setAnnotationTransformer();
        testSuites.add(suite);

        testNG.setXmlSuites(testSuites);
        testNG.run();
    }

    @BeforeMethod
    public void refresh() {
        ContextClass2.refreshCountToZero();
    }

    @Test
    public void whenNoRefreshingStrategyIsDefined() {
        runBeforeTheChecking();
        assertThat(ContextClass2.getRefreshCount(), is(8));
    }

    @Test
    public void whenRefreshingStrategyIsBeforeSuite() {
        REFRESH_STRATEGY_PROPERTY.accept(SUITE_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(1));
        }
        finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeTest() {
        REFRESH_STRATEGY_PROPERTY.accept(ALL_TEST_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(1));
        }
        finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeClass() {
        REFRESH_STRATEGY_PROPERTY.accept(CLASS_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(2));
        }
        finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsBeforeMethod() {
        REFRESH_STRATEGY_PROPERTY.accept(BEFORE_METHOD_STARTING.name());
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(9));
        }
        finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void whenRefreshingStrategyIsCombined() {
        REFRESH_STRATEGY_PROPERTY.accept(stream(RefreshEachTimeBefore.values())
                .map(Enum::name).collect(joining(",")));
        try {
            runBeforeTheChecking();
            assertThat(ContextClass2.getRefreshCount(), is(9));
        }
        finally {
            System.getProperties().remove(REFRESH_STRATEGY_PROPERTY.getPropertyName());
        }
    }

    @AfterClass
    public void afterClass() {
        assertThat(getAllObjects(ContextClass2.class, objectContainer -> true), hasSize(6));
        assertThat(getAllObjects(ContextClass1.class, objectContainer -> true), hasSize(6));
    }
}
