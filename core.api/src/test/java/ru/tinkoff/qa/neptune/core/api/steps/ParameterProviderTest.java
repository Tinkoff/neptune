package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.context.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class ParameterProviderTest {

    private static Integer FIVE = 5;

    @Test
    public void testOfProviderOfEmptyParameters() {
        assertThat("Check created instance", EmptyStepClass.context, not(nullValue()));
    }

    @Test
    public void testOfProviderOfParameterizedParameters() {
        assertThat("Check injected value", ParameterizedStep.context.getNumber(), is(FIVE));
    }

    private static class EmptyStepClass extends Context<EmptyStepClass> {
        private static final EmptyStepClass context = getInstance(EmptyStepClass.class);
    }

    private static class ParameterizedParameterProvider implements ParameterProvider {
        @Override
        public ConstructorParameters provide() {
            return ConstructorParameters.params(FIVE);
        }
    }

    @CreateWith(provider = ParameterizedParameterProvider.class)
    private static class ParameterizedStep extends Context<ParameterizedStep> {
        private final int number;

        private static final ParameterizedStep context = getInstance(ParameterizedStep.class);

        protected ParameterizedStep(int number) {
            this.number = number;
        }

        int getNumber() {
            return number;
        }
    }
}
