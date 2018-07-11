package com.github.toy.constructor.core.api;

import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.fail;

public class NegativeTest {

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Class to substitute should be assignable from " +
                    "com.github.toy.constructor.core.api.GetStep " +
                    "and/or com.github.toy.constructor.core.api.PerformActionStep.")
    public void testOfIllegalClass() {
        getSubstituted(Object.class, params());
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = NoSuchMethodException.class)
    public void testOfCompletelyMismatchingParameters() {
        getSubstituted(GetStepStub.class, params()).get(toGet("Something", getStep -> new Object()));
        fail("The exception throwing was expected");
    }

    @Test(expectedExceptions = NoSuchMethodException.class,
            dependsOnMethods = "testOfCompletelyMismatchingParameters")
    public void testOfPartiallyMismatchingParameters() {
        getSubstituted(GetStepStub.class, params("12345", 1, 1.5F, false))
                .get(toGet("Something", getStep -> new Object()));
        fail("The exception throwing was expected");
    }

    @Test(dependsOnMethods = {"testOfIllegalClass",
            "testOfCompletelyMismatchingParameters",
            "testOfPartiallyMismatchingParameters"})
    public void positiveTest() {
        assertThat(getSubstituted(GetStepStub.class, params("12345", 1, 1, false))
                .get(toGet("Something", getStep -> new Object())), not(nullValue()));
    }

    static class GetStepStub implements GetStep<GetStepStub>, PerformActionStep<GetStepStub> {
        private final CharSequence sequence;
        private final Integer integer2;
        private final int integer1;
        private final boolean b;

       GetStepStub(CharSequence sequence, int integer1, Integer integer2, boolean b) {
            this.sequence = sequence;
            this.integer1 = integer1;
            this.integer2 = integer2;
            this.b = b;
        }
    }
}
