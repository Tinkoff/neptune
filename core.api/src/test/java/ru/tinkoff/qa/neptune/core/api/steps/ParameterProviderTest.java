package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.context.*;
import ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class ParameterProviderTest {

    private static Integer FIVE = 5;

    @Test
    public void testOfProviderOfEmptyParameters() {
        EmptyStepClass emptyStep = ProxyFactory.getProxied(EmptyStepClass.class);
        assertThat("Check created instance", emptyStep, not(nullValue()));
    }

    @Test
    public void testOfProviderOfParameterizedParameters() {
        ParameterizedStep parameterizedStep = ProxyFactory.getProxied(ParameterizedStep.class);
        assertThat("Check injected value", parameterizedStep.getNumber(), is(FIVE));
    }

    @CreateWith(provider = ProviderOfEmptyParameters.class)
    private static class EmptyStepClass implements ActionStepContext<EmptyStepClass> {
        protected EmptyStepClass() {
            super();
        }
    }

    private static class ParameterizedParameterProvider implements ParameterProvider {
        @Override
        public ConstructorParameters provide() {
            return ConstructorParameters.params(FIVE);
        }
    }

    @CreateWith(provider = ParameterizedParameterProvider.class)
    private static class ParameterizedSuperStep implements GetStepContext<ParameterizedStep> {
        private final int number;

        protected ParameterizedSuperStep(int number) {
            this.number = number;
        }

        int getNumber() {
            return number;
        }
    }

    private static class ParameterizedStep extends ParameterizedSuperStep {
        protected ParameterizedStep(int number) {
            super(number);
        }
    }
}
