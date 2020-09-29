package ru.tinkoff.qa.neptune.testng.integration;

import com.google.common.collect.Iterables;
import io.github.classgraph.ClassGraph;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.testng.ITestResult.*;
import static ru.tinkoff.qa.neptune.testng.integration.properties.TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;

public final class DefaultTestRunningListener implements IInvokedMethodListener {

    private final ThreadLocal<Method> previouslyRefreshed = new ThreadLocal<>();
    private final List<Class<? extends Context>> REFRESHABLE_CONTEXTS = new ClassGraph()
            .enableAllInfo()
            .scan()
            .getSubclasses(Context.class.getName())
            .loadClasses(Context.class)
            .stream()
            .filter(ContextRefreshable.class::isAssignableFrom)
            .collect(toList());

    private static boolean isIgnored(Method method) {
        Class<?> declaredBy;
        Test test;
        return (((test = method.getAnnotation(Test.class)) != null)
                && (method.getAnnotation(Ignore.class) != null || !test.enabled()))
                || isClassIgnored(declaredBy = method.getDeclaringClass())
                || declaredBy.getPackage().getAnnotation(Ignore.class) != null;
    }

    private static boolean isClassIgnored(Class<?> classToBeIgnored) {
        var clazz = classToBeIgnored;
        while (!clazz.equals(Object.class)) {
            if (clazz.getAnnotation(Ignore.class) != null) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private static List<Class<? extends Annotation>> getRefreshStrategy() {
        return REFRESH_STRATEGY_PROPERTY.get().stream().map(RefreshEachTimeBefore::get).collect(toList());
    }

    private void refreshIfNecessary(Method method) {
        if (isIgnored(method)) {
            return;
        }

        var annotationToRefreshBefore = getRefreshStrategy();

        ofNullable(previouslyRefreshed.get())
                .ifPresentOrElse(method1 -> {}, () -> {
                    if (stream(method.getAnnotations()).anyMatch(annotation -> annotationToRefreshBefore
                            .contains(((Annotation) annotation).annotationType()))) {
                        REFRESHABLE_CONTEXTS.forEach(ContextRefreshable::refreshContext);
                        previouslyRefreshed.set(method);
                    }
                });
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        var reflectionMethod = method.getTestMethod().getConstructorOrMethod().getMethod();
        ofNullable(testResult.getInstance()).ifPresent(o ->
                refreshIfNecessary(reflectionMethod));

        if (method.isTestMethod()) {
            previouslyRefreshed.remove();
        }

        ofNullable(reflectionMethod.getAnnotation(Test.class)).ifPresent(test -> {
            var name = isNotBlank(test.description()) ? test.description() : reflectionMethod.getName();
            var params = testResult.getParameters();

            System.out.println();
            System.out.println();
            System.out.println(format("TEST '%s' HAS STARTED WITH PARAMETERS: %s", name, Arrays.toString(params)));
            System.out.println();
            System.out.println();
        });
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        var reflectionMethod = method.getTestMethod().getConstructorOrMethod().getMethod();

        ofNullable(reflectionMethod.getAnnotation(Test.class)).ifPresent(test -> {
            var name = isNotBlank(test.description()) ? test.description() : reflectionMethod.getName();
            var status = method.getTestResult().getStatus();
            var params = testResult.getParameters();
            var stringParams = stream(params).map(o -> {
                if (o == null) {
                    return valueOf(o);
                }

                var clazz = o.getClass();
                if (clazz.isArray()) {
                    return Arrays.toString((Object[]) o);
                }

                if (Iterable.class.isAssignableFrom(clazz)) {
                    return Iterables.toString((Iterable<?>) o);
                }

                return valueOf(o);
            }).collect(joining(","));

            System.out.println();
            System.out.println();
            System.out.println(format("TEST '%s' HAS FINISHED WITH PARAMETERS: %s", name, stringParams));
            switch (status) {
                case FAILURE:
                    System.err.println("STATUS: FAILED. Exception:");
                    testResult.getThrowable().printStackTrace();
                    break;

                case SKIP:
                    System.out.println("STATUS: SKIPPED");
                    break;

                case CREATED:
                    System.out.println("STATUS: NOT STARTED");
                    break;

                case STARTED:
                    System.out.println("STATUS: STILL NOT FINISHED");
                    break;

                default:
                    System.out.println("STATUS: SUCCEED. CONGRATULATIONS!");
                    break;
            }
            System.out.println();
            System.out.println();
        });
    }
}
