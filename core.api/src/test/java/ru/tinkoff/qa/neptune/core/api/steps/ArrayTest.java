package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;

public class ArrayTest {

    @Test
    public void arrayTest() {
        var array = new Integer[]{1, 2};
        var array2 = (Object[]) array;
    }
}
