package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;

import java.util.function.Function;

@MakeStringCapturesOnFinishing
@MakeCaptureOnFinishing(typeOfCapture = String.class)
@MakeCaptureOnFinishing(typeOfCapture = Number.class)
@MakeFileCapturesOnFinishing
class ArithmeticalSequence extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<CalculatorSteps, Number, Number, ArithmeticalSequence> {

    private ArithmeticalSequence(Function<Number, Number> originalFunction) {
        super(originalFunction);
    }

    @Description("Appending number {element}")
    public static ArithmeticalSequence appendToResultOf(@DescriptionFragment("element") Number toAppend, SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> from) {
        return new ArithmeticalSequence(number -> number.doubleValue() + toAppend.doubleValue())
                .from(from);
    }

    @Description("Subtraction of number {element}")
    public static ArithmeticalSequence subtractFromResultOf(@DescriptionFragment("element") Number toSubtract, SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> from) {
        return new ArithmeticalSequence(number -> number.doubleValue() - toSubtract.doubleValue())
                .from(from);
    }

    @Description("Multiplying by number {element}")
    public static ArithmeticalSequence multiplyByResultOf(@DescriptionFragment("element") Number toMultiply, SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> from) {
        return new ArithmeticalSequence(number -> number.doubleValue() * toMultiply.doubleValue())
                .from(from);
    }

    @Description("Divide by number {element}")
    public static ArithmeticalSequence divideByResultOf(@DescriptionFragment("element") Number toDivide, SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> from) {
        return new ArithmeticalSequence(number -> number.doubleValue() / toDivide.doubleValue())
                .from(from);
    }
}