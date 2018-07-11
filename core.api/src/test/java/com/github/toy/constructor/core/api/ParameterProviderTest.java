package com.github.toy.constructor.core.api;

import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class ParameterProviderTest {

    private static Integer FIVE = 5;

    @Test
    public void testOfProviderOfEmptyParameters() {
        EmptyStepClass emptyStep = getSubstituted(EmptyStepClass.class);
        assertThat("Check created instance", emptyStep, not(nullValue()));
    }

    @Test
    public void testOfProviderOfParameterizedParameters() {
        ParameterizedStep parameterizedStep = getSubstituted(ParameterizedStep.class);
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
            return params(FIVE);
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
