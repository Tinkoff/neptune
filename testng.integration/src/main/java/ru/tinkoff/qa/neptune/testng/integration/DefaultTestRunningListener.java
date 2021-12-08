package ru.tinkoff.qa.neptune.testng.integration;

import com.google.common.collect.Iterables;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;
import ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.valueOf;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.testng.ITestResult.*;
import static ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable.REFRESHABLE_CONTEXTS;
import static ru.tinkoff.qa.neptune.core.api.concurrency.BusyThreads.setBusy;
import static ru.tinkoff.qa.neptune.core.api.concurrency.BusyThreads.setFree;
import static ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook.getHooks;
import static ru.tinkoff.qa.neptune.testng.integration.properties.TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;

public final class DefaultTestRunningListener implements IInvokedMethodListener, ITestListener {

    private final ThreadLocal<Method> previouslyRefreshed = new ThreadLocal<>();
    private final List<ExecutionHook> hooks = getHooks();

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
                            .contains(annotation.annotationType()))) {
                        REFRESHABLE_CONTEXTS.forEach(ContextRefreshable::refreshContext);
                        previouslyRefreshed.set(method);
                    }
                });
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        setBusy(currentThread());
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
            System.out.printf("TEST '%s' HAS STARTED WITH PARAMETERS: %s%n", name, Arrays.toString(params));
            System.out.println();
            System.out.println();
        });

        if (method.getTestResult().getStatus() != SKIP) {
            hooks.forEach(executionHook -> executionHook
                    .executeMethodHook(reflectionMethod, testResult.getInstance(), method.isTestMethod()));
        }
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
                    return valueOf((Object) null);
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
            System.out.printf("TEST '%s' HAS FINISHED WITH PARAMETERS: %s%n", name, stringParams);
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
        setFree(currentThread());
    }
}
