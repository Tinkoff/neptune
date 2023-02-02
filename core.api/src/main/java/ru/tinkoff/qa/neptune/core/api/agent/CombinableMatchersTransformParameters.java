package ru.tinkoff.qa.neptune.core.api.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.matcher.ElementMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.hamcrest.Matchers.describedAs;
import static ru.tinkoff.qa.neptune.core.api.agent.HamcrestLocalizationHelper.calculateDescription;
import static ru.tinkoff.qa.neptune.core.api.agent.HamcrestLocalizationHelper.isLocalizationUsed;

public class CombinableMatchersTransformParameters implements NeptuneTransformParameters {

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return returns(isSubTypeOf(Matcher.class));
    }

    @Override
    public Class<?> interceptor() {
        return HamcrestCombinableInterceptor.class;
    }

    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return is(CombinableMatcher.class)
            .or(is(CombinableMatcher.CombinableBothMatcher.class))
            .or(is(CombinableMatcher.CombinableEitherMatcher.class));
    }

    @SuppressWarnings("unchecked")
    public static class HamcrestCombinableInterceptor {

        @Advice.OnMethodExit
        public static void interceptExit(@Advice.This Object target,
                                         @Advice.Origin Method method,
                                         @Advice.AllArguments Object[] args,
                                         @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned) throws Throwable {
            if (!isLocalizationUsed()) {
                return;
            }

            var newDescription = calculateDescription((Matcher<?>) returned, method, args);

            if (target instanceof Matcher) {
                returned = new CombinableMatcher<>(describedAs("(" + target + ") " + newDescription, (Matcher<Object>) returned));
            } else {
                var f = target.getClass().getDeclaredField("first");
                f.setAccessible(true);
                var first = f.get(target);
                returned = new CombinableMatcher<>(describedAs(first + " " + newDescription, (Matcher<Object>) returned));
            }
        }

    }
}
