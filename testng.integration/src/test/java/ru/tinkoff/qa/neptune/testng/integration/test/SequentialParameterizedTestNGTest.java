package ru.tinkoff.qa.neptune.testng.integration.test;

import org.hamcrest.Matcher;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;

import static org.hamcrest.MatcherAssert.assertThat;

public class SequentialParameterizedTestNGTest extends BaseTestNgTest {

    private ContextClass2 contextClass2;
    private final int numberToAdd;
    private final Matcher<? super Integer> criteria;
    private int result;

    SequentialParameterizedTestNGTest(int numberToAdd, Matcher<? super Integer> criteria) {
        this.numberToAdd = numberToAdd;
        this.criteria = criteria;
    }

    @Test
    public void calcResult() {
        result = contextClass2.getA() + contextClass2.getB() + numberToAdd;
    }

    @Test(dependsOnMethods = "calcResult")
    public void checkResult() {
        assertThat(result, criteria);
    }
}
