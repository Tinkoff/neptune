package com.github.toy.constructor.testng.integration.test;

import org.testng.TestNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNgTestFinishingTest {

    private static int stopCount = ofNullable(StepClass2.stepClass2)
            .map(StepClass2::getStopCount)
            .orElse(0);

    private static int refreshCount1 = ofNullable(StepClass1.stepClass1)
            .map(StepClass1::getRefreshCount)
            .orElse(0);

    private static int refreshCount2 = ofNullable(StepClass2.stepClass2)
            .map(StepClass2::getRefreshCount)
            .orElse(0);

    @BeforeClass
    public void beforeClass() {
        TestNG testNG=new TestNG();

        List<XmlSuite> testSuites=new ArrayList<>();

        XmlSuite suite=new XmlSuite();
        suite.setName("FinishSuite");

        XmlTest test = new XmlTest(suite);

        List<XmlClass> testClasses=new ArrayList<>();
        testClasses.add(new XmlClass(TestNgIntegrationTest.class.getName()));
        testClasses.add(new XmlClass(TestNgIntegrationTest2.class.getName()));

        test.setXmlClasses(testClasses);
        testSuites.add(suite);

        testNG.setXmlSuites(testSuites);
        testNG.run();
    }

    @Test
    public void shuttingDownTest() {
        assertThat(StepClass2.stepClass2.getStopCount(), is(stopCount + 1));
    }
}
