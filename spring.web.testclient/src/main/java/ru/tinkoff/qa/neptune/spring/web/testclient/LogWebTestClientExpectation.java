package ru.tinkoff.qa.neptune.spring.web.testclient;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static java.util.Objects.nonNull;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Check expectation:")
final class LogWebTestClientExpectation extends SequentialActionSupplier<Expectation<?>,
        Expectation<?>,
        LogWebTestClientExpectation> {

    private LogWebTestClientExpectation() {
        super();
    }

    @Description("{description}")
    static <T> LogWebTestClientExpectation logExpectation(Expectation<T> expectation) {
        return new LogWebTestClientExpectation().performOn(expectation);
    }

    @Override
    protected void howToPerform(Expectation<?> value) {
        var t = value.getThrowable();
        if (nonNull(t)) {
            if (t instanceof AssertionError) {
                throw (AssertionError) t;
            }

            throw new AssertionError(t.getMessage(), t);
        }
    }
}
