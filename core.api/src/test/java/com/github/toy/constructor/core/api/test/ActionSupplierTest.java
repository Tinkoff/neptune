package com.github.toy.constructor.core.api.test;

import com.github.toy.constructor.core.api.SequentalActionSupplier;
import org.testng.annotations.Test;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.fail;

public class ActionSupplierTest {
    private static final Function<Object, String> GET_TO_STRING = Object::toString;
    private static final Function<Object, String> GET_OBJECT_TO_SUBSTRING = o -> o.toString().substring(0, 2);

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function which gets value to perform action is not defined")
    public void negativeTestOfNullFunction() {
        new CleanStringAction().andThen((Function<Object, String>) null, "A", "B", "D");
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Object to perform action is not defined")
    public void negativeTestOfNullObject() {
        new CleanStringAction().andThen((String) null, "A", "B", "D");
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function should be described by the StoryWriter.toGet method.")
    public void negativeTestOfNotDescribedFunction() {
        new CleanStringAction().andThen(GET_TO_STRING, "A", "B", "D");
        fail("The exception throwing was expected");
    }

    @Test
    public void resultActionDescription() {
        assertThat("Description of result action",
                new CleanStringAction().andThen(toGet("String value of the object", GET_TO_STRING), "A", "B", "D")
                        .andThen(toGet("Substring of first 2 symbols taken from string value of the object", GET_OBJECT_TO_SUBSTRING),
                                "A", "B", "D")
                        .andThen("ABCD", "A", "B", "D")
                        .get().toString(), is("Clean string value of the object of given strings on String value of the object. " +
                        "With parameters: [A, B, D] \n and then -> " +
                        "\nClean string value of the object of given strings on " +
                        "Substring of first 2 symbols taken from string value of the object. With parameters: [A, B, D] " +
                        "\n and then -> \nClean string value ABCD of given strings on ABCD. With parameters: [A, B, D]"));
    }

    @Test
    public void resultActionDescription2() {
        assertThat("Description of result action",
                new CleanStringAction().andThen(toGet("String value of the object", GET_TO_STRING))
                        .andThen(toGet("Substring of first 2 symbols taken from string value of the object", GET_OBJECT_TO_SUBSTRING))
                        .andThen("ABCD")
                        .get().toString(), is("Clean string value of the object of given strings on String value of the object " +
                        "\n and then -> " +
                        "\nClean string value of the object of given strings on " +
                        "Substring of first 2 symbols taken from string value of the object " +
                        "\n and then -> " +
                        "\nClean string value ABCD of given strings on ABCD"));
    }

    static class CleanStringAction extends SequentalActionSupplier<Object, String, CleanStringAction> {

        public CleanStringAction() {
            super();
        }

        //this method was added for the unit testing
        CleanStringAction andThen(Function<Object, String> function, String...strings) {
            return super.andThen("Clean string value of the object of given strings",
                    function, strings);
        }

        //this method was added for the unit testing
        CleanStringAction andThen(String string, String...strings) {
            return super.andThen("Clean string value " + string + " of given strings",
                    string, strings);
        }

        @Override
        protected void performActionOn(String value, Object... additionalArgument) {
            for (Object o: additionalArgument) {
                value.replace(o.toString(), "");
            }
        }
    }
}
