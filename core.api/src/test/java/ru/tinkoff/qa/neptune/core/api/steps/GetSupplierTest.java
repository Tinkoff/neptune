package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.pow;
import static ru.tinkoff.qa.neptune.core.api.steps.GetSupplierTest.Cube.cube;
import static ru.tinkoff.qa.neptune.core.api.steps.GetSupplierTest.SqrtFromSomeNumericOperation.sqrt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;

public class GetSupplierTest {

    private static final Function<Number, Number> DOUBLE_CUBE_FUNCTION =
            number -> number.doubleValue() *
                    number.doubleValue() *
                    number.doubleValue();

    @Test
    public void resultFunctionDescriptionWithSupplier() {
        var cube = cube(777);
        assertThat("Result description",
                sqrt(cube).get().toString(),
                is("Sqrt value. From Cube value"));

        assertThat("Description of mediator function",
                cube.get().toString(),
                is("Cube value. From 777"));
    }

    @Test
    public void resultFunctionDescriptionWithChainedSupplier() {
        var sqrt = sqrt(444D);
        var cubeFromSqrt = cube(sqrt);
        assertThat("Result description",
                sqrt(cubeFromSqrt).get().toString(),
                is("Sqrt value. From Cube value"));

        assertThat("Description of mediator function",
                cubeFromSqrt.get().toString(),
                is("Cube value. From Sqrt value"));

        assertThat("Description of mediator function",
                sqrt.get().toString(),
                is("Sqrt value. From 444.0"));
    }

    @Test
    public void resultFunctionDescriptionWithCondition() {
        assertThat("Result description",
                sqrt(cube(777))
                        .criteria("is positive", number -> number.doubleValue() > 0)
                        .criteria("greater than 200", number -> number.doubleValue() > 200).get().toString(),
                is("Sqrt value [Criteria: is positive, greater than 200]. From Cube value"));
    }

    @Test
    public void resultFunctionDescriptionWithChainedConditionalSupplier() {
        var conditionalCube = cube(777)
                .criteria("is positive", number -> number.doubleValue() > 0)
                .criteria("greater than 200", number -> number.doubleValue() > 200);

        assertThat("Result description",
                sqrt(conditionalCube)
                        .criteria("is positive", number -> number.doubleValue() > 0)
                        .criteria("greater than 200", number -> number.doubleValue() > 200).get().toString(),
                is("Sqrt value [Criteria: is positive, greater than 200]. From Cube value"));

        assertThat("Description of mediator function",
                conditionalCube.get().toString(),
                is("Cube value [Criteria: is positive, greater than 200]. From 777"));
    }

    @Test
    public void resultFunctionDescriptionWithValue() {
        assertThat("Result description",
                sqrt(777D).get().toString(),
                is("Sqrt value. From 777.0"));
    }

    @Test
    public void resultFunctionDescriptionWithNotDescribedFunction() {
        assertThat("Result description",
                sqrt(number -> pow(number.doubleValue(), 2D)).get().toString(),
                is("Sqrt value"));
    }

    @Test
    public void resultFunctionDescriptionWithDescribedFunction() {
        assertThat("Result description",
                sqrt(toGet("Sqr value", number -> pow(number.doubleValue(), 2D))).get().toString(),
                is("Sqrt value. From Sqr value"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Function to get value from is not defined")
    public void negativeTestOfNullFunction() {
        sqrt().from((Function<Number, Double>) null);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "The supplier of a function is not defined")
    public void negativeTestOfNullGetSupplier() {
        sqrt().from((SequentialGetStepSupplier<Number, Double, ?, ?, ?>) null);
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "The object to get value from is not defined")
    public void negativeTestOfNullObjectFrom() {
        sqrt().from((Double) null);
        fail("The exception throwing was expected");
    }

    static class Cube extends SequentialGetStepSupplier.GetObjectStepSupplier<Number, Number, Cube> {
        Cube() {
            super("Cube value", DOUBLE_CUBE_FUNCTION);
        }

        static Cube cube(Number origin) {
            return new Cube().from(origin);
        }

        static Cube cube(SequentialGetStepSupplier<Number, Number, ?, ?, ?> from) {
            return new Cube().from(from);
        }

        protected Cube criteria(String description, Predicate<Number> numberPredicate) {
            return super.criteria(description, numberPredicate);
        }
    }

    static class SqrtFromSomeNumericOperation
            extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<Number, Number, Number, SqrtFromSomeNumericOperation> {

        SqrtFromSomeNumericOperation() {
            super("Sqrt value", number -> Math.sqrt(number.doubleValue()));
        }

        //this method was added for the unit testing
        static SqrtFromSomeNumericOperation sqrt() {
            return new SqrtFromSomeNumericOperation();
        }

        static SqrtFromSomeNumericOperation sqrt(SequentialGetStepSupplier<Number, Number, ?, ?, ?> numericOperation) {
            return new SqrtFromSomeNumericOperation().from(numericOperation);
        }

        static SqrtFromSomeNumericOperation sqrt(Function<Number, Double> numericOperation) {
            return new SqrtFromSomeNumericOperation().from(numericOperation);
        }

        static SqrtFromSomeNumericOperation sqrt(Double doubleValue) {
            return new SqrtFromSomeNumericOperation().from(doubleValue);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(SequentialGetStepSupplier<Number, ? extends Number, ?, ?, ?> numericOperation) {
            return super.from(numericOperation);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(Number numberValue) {
            return super.from(numberValue);
        }

        //this method was overridden for the unit testing
        @Override
        protected SqrtFromSomeNumericOperation from(Function<Number, ? extends Number> function) {
            return super.from(function);
        }

        protected SqrtFromSomeNumericOperation criteria(String description, Predicate<Number> numberPredicate) {
            return super.criteria(description, numberPredicate);
        }
    }
}
