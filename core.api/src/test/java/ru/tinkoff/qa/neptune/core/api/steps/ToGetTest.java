package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.fail;

public class ToGetTest {

    private static final Function<Object, String> GET_TO_STRING = Object::toString;
    private static final Function<String, Integer> GET_STRING_LENGTH = String::length;
    private static final Function<Integer, Boolean> GET_POSITIVITY = integer -> integer.compareTo(0) > 0;

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "It seems given after-function doesn't describe any value to get. " +
                    "Use method StoryWriter.toGet")
    public void negativeTestWhenTheNextFunctionIsNotDescribed() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                GET_TO_STRING);
        describedToString.andThen(GET_STRING_LENGTH);
        fail("The exception throwing was expected");
    }


    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Description should not be empty string or null value")
    public void negativeTestOfEmptyDescription() {
        StoryWriter.toGet("", GET_TO_STRING);
        fail("The exception throwing was expected");
    }

    @Test
    public void checkDescriptionOfAFunctionThen() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                GET_TO_STRING);
        Function<String, Integer> describedStringLength = StoryWriter.toGet("Length of the given string",
                GET_STRING_LENGTH);

        assertThat("Sting value of the function",
                describedToString.andThen(describedStringLength).toString(),
                is("Length of the given string"));
    }

    @Test
    public void checkDescriptionOfAFunctionCompose() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                GET_TO_STRING);
        Function<String, Integer> describedStringLength = StoryWriter.toGet("Length of the given string",
                GET_STRING_LENGTH);

        assertThat("Sting value of the function",
                describedStringLength.compose(describedToString).toString(),
                is("Length of the given string"));
    }

    @Test
    public void checkDescriptionOfAFunctionComplex() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                GET_TO_STRING);
        Function<String, Integer> describedStringLength = StoryWriter.toGet("Length of the given string",
                GET_STRING_LENGTH);
        Function<Integer, Boolean> describedPositivityFunction =
                StoryWriter.toGet("Positivity of the calculated value", GET_POSITIVITY);

        System.out.println(describedStringLength.compose(describedToString).andThen(describedPositivityFunction).toString());
        assertThat("Sting value of the function",
                describedStringLength.compose(describedToString).andThen(describedPositivityFunction).toString(),
                is("Positivity of the calculated value"));
    }

    @Test
    public void checkNullPointerExceptionSafetyOnCompose() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                o -> null);
        Function<String, Integer> describedStringLength = StoryWriter.toGet("Length of the given string",
                GET_STRING_LENGTH);

        assertThat("Check that null is returned", describedStringLength.compose(describedToString)
                        .apply(new Object()),
                nullValue());
    }

    @Test
    public void checkNullPointerExceptionSafetyOnAndThan() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                o -> null);
        Function<String, Integer> describedStringLength = StoryWriter.toGet("Length of the given string",
                GET_STRING_LENGTH);

        assertThat("Check that null is returned", describedToString.andThen(describedStringLength)
                        .apply(new Object()),
                nullValue());
    }
}
