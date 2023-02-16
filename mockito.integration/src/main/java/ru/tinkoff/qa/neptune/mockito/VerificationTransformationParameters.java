package ru.tinkoff.qa.neptune.mockito;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.internal.MockedStaticImpl;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.verification.VerificationMode;
import ru.tinkoff.qa.neptune.core.api.agent.NeptuneTransformParameters;

import java.lang.reflect.Method;
import java.util.LinkedList;

import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static ru.tinkoff.qa.neptune.mockito.VerificationExecutionData.setMode;
import static ru.tinkoff.qa.neptune.mockito.VerificationExecutionData.setName;

public class VerificationTransformationParameters implements NeptuneTransformParameters {

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return named("verify");
    }

    @Override
    public Class<?> interceptor() {
        return VerificationInterceptor.class;
    }

    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return is(Mockito.class)
            .or(isSubTypeOf(InOrder.class))
            .or(is(MockedStaticImpl.class));
    }

    public static class VerificationInterceptor {

        public static final ThreadLocal<LinkedList<Method>> STACK = ThreadLocal.withInitial(LinkedList::new);

        @Advice.OnMethodEnter(inline = false)
        public static void interceptEnter(@Advice.Origin Method invoked,
                                          @Advice.AllArguments Object[] params) {
            if (STACK.get().isEmpty()) {
                var declares = invoked.getDeclaringClass();
                if (InOrder.class.isAssignableFrom(declares)) {
                    setName(new VerifyInvocationInOrder());
                } else {
                    setName(new VerifyInvocation());
                }

                var mode = stream(params)
                    .filter(p -> ((p instanceof VerificationMode) || (p instanceof VerificationInOrderMode)))
                    .findFirst()
                    .orElse(null);

                if (nonNull(mode)) {
                    setMode(mode);
                }
            }

            STACK.get().addLast(invoked);
        }

        @Advice.OnMethodExit(inline = false)
        public static void interceptExit(@Advice.Origin Method invoked) {
            STACK.get().removeLast();
        }
    }
}
