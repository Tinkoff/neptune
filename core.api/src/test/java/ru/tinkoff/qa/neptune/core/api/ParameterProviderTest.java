package ru.tinkoff.qa.neptune.core.api;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.proxy.ProxyFactory;

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
    private static class EmptyStepClass implements PerformActionStep<EmptyStepClass> {
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
    private static class ParameterizedStep implements GetStep<ParameterizedStep> {

        private final int number;

        protected ParameterizedStep(int number) {
            this.number = number;
        }

        int getNumber() {
            return number;
        }
    }
}
