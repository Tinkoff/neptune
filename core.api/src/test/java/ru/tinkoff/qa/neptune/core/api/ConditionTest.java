package ru.tinkoff.qa.neptune.core.api;

import org.testng.annotations.Test;

import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;

public class ConditionTest {

    private static Predicate<Number> IS_POSITIVE = number -> number.intValue() > 0;
    private static Predicate<Number> IS_NOT_FRACTION = number -> (number.floatValue() - number.intValue()) == 0;
    private static Predicate<Number> GREATER_THAN_TEN = number -> number.intValue() > 10;
    private static Predicate<Number> LOWER_THAN_TWENTY = number -> number.intValue() < 20;
    private static Predicate<Number> GREATER_THAN_FIFTEEN = number -> number.intValue() > 15;

    @Test
    public void testWhenPredicateANDIsNotDescribed() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        assertThat("Description of the resulted predicate",
                describedIsPositive.and(IS_NOT_FRACTION).toString(),
                is("Number should be positive"));
    }

    @Test
    public void negativeTestWhenPredicateORIsNotDescribed() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        assertThat("Description of the resulted predicate",
                describedIsPositive.or(IS_NOT_FRACTION).toString(),
                is("Number should be positive"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp =
            "Description should not be empty")
    public void negativeTestOfEmptyDescription() {
        condition("", IS_POSITIVE);
        fail("The exception throwing was expected");
    }

    @Test
    public void checkPredicateDescriptionAnd() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number shoid not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                describedIsPositive.and(describedIsNotFraction).toString(),
                is("Number should be positive, Number shoid not have decimal value"));
    }

    @Test
    public void checkPredicateDescriptionOr() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number shoid not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                describedIsPositive.or(describedIsNotFraction).toString(),
                is("(Number should be positive) or (Number shoid not have decimal value)"));
    }

    @Test
    public void checkPredicateDescriptionNot() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        assertThat("String value of the predicate",
                describedIsPositive.negate().toString(),
                is("not (Number should be positive)"));
    }

    @Test
    public void checkPredicateDescriptionComplex() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number shoid not have decimal value", IS_NOT_FRACTION);

        Predicate<Number> describedGreaterThan10 = condition("Number should be greater than 10", GREATER_THAN_TEN);
        Predicate<Number> describedGreaterThan15 = condition("Number should be greater than 15", GREATER_THAN_FIFTEEN);
        Predicate<Number> describedLowerThan20 = condition("Number should be lower than 20", LOWER_THAN_TWENTY);

        assertThat("String value of the predicate",
                describedIsPositive.and(describedIsNotFraction)
                        .and(describedGreaterThan10
                        .or(describedGreaterThan15))
                        .or(describedLowerThan20.negate()).toString(),
                is("(Number should be positive, Number shoid not have decimal value, " +
                        "(Number should be greater than 10) or (Number should be greater than 15)) or " +
                        "(not (Number should be lower than 20))"));
    }
}
