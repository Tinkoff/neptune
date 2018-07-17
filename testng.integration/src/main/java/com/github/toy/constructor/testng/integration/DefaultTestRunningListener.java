package com.github.toy.constructor.testng.integration;

import com.github.toy.constructor.core.api.cleaning.RefreshAndStopUtil;
import com.github.toy.constructor.testng.integration.properties.RefreshEachTimeBefore;
import org.testng.*;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.toy.constructor.core.api.cleaning.RefreshAndStopUtil.refresh;
import static com.github.toy.constructor.core.api.concurency.GroupingObjects.addGroupingObjectForCurrentThread;
import static com.github.toy.constructor.testng.integration.properties.TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class DefaultTestRunningListener implements IInvokedMethodListener, ISuiteListener {

    private final Set<Object> knownTests = new HashSet<>();
    private final List<Class<? extends Annotation>> refreshBeforeMethodsAnnotatedBy =
            new ArrayList<>(REFRESH_STRATEGY_PROPERTY.get().stream().map(RefreshEachTimeBefore::get).collect(toList()));

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

        int methodModifiers = method.getModifiers();
        if (!isStatic(methodModifiers) && stream(method.getAnnotations())
                .filter(annotation -> refreshBeforeMethodsAnnotatedBy
                        .contains(((Annotation) annotation).annotationType())).collect(toList())
                .size() > 0) {
            refresh(instance);
        }

    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        addGroupingObjectForCurrentThread(testResult.getTestContext().getSuite());
        Object instance = testResult.getInstance();
        refreshIfNecessary(instance, method.getTestMethod().getConstructorOrMethod()
                .getMethod());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!isIgnored(method.getTestMethod().getConstructorOrMethod()
                .getMethod())) {
            Object instance = testResult.getInstance();
            knownTests.add(instance);
        }
    }

    @Override
    public void onStart(ISuite suite) {
        addGroupingObjectForCurrentThread(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        knownTests.forEach(RefreshAndStopUtil::shutDown);
    }
}
