package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;

import java.math.BigDecimal;
import java.util.function.Function;

@CaptureOnSuccess(by = {TestNumberCaptor.class, TestStringCaptor.class, TestFileCaptor.class})
@CaptureOnFailure(by = {TestNumberCaptor.class, TestStringCaptor.class, TestCaptor.class})
@MaxDepthOfReporting(2)
class Arithmetical extends SequentialGetStepSupplier.GetObjectStepSupplier<CalculatorSteps, Number, Arithmetical> {

    private Arithmetical(Function<CalculatorSteps, Number> originalFunction) {
        super(originalFunction);
    }

    @Description("Entering number {number}")
    public static Arithmetical number(@DescriptionFragment("number") Number number) {
        return new Arithmetical(calculatorSteps -> calculatorSteps
                .setCalculated(calculatorSteps.get() + number.doubleValue()));
    }

    @Description("Appending number {toAppend}")
    public static Arithmetical append(@DescriptionFragment("toAppend") Number toAppend) {
        return new Arithmetical(calculatorSteps -> calculatorSteps
                .setCalculated(calculatorSteps.get() + toAppend.doubleValue()));
    }

    @Description("Subtract number {toSubtract}")
    public static Arithmetical subtract(@DescriptionFragment("toSubtract") Number toSubtract) {
        return new Arithmetical(calculatorSteps -> calculatorSteps
                .setCalculated(calculatorSteps.get() - toSubtract.doubleValue()));
    }

    @Description("Multiplying by number {toMultiply}")
    public static Arithmetical multiply(@DescriptionFragment("toMultiply") Number toMultiply) {
        return new Arithmetical(calculatorSteps -> calculatorSteps
                .setCalculated(calculatorSteps.get() * toMultiply.doubleValue()));
    }

    @Description("Divide by number {toDivide}")
    public static Arithmetical divide(@DescriptionFragment("toDivide") Number toDivide) {
        return new Arithmetical(calculatorSteps -> calculatorSteps.setCalculated(new BigDecimal(calculatorSteps.get())
                .divide(new BigDecimal(toDivide.doubleValue())).doubleValue()));
    }
}
