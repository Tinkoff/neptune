package ru.tinkoff.qa.neptune.core.api;

import org.testng.annotations.Test;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

public class ActionTest {
    private static final Consumer<String> REPLACE_SPACE = s -> s.replace(" ", "");
    private static final Consumer<String> REPLACE_A = s -> s.replace("A", "");

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "It seems given consumer doesn't describe any after-action. " +
                    "Use method StoryWriter.action to describe the after-action or override the toString method")
    public void negativeTestNextActionIsNotDescribed() {
        Consumer<String> describedSpaceReplacing = StoryWriter.action("Replace spaces from the string", REPLACE_SPACE);
        describedSpaceReplacing.andThen(REPLACE_A);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Description should not be empty")
    public void negativeTestOfEmptyDescription() {
        StoryWriter.action("", REPLACE_SPACE);
        fail("The exception throwing was expected");
    }

    @Test
    public void checkDescriptionOfAnAction() {
        Consumer<String> describedSpaceReplacing = StoryWriter.action("Replace spaces from the string", REPLACE_SPACE);
        Consumer<String> describedAReplacing = StoryWriter.action("Replace A characters from the string", REPLACE_A);

        assertThat("String value of the consumer",
                describedSpaceReplacing.andThen(describedAReplacing).andThen(describedSpaceReplacing).toString(),
                is("Replace spaces from the string.\n\t  " +
                        "And then Replace A characters from the string.\n\t  " +
                        "And then Replace spaces from the string")
        );
    }
}
