package ru.tinkoff.qa.neptune.mockito;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.mockito.internal.MockedStaticImpl;
import org.mockito.internal.MockitoCore;
import org.mockito.plugins.MockMaker;
import ru.tinkoff.qa.neptune.core.api.agent.NeptuneTransformParameters;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class VerifyNoInteractionsTransformationParameters implements NeptuneTransformParameters {
    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return named("verifyNoInteractions")
            .or(named("verifyNoMoreInteractions"))
            .or(named("verifyNoMoreInteractionsInOrder"));
    }

    @Override
    public Class<?> interceptor() {
        return NoInteractionsStaticInterceptor.class;
    }

    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return is(MockitoCore.class).or(is(MockedStaticImpl.class));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static class NoInteractionsStaticInterceptor {

        public static String prepareMocks(Object target,
                                          Object[] args) throws Exception {
            if (target instanceof MockedStaticImpl) {
                var field = MockedStaticImpl.class.getDeclaredField("control");
                field.setAccessible(true);
                return ((MockMaker.StaticMockControl) field.get(target)).getType().getName();
            } else {
                var arg = args[0];
                if (isNull(arg)) {
                    return null;
                }

                Stream<Object> mockStream = null;
                if (arg.getClass().isArray()) {
                    if (((Object[]) arg).length == 0) {
                        return null;
                    }
                    mockStream = Arrays.stream((Object[]) arg);
                }

                if (arg instanceof Collection) {
                    if (((Collection<Object>) arg).isEmpty()) {
                        return null;
                    }

                    mockStream = ((Collection<Object>) arg).stream();
                }

                if (isNull(mockStream)) {
                    return null;
                }

                return mockStream.map(o -> {
                    if (o instanceof Class) {
                        return ((Class) o).getName();
                    }
                    return valueOf(o);
                }).collect(joining(", "));
            }
        }

        public static String getStepName(Method method) {
            if (method.getName().equals("verifyNoInteractions")) {
                return new VerifyNoInteractions().toString();
            } else if (method.getName().equals("verifyNoMoreInteractions")) {
                return new VerifyNoMoreInteractions().toString();
            } else {
                return new VerifyNoMoreInteractionsInOrder().toString();
            }
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class, inline = false)
        public static void interceptFailedExit(
            @Advice.This Object target,
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] args,
            @Advice.Thrown Throwable thrown) throws Exception {

            String mocks = prepareMocks(target, args);

            if (isNull(mocks)) {
                return;
            }

            Object stepName = getStepName(method);

            try {
                $(stepName + " " + mocks, () -> {
                    if (nonNull(thrown)) {
                        if (thrown instanceof RuntimeException) {
                            throw (RuntimeException) thrown;
                        }

                        if (thrown instanceof Error) {
                            throw (Error) thrown;
                        }

                        throw new IllegalStateException(thrown);
                    }
                });
            } catch (Throwable throwable) {
                //do nothing here
            }
        }
    }
}
