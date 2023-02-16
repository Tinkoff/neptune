package ru.tinkoff.qa.neptune.mockito;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.mockito.internal.verification.NoInteractions;
import org.mockito.internal.verification.NoMoreInteractions;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.verification.VerificationMode;
import ru.tinkoff.qa.neptune.core.api.agent.NeptuneTransformParameters;

import java.util.LinkedList;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static ru.tinkoff.qa.neptune.mockito.VerificationExecutionData.*;

public class VerificationModeTransformationParameters implements NeptuneTransformParameters {
    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return isPublic()
            .and(not(isStatic()))
            .and(named("verify").or(named("verifyInOrder")))
            .and(
                takesArgument(0, isSubTypeOf(VerificationData.class))
                    .or(takesArgument(0, isSubTypeOf(VerificationDataInOrder.class)))
            );
    }

    @Override
    public Class<?> interceptor() {
        return VerificationModeInterceptor.class;
    }

    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return isSubTypeOf(VerificationMode.class)
            .or(isSubTypeOf(VerificationInOrderMode.class))
            .and(not(is(NoInteractions.class)))
            .and(not(is(NoMoreInteractions.class)));
    }

    public static class VerificationModeInterceptor {

        public static final ThreadLocal<LinkedList<Object>> STACK = new ThreadLocal<>();

        @Advice.OnMethodEnter(inline = false)
        public static void interceptEnter(@Advice.This Object target) {
            var list = STACK.get();
            if (isNull(list)) {
                list = new LinkedList<>();
                STACK.set(list);
            }
            list.addLast(target);
        }

        public static Object getWanted(Object verificationData) {
            if (verificationData instanceof VerificationData) {
                return ((VerificationData) verificationData).getTarget();
            } else {
                return ((VerificationDataInOrder) verificationData).getWanted();
            }
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class, inline = false)
        public static void interceptExit(
            @Advice.Argument(0) Object verificationData,
            @Advice.Thrown Throwable thrown) {

            var list = STACK.get();
            list.removeLast();
            if (!list.isEmpty()) {
                return;
            }
            STACK.remove();

            setWanted(getWanted(verificationData));
            if (nonNull(thrown)) {
                setThrown(thrown);
            }
            runStep();
        }
    }
}