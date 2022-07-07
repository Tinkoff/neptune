package ru.tinkoff.qa.neptune.testng.integration.test;

import org.hamcrest.Matcher;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;

public class SequentialParameterizedTestNGTest extends BaseTestNgTest {

    private final int numberToAdd;
    private final Matcher<? super Integer> criteria;
    private int result;

    SequentialParameterizedTestNGTest(int numberToAdd, Matcher<? super Integer> criteria) {
        this.numberToAdd = numberToAdd;
        this.criteria = criteria;
    }

    @Test
    public void calcResult() {
        result = getCreatedContextOrCreate(ContextClass2.class).getA()
                + getCreatedContextOrCreate(ContextClass2.class).getB() + numberToAdd;
    }

    @Test(dependsOnMethods = "calcResult")
    public void checkResult() {
        assertThat(result, criteria);
    }
}
