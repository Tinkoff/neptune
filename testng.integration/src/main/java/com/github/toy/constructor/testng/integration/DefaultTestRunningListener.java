package com.github.toy.constructor.testng.integration;

import com.github.toy.constructor.core.api.Stoppable;
import com.github.toy.constructor.testng.integration.properties.RefreshEachTimeBefore;
import org.testng.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.toy.constructor.core.api.Refreshable.refresh;
import static com.github.toy.constructor.testng.integration.properties.TestNGRefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class DefaultTestRunningListener implements IInvokedMethodListener, ISuiteListener {

    private final Set<Object> knownTests = new HashSet<>();
    private final List<Class<? extends Annotation>> refreshBeforeMethodsAnnotatedBy =
            new ArrayList<>(REFRESH_STRATEGY_PROPERTY.get().stream().map(RefreshEachTimeBefore::get).collect(toList()));

    private void refreshIfNecessary(Object instance, Method method) {
        if (stream(method.getAnnotations())
                .filter(annotation -> refreshBeforeMethodsAnnotatedBy
                        .contains(annotation.getClass())).collect(toList())
                .size() > 0) {
            refresh(instance);
        }

    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        Object instance = testResult.getInstance();
        knownTests.add(instance);
        refreshIfNecessary(instance, method.getTestMethod().getConstructorOrMethod()
                .getMethod());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        //does nothing
    }

    @Override
    public void onStart(ISuite suite) {
        //does nothing
    }

    @Override
    public void onFinish(ISuite suite) {
        knownTests.forEach(Stoppable::shutDown);
    }
}
