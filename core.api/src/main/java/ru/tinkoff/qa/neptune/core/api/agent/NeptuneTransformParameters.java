package ru.tinkoff.qa.neptune.core.api.agent;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;


public interface NeptuneTransformParameters {
    /**
     * @return The matcher identifying methods to apply the advice to.
     * Returned value may not be null.
     */
    ElementMatcher<? super MethodDescription> methodMatcher();

    /**
     * @return class of interceptor is used for advice
     * Returned value may not be null.
     */
    Class<?> interceptor();

    /**
     * @return The matcher identifying types to apply the advice to.
     */
    ElementMatcher<? super TypeDescription> typeMatcher();
}