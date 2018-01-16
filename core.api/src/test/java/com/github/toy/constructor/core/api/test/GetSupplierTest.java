package com.github.toy.constructor.core.api.test;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.core.api.SequentialGetSupplier;
import org.testng.annotations.Test;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.test.GetSupplierTest.Cube.cube;
import static com.github.toy.constructor.core.api.test.GetSupplierTest.SqrtFromSomeNumericOperation.sqrt;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.fail;

public class GetSupplierTest {

    private static final Function<Number, Double> DOUBLE_CUBE_FUNCTION =
            number -> number.doubleValue() *
                    number.doubleValue() *
                    number.doubleValue();

    @Test
    public void resultFunctionDescriptionWithSupplier() {
        assertThat("Result description",
                sqrt(cube(777)).get().toString(),
                is("Sqrt value from (Cube value of " + 777 +")"));
    }


    @Test
    public void resultFunctionDescriptionWithValue() {
        assertThat("Result description",
                sqrt(777D).get().toString(),
                is("Sqrt value from (" + 777.0 + ")"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function to get value from was not defined")
    public void negativeTestOfNullFunction() {
        sqrt().from((Function<Number, Double>) null);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function to get value from is not described. Use method StoryWriter.toGet to describe it.")
    public void negativeTestOfNotDescribedFunction() {
        sqrt().from(DOUBLE_CUBE_FUNCTION);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "The supplier of the function is not defined")
    public void negativeTestOfNullGetSupplier() {
        sqrt().from((GetSupplier<Number, Double, ?>) null);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "The object to get value from is not defined")
    public void negativeTestOfNullObjectFrom() {
        sqrt().from((Double) null);
        fail("The exception throwing was expected");
    }

    static class Cube extends GetSupplier<Number, Double, Cube> {
        static Cube cube(Number origin) {
            return new Cube().set(toGet(format("Cube value of %s", origin), DOUBLE_CUBE_FUNCTION));
        }
    }

    static class SqrtFromSomeNumericOperation extends SequentialGetSupplier<Number, Double, Double, SqrtFromSomeNumericOperation> {

        //this method was added for the unit testing
        static SqrtFromSomeNumericOperation sqrt() {
            return new SqrtFromSomeNumericOperation();
        }

        static SqrtFromSomeNumericOperation sqrt(GetSupplier<Number, Double, ?> numericOperation) {
            return new SqrtFromSomeNumericOperation().from(numericOperation);
        }

        static SqrtFromSomeNumericOperation sqrt(Double doubleValue) {
            return new SqrtFromSomeNumericOperation().from(doubleValue);
        }

        @Override
        protected Function<Double, Double> getEndFunction() {
            return toGet("Sqrt value", Math::sqrt);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(GetSupplier<Number, Double, ?> numericOperation) {
            return super.from(numericOperation);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(Double doubleValue) {
            return super.from(doubleValue);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(Function<Number, Double> function) {
            return super.from(function);
        }
    }
}
