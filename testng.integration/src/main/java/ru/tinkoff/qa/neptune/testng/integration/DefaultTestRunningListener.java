package ru.tinkoff.qa.neptune.testng.integration;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.concurency.GroupingObjects;
import ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore;
import org.testng.*;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.testng.ITestResult.FAILURE;
import static org.testng.ITestResult.SKIP;
import static ru.tinkoff.qa.neptune.core.api.cleaning.Refreshable.refresh;
import static ru.tinkoff.qa.neptune.core.api.concurency.GroupingObjects.addGroupingObjectForCurrentThread;
import static ru.tinkoff.qa.neptune.testng.integration.properties.TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class DefaultTestRunningListener implements IInvokedMethodListener, ISuiteListener {

    private final Set<Object> knownTests = new HashSet<>();
    private final List<Class<? extends Annotation>> refreshBeforeMethodsAnnotatedBy =
            new ArrayList<>(REFRESH_STRATEGY_PROPERTY.get().stream().map(RefreshEachTimeBefore::get).collect(toList()));

    private final ThreadLocal<Method> previouslyRefreshed = new ThreadLocal<>();

    private static boolean isIgnored(Method method) {
        Class<?> declaredBy;
        Test test;
        return (((test = method.getAnnotation(Test.class)) != null)
                && (method.getAnnotation(Ignore.class) != null || !test.enabled()))
                || isClassIgnored(declaredBy = method.getDeclaringClass())
                || declaredBy.getPackage().getAnnotation(Ignore.class) != null;
    }

    private static boolean isClassIgnored(Class<?> classToBeIgnored) {
        Class<?> clazz = classToBeIgnored;
        while (!clazz.equals(Object.class)) {
            if (clazz.getAnnotation(Ignore.class) != null) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private void refreshIfNecessary(Object instance, Method method) {
        if (isIgnored(method)) {
            return;
        }

        ofNullable(previouslyRefreshed.get())
                .ifPresentOrElse(method1 -> {}, () -> {
                    int methodModifiers = method.getModifiers();
                    if (!isStatic(methodModifiers) && stream(method.getAnnotations())
                            .filter(annotation -> refreshBeforeMethodsAnnotatedBy
                                    .contains(((Annotation) annotation).annotationType())).collect(toList())
                            .size() > 0) {
                        refresh(instance);
                        previouslyRefreshed.set(method);
                    }
                });
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        ofNullable(testResult.getTestContext()).map(ITestContext::getSuite)
                .ifPresent(GroupingObjects::addGroupingObjectForCurrentThread);
        Method reflectionMethod = method.getTestMethod().getConstructorOrMethod().getMethod();
        ofNullable(testResult.getInstance()).ifPresent(o ->
                refreshIfNecessary(o, reflectionMethod));

        if (method.isTestMethod()) {
            previouslyRefreshed.remove();
        }

        ofNullable(reflectionMethod.getAnnotation(Test.class)).ifPresent(test -> {
            String name = isNotBlank(test.description()) ? test.description() : reflectionMethod.getName();
            Object[] params = testResult.getParameters();

            System.out.println();
            System.out.println();
            System.out.println(format("TEST '%s' HAS STARTED WITH PARAMETERS: %s", name, Arrays.toString(params)));
            System.out.println();
            System.out.println();
        });
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Method reflectionMethod = method.getTestMethod().getConstructorOrMethod().getMethod();
        if (!isIgnored(reflectionMethod)) {
            Object instance = testResult.getInstance();
            knownTests.add(instance);
        }

        ofNullable(reflectionMethod.getAnnotation(Test.class)).ifPresent(test -> {
            String name = isNotBlank(test.description()) ? test.description() : reflectionMethod.getName();
            int status = method.getTestResult().getStatus();
            Object[] params = testResult.getParameters();

            System.out.println();
            System.out.println();
            System.out.println(format("TEST '%s' HAS FINISHED WITH PARAMETERS: %s", name, Arrays.toString(params)));
            switch (status) {
                case FAILURE:
                    System.err.println("STATUS: FAILED. Exception:");
                    testResult.getThrowable().printStackTrace();
                    break;

                case SKIP:
                    System.out.println("STATUS: SKIPPED");
                    break;

                default:
                    System.out.println("STATUS: SUCCEED. CONGRATULATIONS!");
                    break;
            }
            System.out.println();
            System.out.println();
        });
    }

    @Override
    public void onStart(ISuite suite) {
        addGroupingObjectForCurrentThread(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        knownTests.forEach(Stoppable::shutDown);
    }
}
