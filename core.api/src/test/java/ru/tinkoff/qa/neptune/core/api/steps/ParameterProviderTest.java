package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;

public class ParameterProviderTest {

    private static final Integer FIVE = 5;

    @Test
    public void testOfProviderOfEmptyParameters() {
        assertThat("Check created instance", EmptyStepClass.context, not(nullValue()));
    }

    @Test
    public void testOfProviderOfParameterizedParameters() {
        assertThat("Check injected value", ParameterizedStep.context.getNumber(), is(FIVE));
    }

    private static class EmptyStepClass extends Context<EmptyStepClass> {
        private static final EmptyStepClass context = getCreatedContextOrCreate(EmptyStepClass.class);
    }

    private static class ParameterizedParameterProvider implements ParameterProvider {
        @Override
        public Object[] provide() {
            return new Object[]{FIVE};
        }
    }

    @CreateWith(provider = ParameterizedParameterProvider.class)
    private static class ParameterizedStep extends Context<ParameterizedStep> {
        private final int number;

        private static final ParameterizedStep context = getCreatedContextOrCreate(ParameterizedStep.class);

        protected ParameterizedStep(int number) {
            this.number = number;
        }

        int getNumber() {
            return number;
        }
    }
}
