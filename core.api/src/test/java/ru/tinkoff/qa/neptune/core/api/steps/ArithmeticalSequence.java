package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;

import java.util.function.Function;

import static java.lang.String.format;

@MakeStringCapturesOnFinishing
@MakeCaptureOnFinishing(typeOfCapture = String.class)
@MakeCaptureOnFinishing(typeOfCapture = Number.class)
@MakeFileCapturesOnFinishing
class ArithmeticalSequence extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<CalculatorSteps, Number, Number, ArithmeticalSequence> {

    private ArithmeticalSequence(String description, Function<Number, Number> originalFunction) {
        super(description, originalFunction);
    }

    static ArithmeticalSequence appendToResultOf(Number toAppend, SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> from) {
        return new ArithmeticalSequence(format("Appending number %s", toAppend),
                number -> number.doubleValue() + toAppend.doubleValue())
                .from(from);
    }

    static ArithmeticalSequence subtractFromResultOf(Number toSubtract, SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> from) {
        return new ArithmeticalSequence(format("Subtraction of number %s", toSubtract),
                number -> number.doubleValue() - toSubtract.doubleValue())
                .from(from);
    }

    static ArithmeticalSequence multiplyByResultOf(Number toMultiply, SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> from) {
        return new ArithmeticalSequence(format("Multiplying by number %s", toMultiply),
                number -> number.doubleValue() * toMultiply.doubleValue())
                .from(from);
    }

    static ArithmeticalSequence divideByResultOf(Number toDivide, SequentialGetStepSupplier<CalculatorSteps, Number, ?, ?, ?> from) {
        return new ArithmeticalSequence(format("Divide by number %s", toDivide),
                number -> number.doubleValue() / toDivide.doubleValue())
                .from(from);
    }
}