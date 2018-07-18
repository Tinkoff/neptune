package ru.tinkoff.qa.neptune.core.api;

import org.testng.annotations.Test;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.GetSupplierTest.Cube.cube;
import static ru.tinkoff.qa.neptune.core.api.GetSupplierTest.SqrtFromSomeNumericOperation.sqrt;
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
                is("Sqrt value"));
    }


    @Test
    public void resultFunctionDescriptionWithValue() {
        assertThat("Result description",
                sqrt(777D).get().toString(),
                is("Sqrt value"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function to get value from was not defined")
    public void negativeTestOfNullFunction() {
        sqrt().from((Function<Number, Double>) null);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "The supplier of the function is not defined")
    public void negativeTestOfNullGetSupplier() {
        sqrt().from((GetStepSupplier<Number, Double, ?>) null);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "The object to get value from is not defined")
    public void negativeTestOfNullObjectFrom() {
        sqrt().from((Double) null);
        fail("The exception throwing was expected");
    }

    static class Cube extends GetStepSupplier<Number, Double, Cube> {
        static Cube cube(Number origin) {
            return new Cube().set(StoryWriter.toGet(format("Cube value of %s", origin), DOUBLE_CUBE_FUNCTION));
        }
    }

    static class SqrtFromSomeNumericOperation extends SequentialGetStepSupplier<Number, Double, Double, SqrtFromSomeNumericOperation> {

        //this method was added for the unit testing
        static SqrtFromSomeNumericOperation sqrt() {
            return new SqrtFromSomeNumericOperation();
        }

        static SqrtFromSomeNumericOperation sqrt(GetStepSupplier<Number, Double, ?> numericOperation) {
            return new SqrtFromSomeNumericOperation().from(numericOperation);
        }

        static SqrtFromSomeNumericOperation sqrt(Double doubleValue) {
            return new SqrtFromSomeNumericOperation().from(doubleValue);
        }

        @Override
        protected Function<Double, Double> getEndFunction() {
            return StoryWriter.toGet("Sqrt value", Math::sqrt);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(GetStepSupplier<Number, ? extends Double, ?> numericOperation) {
            return super.from(numericOperation);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(Double doubleValue) {
            return super.from(doubleValue);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(Function<Number, ? extends Double> function) {
            return super.from(function);
        }
    }
}
