package ru.tinkoff.qa.neptune.testng.integration.test;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import static org.hamcrest.Matchers.*;

public class SequentialParameterizedTestNGTestFactory {
    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {1, equalTo(4)},
                {2, equalTo(5)},
        };
    }

    @Factory(dataProvider = "data")
    public Object[] createInstances(int valueToSet,
                                    Matcher<? super Integer> criteria) {
        return new Object[]{new SequentialParameterizedTestNGTest(valueToSet, criteria)};
    }
}
