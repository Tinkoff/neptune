package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;

import java.math.BigDecimal;
import java.util.function.Function;

import static java.lang.String.format;

@MakeStringCapturesOnFinishing
@MakeCaptureOnFinishing(typeOfCapture = String.class)
@MakeFileCapturesOnFinishing
class Arithmetical extends SequentialGetStepSupplier.GetObjectStepSupplier<CalculatorSteps, Number, Arithmetical> {

    private Arithmetical(String description, Function<CalculatorSteps, Number> originalFunction) {
        super(description, originalFunction);
    }

    static Arithmetical number(Number number) {
        return new Arithmetical(format("Entering number %s", number), calculatorSteps -> calculatorSteps
                .setCalculated(calculatorSteps.get() + number.doubleValue()));
    }

    static Arithmetical append(Number toAppend) {
        return new Arithmetical(format("Appending number %s", toAppend), calculatorSteps -> calculatorSteps
                .setCalculated(calculatorSteps.get() + toAppend.doubleValue()));
    }

    static Arithmetical subtract(Number toSubtract) {
        return new Arithmetical(format("Subtract number %s", toSubtract), calculatorSteps -> calculatorSteps
                .setCalculated(calculatorSteps.get() - toSubtract.doubleValue()));
    }

    static Arithmetical multiply(Number toMultiply) {
        return new Arithmetical(format("Multiplying by number %s", toMultiply), calculatorSteps -> calculatorSteps
                .setCalculated(calculatorSteps.get() * toMultiply.doubleValue()));
    }

    static Arithmetical divide(Number toDivide) {
        return new Arithmetical(format("Divide by number %s", toDivide), calculatorSteps ->
                calculatorSteps.setCalculated(new BigDecimal(calculatorSteps.get())
                        .divide(new BigDecimal(toDivide.doubleValue())).doubleValue()));
    }
}
