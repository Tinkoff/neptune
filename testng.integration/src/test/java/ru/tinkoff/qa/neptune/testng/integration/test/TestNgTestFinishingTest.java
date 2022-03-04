package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.TestNG;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore;
import ru.tinkoff.qa.neptune.testng.integration.test.ignored.IgnoredStubTest;
import ru.tinkoff.qa.neptune.testng.integration.test.ignored.entries.IgnoredStubTest2;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.core.api.concurrency.ObjectContainer.getAllObjects;
import static ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore.*;
import static ru.tinkoff.qa.neptune.testng.integration.properties.TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;

public class TestNgTestFinishingTest {

    private void runBeforeTheChecking() {
        ContextClass2.refreshCountToZero();
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

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {null, 9},
                {of(SUITE_STARTING), 1},
                {of(TEST_STARTING), 1},
                {of(CLASS_STARTING), 2},
                {of(BEFORE_METHOD_STARTING), 9},
                {of(METHOD_STARTING), 9},
                {asList(RefreshEachTimeBefore.values()), 9}

        };
    }

    @Test(dataProvider = "data", groups = "refresh")
    public void refreshTest(List<RefreshEachTimeBefore> strategies, int expected) {
        REFRESH_STRATEGY_PROPERTY.accept(strategies);
        runBeforeTheChecking();
        assertThat(ContextClass2.getRefreshCount(), is(expected));
    }

    @AfterGroups(groups = "refresh")
    public void afterRefreshTest() {
        REFRESH_STRATEGY_PROPERTY.accept(null);
    }

    @Test(dependsOnMethods = {"refreshTest", "hookTest"})
    public void instanceCountTest() {
        assertThat(getAllObjects(ContextClass2.class, objectContainer -> true), hasSize(6));
        assertThat(getAllObjects(ContextClass1.class, objectContainer -> true), hasSize(6));
    }

    @Test(priority = 2)
    public void hookTest() {
        TestHook.count = 0;
        runBeforeTheChecking();
        assertThat(TestHook.count, is(55));
    }
}
