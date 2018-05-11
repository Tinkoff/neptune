package com.github.toy.constructor.selenium.test.function.descriptions.search;

import org.openqa.selenium.SearchContext;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.items;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.item;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * This test checks descriptions of functions which are built by
 * {@code 'com.github.toy.constructor.selenium.functions.searching.SearchSupplier.item'} methods
 */
public class SomeItems {

    private static final Function<SearchContext, List<SearchContext>> SOME_FUNCTION =
            toGet("Some items from search context", searchContext -> List.of());

    private static final Predicate<SearchContext> SOME_CONDITION =
            condition("Some condition", searchContext -> true);


    @Test
    public void descriptionOfASingleItemWithDuration() {
        assertThat(item("A single item", SOME_FUNCTION, ofSeconds(5), SOME_CONDITION).toString(),
                is("A single item with condition Some condition. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfASingleItemWithoutAnyDurationSpecified() {
        assertThat(item("A single item", SOME_FUNCTION, SOME_CONDITION).toString(),
                is("A single item with condition Some condition. " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfASingleItemWithDurationSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(item("A single item", SOME_FUNCTION, SOME_CONDITION).toString(),
                    is("A single item with condition Some condition. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void descriptionOfMultipleItemsWithDuration() {
        assertThat(items(SOME_FUNCTION, ofSeconds(5), SOME_CONDITION).toString(),
                is("Some items from search context with condition Some condition. " +
                        "Time to get valuable result: 0:00:05:000"));
    }

    @Test
    public void descriptionOfMultipleItemsWithoutAnyDurationSpecified() {
        assertThat(items(SOME_FUNCTION, SOME_CONDITION).toString(),
                is("Some items from search context with condition Some condition. " +
                        "Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void descriptionOfMultipleItemsWithDurationSpecifiedInProperties() {
        System.setProperty(ELEMENT_WAITING_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(ELEMENT_WAITING_TIME_VALUE.getPropertyName(), "3");
        try {
            assertThat(items(SOME_FUNCTION, SOME_CONDITION).toString(),
                    is("Some items from search context with condition Some condition. " +
                            "Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(ELEMENT_WAITING_TIME_UNIT.getPropertyName());
            System.getProperties().remove(ELEMENT_WAITING_TIME_VALUE.getPropertyName());
        }
    }
}
