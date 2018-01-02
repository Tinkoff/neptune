package com.github.toy.constructor.core.api.test;

import org.testng.annotations.Test;

import java.util.function.Consumer;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.AssertJUnit.fail;

public class ActionTest {
    private static final Consumer<String> REPLACE_SPACE = s -> s.replace(" ", "");
    private static final Consumer<String> REPLACE_A = s -> s.replace("A", "");

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "It seems given consumer doesn't describe any action. " +
                    "Use method StoryWriter.action to describe the after-action.")
    public void negativeTestNextActionIsNotDescribed() {
        Consumer<String> describedSpaceReplacing = action("Replace spaces from the string", REPLACE_SPACE);
        describedSpaceReplacing.andThen(REPLACE_A);
        fail("The exception thowing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Description should not be empty")
    public void negativeTestOfEmptyDescription() {
        action("", REPLACE_SPACE);
        fail("The exception thowing was expected");
    }

    @Test
    public void checkDescriptionOfAnAction() {
        Consumer<String> describedSpaceReplacing = action("Replace spaces from the string", REPLACE_SPACE);
        Consumer<String> describedAReplacing = action("Replace A characters from the string", REPLACE_A);

        assertThat("String value of the consumer",
                describedSpaceReplacing.andThen(describedAReplacing).andThen(describedSpaceReplacing).toString(),
                is("Replace spaces from the string \n and then -> \nReplace A characters from the string \n and then -> " +
                        "\nReplace spaces from the string")
        );
    }
}
