package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;

import java.util.function.Consumer;

import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.action;

public class ActionTest {
    private static final Consumer<String> REPLACE_SPACE = s -> s.replace(" ", "");
    private static final Consumer<String> REPLACE_A = s -> s.replace("A", "");

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "It seems given consumer doesn't describe any after-action. " +
                    "Use method StoryWriter.action to describe the after-action to perform")
    public void negativeTestNextActionIsNotDescribed() {
        Consumer<String> describedSpaceReplacing = action("Replace spaces from the string", REPLACE_SPACE);
        describedSpaceReplacing.andThen(REPLACE_A);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Description should not be empty")
    public void negativeTestOfEmptyDescription() {
        action("", REPLACE_SPACE);
        fail("The exception throwing was expected");
    }
}
