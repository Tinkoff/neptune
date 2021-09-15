package ru.tinkoff.qa.neptune.testng.integration.test;

import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore;

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

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {null, 3},
                {of(SUITE_STARTING), 0},
                {of(TEST_STARTING), 0},
                {of(CLASS_STARTING), 0},
                {of(BEFORE_METHOD_STARTING), 0},
                {of(METHOD_STARTING), 3},
                {asList(RefreshEachTimeBefore.values()), 3}

        };
    }

    @Test(dataProvider = "data")
    public void refreshTest(List<RefreshEachTimeBefore> strategies, int expected) {
        REFRESH_STRATEGY_PROPERTY.accept(strategies);
        runBeforeTheChecking();
        assertThat(ContextClass2.getRefreshCount(), is(expected));
    }

    @AfterMethod
    public void afterRefreshTest() {
        REFRESH_STRATEGY_PROPERTY.accept(null);
    }
}
