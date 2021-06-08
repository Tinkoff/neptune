package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.pojo.PojoGetterReturnsMatcher.getterReturns;

public class PojoMatcherTest {

    private static final SomePojo SOME_POJO = new SomePojo()
            .setA("AB")
            .setB(false);

    @DataProvider
    public Object[][] data1() {
        return new Object[][]{
                {getterReturns("getA", startsWith("A"), endsWith("B"))},
                {getterReturns("getA", startsWith("A"))},
                {getterReturns("getA", "AB")},
        };
    }

    @DataProvider
    public Object[][] data2() {
        return new Object[][]{
                {getterReturns("getB", true), "Tested object\n" +
                        "Expected: Public getter 'getB' is expected to return a value: <true>\n" +
                        "     but: Class class ru.tinkoff.qa.neptune.core.api.hamcrest.SomePojo has " +
                        "no non-static and public method 'getB' with empty signature and which returns some value"},
                {getterReturns("getA", startsWith("B"), endsWith("A")),
                        "Tested object\n" +
                                "Expected: Public getter 'getA' is expected to return a value: a string starting with \"B\", a string ending with \"A\"\n" +
                                "     but: value returned from 'getA': AB. doesn't match any of listed criteria"},
                {getterReturns("getA", startsWith("B")), "Tested object\n" +
                        "Expected: Public getter 'getA' is expected to return a value: a string starting with \"B\"\n" +
                        "     but: value returned from 'getA': AB. was \"AB\""},
                {getterReturns("getA", "BA"), "Tested object\n" +
                        "Expected: Public getter 'getA' is expected to return a value: \"BA\"\n" +
                        "     but: value returned from 'getA': AB. was \"AB\""},
        };
    }

    @Test(dataProvider = "data1")
    public void test1(Matcher<Object> matcher) {
        assertThat("Tested object", SOME_POJO, matcher);
    }

    @Test(dataProvider = "data2")
    public void test2(Matcher<Object> matcher, String errorText) {
        try {
            assertThat("Tested object", SOME_POJO, matcher);
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(errorText));
            return;
        }

        fail("Exception was expected");
    }
}
