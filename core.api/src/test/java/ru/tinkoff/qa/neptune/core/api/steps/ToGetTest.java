package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

public class ToGetTest {

    private static final Function<Object, String> GET_TO_STRING = Object::toString;
    private static final Function<String, Integer> GET_STRING_LENGTH = String::length;
    private static final Function<Integer, Boolean> GET_POSITIVITY = integer -> integer.compareTo(0) > 0;

    @Test
    public void testWhenTheNextFunctionIsNotDescribed() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                GET_TO_STRING);
        assertThat(describedToString.andThen(GET_STRING_LENGTH).toString(),
                is("<not described value>"));
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
    public void checkDescriptionOfAFunctionComposeBeforeIsNotDescribed() {
        Function<String, Integer> describedStringLength = StoryWriter.toGet("Length of the given string",
                GET_STRING_LENGTH);

        var result = describedStringLength.compose(GET_TO_STRING);

        assertThat("Sting value of the function",
                result.toString(),
                is("Length of the given string"));
        assertThat(result.getClass(), is(StepFunction.class));
    }

    @Test
    public void checkDescriptionOfAFunctionChainedComposeBeforeIsNotDescribed() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                GET_TO_STRING);
        Function<String, Integer> describedStringLength = StoryWriter.toGet("Length of the given string",
                GET_STRING_LENGTH);

        var result = describedStringLength.compose(describedToString).compose(s -> s);

        assertThat("Sting value of the function",
                result.toString(),
                is("Length of the given string"));
        assertThat(result.getClass(), is(StepFunction.SequentialStepFunction.class));
        assertThat(((StepFunction.SequentialStepFunction) result).sequence.size(), is(3));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkDescriptionOfAFunctionChainedAndThenAfterIsNotDescribed() {
        Function<Object, String> describedToString = StoryWriter.toGet("String value of the object",
                GET_TO_STRING);
        Function<String, Integer> describedStringLength = StoryWriter.toGet("Length of the given string",
                GET_STRING_LENGTH);

        var result = describedStringLength.compose(describedToString).andThen(s -> s);

        assertThat("Sting value of the function",
                result.toString(),
                is("<not described value>"));
        assertThat(result.getClass(), is(StepFunction.SequentialStepFunction.class));
        assertThat(((StepFunction.SequentialStepFunction) result).sequence.size(), is(3));
        assertThat(new ArrayList<>(((StepFunction.SequentialStepFunction) result).sequence).get(2).toString(),
                is("<not described value>"));
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
