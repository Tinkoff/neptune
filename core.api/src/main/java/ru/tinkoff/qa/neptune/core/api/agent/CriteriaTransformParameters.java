package ru.tinkoff.qa.neptune.core.api.agent;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class CriteriaTransformParameters extends AbstractStepTransformParameters {

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return isStatic()
            .and(returns(hasSuperType(isSubTypeOf(Criteria.class)).or(is(Criteria.class))));
    }

    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return declaresMethod(methodMatcher());
    }

}
