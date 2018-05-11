package com.github.toy.constructor.selenium.test.function.descriptions.search;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.reflections.Reflections;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static com.github.toy.constructor.selenium.api.widget.Widget.getWidgetName;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeEnabled;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.widgets;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.widget;
import static com.github.toy.constructor.selenium.properties.FlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static java.lang.String.format;
import static java.time.Duration.ofSeconds;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * This test checks descriptions of functions which are built by
 * {@code 'com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.widget*'} methods
 * and methods that overload load these ones.
 */
public class Widgets {

    @DataProvider
    public Object[][] supportedClasses() {
        Object[][] result = new Object[][]{};

        Reflections reflections = new Reflections("");
        Set<Class<? extends Widget>> classes = reflections.getSubTypesOf(Widget.class);

        for (Class<? extends Widget> clazz: classes) {
            if (clazz.getAnnotation(Name.class) == null) {
                throw new IllegalArgumentException(format("Class %s is not annotated by %s", clazz.getName(),
                        Name.class.getName()));
            }

            result = add(result, new Object[] {clazz});
        }
        return result;
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDuration(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, ofSeconds(5), shouldBeEnabled()).toString(),
                is(format("%s with condition Should be enabled. " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyDurationSpecified(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, shouldBeEnabled()).toString(),
                is(format("%s with condition Should be enabled. " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDurationSpecifiedInProperties(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(widget(widgetClass, shouldBeEnabled()).toString(),
                    is(format("%s with condition Should be enabled. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionSpecified(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, ofSeconds(5)).toString(),
                is(format("%s. Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionAndTimeSpecified(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass).toString(),
                is(format("%s. Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionAndTimeSpecifiedInProperties(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widget(widgetClass).toString(),
                    is(format("%s. Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDefinedConditionAndTimeSpecifiedInProperties(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widget(widgetClass, shouldBeEnabled()).toString(),
                    is(format("%s with condition Should be enabled. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithVisibilityConditionAndTimeSpecifiedInProperties(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(widget(widgetClass).toString(),
                    is(format("%s with condition Should be visible. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDuration(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, ofSeconds(5), shouldBeEnabled()).toString(),
                is(format("Elements of type %s with condition Should be enabled. " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyDurationSpecified(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, shouldBeEnabled()).toString(),
                is(format("Elements of type %s with condition Should be enabled. " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDurationSpecifiedInProperties(Class<? extends Widget> widgetClass){
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(widgets(widgetClass, shouldBeEnabled()).toString(),
                    is(format("Elements of type %s with condition Should be enabled. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionSpecified(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, ofSeconds(5)).toString(),
                is(format("Elements of type %s. " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionAndTimeSpecified(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass).toString(),
                is(format("Elements of type %s. " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionAndTimeSpecifiedInProperties(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widgets(widgetClass).toString(),
                    is(format("Elements of type %s." +
                            " Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDefinedConditionAndTimeSpecifiedInProperties(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widgets(widgetClass, shouldBeEnabled()).toString(),
                    is(format("Elements of type %s with condition Should be enabled. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithVisibilityConditionAndTimeSpecifiedInProperties(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(widgets(widgetClass).toString(),
                    is(format("Elements of type %s with condition Should be visible. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDurationAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, List.of("Label1", "Label2"), ofSeconds(5), shouldBeEnabled()).toString(),
                is(format("%s with condition " +
                        "(Should have string label(s) [Label1, Label2]) AND (Should be enabled). " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyDurationAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, List.of("Label1", "Label2"), shouldBeEnabled()).toString(),
                is(format("%s with condition " +
                        "(Should have string label(s) [Label1, Label2]) AND (Should be enabled). " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDurationSpecifiedInPropertiesAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(widget(widgetClass, List.of("Label1", "Label2"), shouldBeEnabled()).toString(),
                    is(format("%s with condition " +
                            "(Should have string label(s) [Label1, Label2]) AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionSpecifiedAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, List.of("Label1", "Label2"), ofSeconds(5)).toString(),
                is(format("%s with condition " +
                        "Should have string label(s) [Label1, Label2]. " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionAndTimeSpecifiedAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, List.of("Label1", "Label2")).toString(),
                is(format("%s with condition " +
                        "Should have string label(s) [Label1, Label2]. " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widget(widgetClass, List.of("Label1", "Label2")).toString(),
                    is(format("%s with condition " +
                            "Should have string label(s) [Label1, Label2]. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDefinedConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widget(widgetClass, List.of("Label1", "Label2"), shouldBeEnabled()).toString(),
                    is(format("%s with condition " +
                            "(Should have string label(s) [Label1, Label2]) AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithVisibilityConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(widget(widgetClass, List.of("Label1", "Label2")).toString(),
                    is(format("%s with condition " +
                            "(Should have string label(s) [Label1, Label2]) AND (Should be visible). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDurationAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, List.of("Label1", "Label2"), ofSeconds(5), shouldBeEnabled()).toString(),
                is(format("Elements of type %s with condition " +
                        "(Should have string label(s) [Label1, Label2]) AND (Should be enabled). " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyDurationSpecifiedAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, List.of("Label1", "Label2"), shouldBeEnabled()).toString(),
                is(format("Elements of type %s with condition " +
                        "(Should have string label(s) [Label1, Label2]) AND (Should be enabled). " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDurationSpecifiedInPropertiesAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(widgets(widgetClass, List.of("Label1", "Label2"), shouldBeEnabled()).toString(),
                    is(format("Elements of type %s with condition " +
                            "(Should have string label(s) [Label1, Label2]) AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionSpecifiedAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, List.of("Label1", "Label2"), ofSeconds(5)).toString(),
                is(format("Elements of type %s with condition " +
                        "Should have string label(s) [Label1, Label2]. " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionAndTimeSpecifiedAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, List.of("Label1", "Label2")).toString(),
                is(format("Elements of type %s with condition Should have string label(s) [Label1, Label2]. " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widgets(widgetClass, List.of("Label1", "Label2")).toString(),
                    is(format("Elements of type %s " +
                            "with condition Should have string label(s) [Label1, Label2]. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDefinedConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widgets(widgetClass, List.of("Label1", "Label2"), shouldBeEnabled()).toString(),
                    is(format("Elements of type %s with condition " +
                            "(Should have string label(s) [Label1, Label2]) AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithVisibilityConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabels(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(widgets(widgetClass, List.of("Label1", "Label2")).toString(),
                    is(format("Elements of type %s with condition " +
                            "(Should have string label(s) [Label1, Label2]) AND (Should be visible). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDurationAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, "Label1", ofSeconds(5), shouldBeEnabled()).toString(),
                is(format("%s with condition " +
                        "(Should have string label(s) [Label1]) AND (Should be enabled). " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyDurationAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, "Label1", shouldBeEnabled()).toString(),
                is(format("%s with condition " +
                        "(Should have string label(s) [Label1]) AND (Should be enabled). " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDurationSpecifiedInPropertiesAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(widget(widgetClass, "Label1", shouldBeEnabled()).toString(),
                    is(format("%s with condition " +
                            "(Should have string label(s) [Label1]) AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionSpecifiedAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, "Label1", ofSeconds(5)).toString(),
                is(format("%s with condition " +
                        "Should have string label(s) [Label1]. " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionAndTimeSpecifiedAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        assertThat(widget(widgetClass, "Label1").toString(),
                is(format("%s with condition " +
                        "Should have string label(s) [Label1]. " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithoutAnyConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widget(widgetClass, "Label1").toString(),
                    is(format("%s with condition " +
                            "Should have string label(s) [Label1]. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithDefinedConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widget(widgetClass, "Label1", shouldBeEnabled()).toString(),
                    is(format("%s with condition " +
                            "(Should have string label(s) [Label1]) AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfASingleWidgetsWithVisibilityConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(widget(widgetClass, "Label1").toString(),
                    is(format("%s with condition " +
                            "(Should have string label(s) [Label1]) AND (Should be visible). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDurationAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, "Label1", ofSeconds(5), shouldBeEnabled()).toString(),
                is(format("Elements of type %s with condition " +
                        "(Should have string label(s) [Label1]) AND (Should be enabled). " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyDurationSpecifiedAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, "Label1", shouldBeEnabled()).toString(),
                is(format("Elements of type %s with condition (Should have string label(s) [Label1]) " +
                        "AND (Should be enabled). Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDurationSpecifiedInPropertiesAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(
                    widgets(widgetClass, "Label1", shouldBeEnabled()).toString(),
                    is(format("Elements of type %s with condition " +
                            "(Should have string label(s) [Label1]) AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionSpecifiedAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, "Label1", ofSeconds(5)).toString(),
                is(format("Elements of type %s with condition " +
                        "Should have string label(s) [Label1]. " +
                        "Time to get valuable result: 0:00:05:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionAndTimeSpecifiedAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        assertThat(widgets(widgetClass, "Label1").toString(),
                is(format("Elements of type %s with condition " +
                        "Should have string label(s) [Label1]. " +
                        "Time to get valuable result: 0:01:00:000", getWidgetName(widgetClass))));
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithoutAnyConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widgets(widgetClass, "Label1").toString(),
                    is(format("Elements of type %s with condition " +
                            "Should have string label(s) [Label1]. " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithDefinedConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(widgets(widgetClass, "Label1", shouldBeEnabled()).toString(),
                    is(format("Elements of type %s with condition " +
                            "(Should have string label(s) [Label1]) AND (Should be enabled). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(dataProvider = "supportedClasses")
    public void descriptionOfMultipleWidgetsWithVisibilityConditionAndTimeSpecifiedInPropertiesAndSpecifiedLabel(Class<? extends Widget> widgetClass) {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        System.setProperty(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName(), "true");

        try {
            assertThat(widgets(widgetClass, "Label1").toString(),
                    is(format("Elements of type %s with condition " +
                            "(Should have string label(s) [Label1]) AND (Should be visible). " +
                            "Time to get valuable result: 0:03:00:000", getWidgetName(widgetClass))));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
            System.getProperties().remove(FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.getPropertyName());
        }
    }
}
