package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;

import java.util.function.Function;

import static java.lang.String.format;

@MakeStringCapturesOnFinishing
@MakeCaptureOnFinishing(typeOfCapture = String.class)
@MakeFileCapturesOnFinishing
abstract class ArithmeticalSequence extends SequentialGetStepSupplier<CalculatorSteps, Number, Number, ArithmeticalSequence> {

    private static ArithmeticalSequence getResulted(ArithmeticalSequence sequence,
                                                    GetStepSupplier<CalculatorSteps, Number, ?> supplier) {
        return sequence.from(supplier);
    }

    static ArithmeticalSequence appendToResultOf(Number toAppend, GetStepSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return StoryWriter.toGet(format("Appending number %s", toAppend),
                        number -> number.doubleValue() + toAppend.doubleValue());
            }
        }, result);
    }

    static ArithmeticalSequence subtractFromResultOf(Number toSubtract, GetStepSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return StoryWriter.toGet(format("Subtract number %s", toSubtract), number -> number.doubleValue() - toSubtract.doubleValue());
            }
        }, result);
    }

    static ArithmeticalSequence multiplyByResultOf(Number toMultiply, GetStepSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return StoryWriter.toGet(format("Multiplying by number %s", toMultiply), number -> number.doubleValue() * toMultiply.doubleValue());
            }
        }, result);
    }

    static ArithmeticalSequence divideByResultOf(Number toDivide, GetStepSupplier<CalculatorSteps, Number, ?> result) {
        return getResulted(new ArithmeticalSequence() {
            @Override
            protected Function<Number, Number> getEndFunction() {
                return StoryWriter.toGet(format("Divide by number %s", toDivide), number -> number.doubleValue() / toDivide.doubleValue());
            }
        }, result);
    }
}