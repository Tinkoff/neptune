package ru.tinkoff.qa.neptune.check.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.matcher.ElementMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.core.DescribedAs;
import ru.tinkoff.qa.neptune.core.api.agent.NeptuneTransformParameters;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static ru.tinkoff.qa.neptune.check.agent.HamcrestLocalizationHelper.calculateDescription;
import static ru.tinkoff.qa.neptune.check.agent.HamcrestLocalizationHelper.isLocalizationUsed;

public class HamcrestTransformParameters implements NeptuneTransformParameters {

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return returns(isSubTypeOf(Matcher.class)).and(isStatic());
    }

    @Override
    public Class<?> interceptor() {
        return HamcrestInterceptor.class;
    }

    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return nameStartsWith("org.hamcrest.")
            .or(nameStartsWith("org.exparity.hamcrest."))
            .and(not(is(Matchers.class))
                .and(not(is(CombinableMatcher.class)))
                .and(not(is(CombinableMatcher.CombinableBothMatcher.class)))
                .and(not(is(CombinableMatcher.CombinableEitherMatcher.class)))
                .and(not(is(DescribedAs.class))));
    }

    public static class HamcrestInterceptor {

        @Advice.OnMethodExit
        public static void interceptExit(
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] args,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned) {
            if (isLocalizationUsed()) {
                var newDescription = calculateDescription((Matcher<?>) returned, method, args);
                returned = Matchers.describedAs(newDescription, (Matcher<?>) returned);
            }
        }
    }
}
