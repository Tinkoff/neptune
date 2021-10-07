package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.WebTestClient;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Check expectation:")
@Description("{description}")
class CheckWebTestClientExpectation<T> extends SequentialActionSupplier<WebTestClient.ResponseSpec,
        WebTestClient.ResponseSpec,
        CheckWebTestClientExpectation<T>> {

    @DescriptionFragment("description")
    private final Function<WebTestClient.ResponseSpec, T> assertion;

    public CheckWebTestClientExpectation(Function<WebTestClient.ResponseSpec, T> assertion) {
        checkNotNull(assertion);
        this.assertion = assertion;
    }

    static <T> CheckWebTestClientExpectation<T> checkExpectation(Function<WebTestClient.ResponseSpec, T> assertion) {
        return new CheckWebTestClientExpectation<>(assertion).performOn(spec -> spec);
    }

    @Override
    protected void howToPerform(WebTestClient.ResponseSpec value) {
        assertion.apply(value);
    }
}
