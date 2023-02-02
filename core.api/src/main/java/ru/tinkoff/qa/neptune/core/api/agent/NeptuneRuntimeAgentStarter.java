package ru.tinkoff.qa.neptune.core.api.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.ServiceLoader.load;
import static net.bytebuddy.agent.ByteBuddyAgent.install;
import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public final class NeptuneRuntimeAgentStarter {

    private static boolean isLaunched;

    private NeptuneRuntimeAgentStarter() {
        super();
    }

    public static synchronized void runAgent() {

        if (isIsLaunched()) {
            return;
        }

        var iterator = load(NeptuneTransformParameters.class).iterator();
        var parameters = new ArrayList<TransformationParameters>();
        iterator.forEachRemaining(p -> parameters.add(new TransformationParameters(p)));

        if (parameters.size() == 0) {
            isLaunched = true;
            return;
        }

        var inst = install();

        var agentBuilderSettings = new AgentBuilder.Default()
            .disableClassFormatChanges()
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
            .with(AgentBuilder.DescriptionStrategy.SuperTypeLoading.Default.POOL_FIRST)
            .ignore(nameStartsWith("net.bytebuddy."))
            .ignore(nameStartsWith("com.sun."))
            .ignore(nameStartsWith("org.testng."))
            .ignore(nameStartsWith("org.junit."))
            .ignore(nameStartsWith("com.gradle."))
            .ignore(nameContains("maven"))
            .ignore(nameContains("apache"))
            .ignore(nameStartsWith("com.google."))
            .ignore(nameStartsWith("com.fasterxml"));

        AgentBuilder.Identified.Extendable extendable = null;

        for (var p : parameters) {
            if (isNull(extendable)) {
                extendable = agentBuilderSettings
                    .type(p.getTypeDescription())
                    .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder.visit(
                        Advice.to(p.getInterceptor()).on(p.getMethodMatcher())
                    ));
            } else {
                extendable = extendable
                    .type(p.getTypeDescription())
                    .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder.visit(
                        Advice.to(p.getInterceptor()).on(p.getMethodMatcher())
                    ));
            }
        }

        if (nonNull(extendable)) {
            extendable.installOn(inst);
        }
        isLaunched = true;
    }

    public static boolean isIsLaunched() {
        return isLaunched;
    }

    private static final class TransformationParameters {

        private final ElementMatcher<? super TypeDescription> typeDescription;
        private final ElementMatcher<? super MethodDescription> methodMatcher;
        private final Class<?> interceptor;

        private TransformationParameters(NeptuneTransformParameters provider) {
            var typeDescription = provider.typeMatcher();
            var methodMatcher = provider.methodMatcher();
            var interceptor = provider.interceptor();

            checkState(nonNull(typeDescription),
                "Type matcher is not defined. " + provider.getClass().getName());

            checkState(nonNull(methodMatcher),
                "Method matcher is not defined. " + provider.getClass().getName());

            checkState(nonNull(interceptor),
                "Class-interceptor is not defined. " + provider.getClass().getName());

            this.typeDescription = typeDescription;
            this.methodMatcher = methodMatcher;
            this.interceptor = interceptor;
        }

        private ElementMatcher<? super MethodDescription> getMethodMatcher() {
            return methodMatcher;
        }

        private Class<?> getInterceptor() {
            return interceptor;
        }

        private ElementMatcher<? super TypeDescription> getTypeDescription() {
            return typeDescription;
        }
    }
}
