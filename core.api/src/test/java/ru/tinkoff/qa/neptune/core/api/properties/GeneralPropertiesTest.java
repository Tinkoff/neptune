package ru.tinkoff.qa.neptune.core.api.properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GeneralPropertiesTest {

    @AfterMethod
    public void clear() {
        System.clearProperty("propertyA");
        System.clearProperty("propertyB");
        System.clearProperty("ConcatenatedProperty");
    }

    @Test
    public void externalPropertiesPriorityTest() {
        System.setProperty("propertyA", "a");
        System.setProperty("propertyB", "b");

        assertThat(new PropertyA().get(), is("A"));
        assertThat(new PropertyB().get(), is("B"));
    }

    @Test
    public void reusePropertiesTest() {
        System.setProperty("ConcatenatedProperty", "${propertyA}+${propertyB}");
        assertThat(new ConcatenatedProperty().get(), is("A+B"));
    }
}
