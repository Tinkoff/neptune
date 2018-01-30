package com.github.toy.constructor.selenium.api.widget.conditions;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Widget;

import java.util.Arrays;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class CommonConditions {

    private CommonConditions(){
        super();
    }

    /**
     * @param <T> type of the input value
     * @return predicate that checks visibility of element
     */
    public static <T extends Widget> Predicate<T> isVisible() {
        return condition("Should be visible on the page",
                Widget::isVisible);
    }

    /**
     * @param <T> type of the input value
     * @return predicate that checks is element enabled or not
     */
    public static <T extends Widget> Predicate<T> isEnabled() {
        return condition("Should be enabled",
                Widget::isEnabled);
    }

    /**
     * @param labels which can be used for the seeking element
     * @param <T> type of the input value
     * @return predicate that checks labels of an element
     */
    public static  <T extends Widget & Labeled> Predicate<T> hasLabels(String...labels) {
        checkNotNull(labels);
        checkArgument(labels.length > 0, "At least one label should be defined");
        return condition(format("Should have string label(s)", Arrays.toString(labels)),
                t -> t.labels().containsAll(asList(labels)));
    }

    /**
     * @param attribute of an element to check
     * @param attrValue desired value of the attribute
     * @param <T> type of the input value
     * @return predicate that checks value of the attribute.
     */
    public static <T extends Widget> Predicate<T> hasAttribute(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(!isBlank(attrValue), "Attribute value should not be empty or null.");

        return condition(format("Has attribute '%s=\"%s\"'", attribute, attrValue),
                t -> attrValue.equals(t.getAttribute(attribute)));
    }

    /**
     *
     * @param attribute of an element to check
     * @param attrValue substring which value of the attribute should contains
     * @param <T> <T> type of the input value
     * @return predicate that checks value of the attribute.
     */
    public static <T extends Widget> Predicate<T> attributeContains(String attribute, String attrValue) {
        checkArgument(!isBlank(attribute), "Attribute name should not be empty or null.");
        checkArgument(!isBlank(attrValue), "Attribute value should not be empty or null.");

        return condition(format("Should have attribute '%s' which conatains value '%s'", attribute, attrValue),
                t -> ofNullable(t.getAttribute(attribute)).map(s -> s.contains(attrValue)).orElse(false));
    }
}
