package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.GetStep;
import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.core.api.proxy.ConstructorParameters;
import com.github.toy.constructor.core.api.proxy.CreateWith;
import com.github.toy.constructor.core.api.proxy.ParameterProvider;
import com.github.toy.constructor.core.api.proxy.ProviderOfEmptyParameters;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class ParameterProviderTest {

    private static Integer FIVE = 5;

    @Test
    public void testOfProviderOfEmptyParameters() throws Exception {
        EmptyStepClass emptyStep = getSubstituted(EmptyStepClass.class);
        assertThat("Check created instance", emptyStep, not(nullValue()));
    }

    @Test
    public void testOfProviderOfParameterizedParameters() throws Exception {
        ParameterizedStep parameterizedStep = getSubstituted(ParameterizedStep.class);
        assertThat("Check injected value", parameterizedStep.getNumber(), is(FIVE));
    }

    @CreateWith(provider = ProviderOfEmptyParameters.class)
    private static class EmptyStepClass implements PerformStep<EmptyStepClass> {

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

        private ParameterizedStep(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }
}
