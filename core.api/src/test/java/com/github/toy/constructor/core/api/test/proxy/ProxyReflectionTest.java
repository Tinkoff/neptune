package com.github.toy.constructor.core.api.test.proxy;

import com.github.toy.constructor.core.api.StepMark;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static java.util.Arrays.asList;
import static java.util.Arrays.deepEquals;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class ProxyReflectionTest {

    private CalculatorSteps calculator;
    private final List<Method> methodsDeclaredByClasses = getMethodsFromClasses(CalculatorSteps.class);
    private final List<Method> methodsDeclaredByInterfaces = getMethodsFromInterfaces(CalculatorSteps.class);

    private static List<Method> getMethodsFromClasses(Class<?> origin, Class<?>...excludedSuperClasses) {
        Class<?> clazz = origin;
        List<Method> result = new ArrayList<>();
        List<Class<?>> excluded = asList(excludedSuperClasses);
        while (clazz != null) {
            if (!excluded.contains(clazz)) {
                result.addAll(asList(clazz.getDeclaredMethods()));
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    private static List<Method> getMethodsFromInterfaces(Class<?> origin) {
        List<Method> result = new ArrayList<>();
        asList(origin.getInterfaces()).forEach(interfaceParam ->
                result.addAll(asList(interfaceParam.getDeclaredMethods())));
        return result;
    }

    @BeforeClass
    public void beforeAll() throws Exception {
        calculator = getSubstituted(CalculatorSteps.class, params(), new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }

            @Override
            public String value() {
                return "Test value";
            }
        }).perform(action("Something", calculatorSteps -> {}));
    }

    @Test
    public void reflectionTestOfNotTheFinalClass() {
        assertThat("Check that class of resulted object is not final",
                !Modifier.isFinal(calculator.getClass().getModifiers()),
                is(true));
    }

    @Test
    public void reflectionTestOfMethodsDeclaredByClass() {
        List<Method> declaredByProxyClass = getMethodsFromClasses(calculator.getClass(),
                CalculatorSteps.class, Object.class);
        List<Method> toBeReported = methodsDeclaredByClasses
                .stream()
                .filter(method -> method.getAnnotation(StepMark.class) != null).collect(toList());

        List<Method> found = new ArrayList<>();

        List<Method> toBeReportedByProxy = declaredByProxyClass.stream().filter(method ->
                method.getAnnotation(TestAnnotation.class) != null).collect(toList());

        toBeReported.forEach(method -> {
            for (Method m: toBeReportedByProxy) {
                if (method.getName().equals(m.getName()) &&
                        deepEquals(method.getParameterTypes(), m.getParameterTypes())) {
                    found.add(method);
                }
            }
        });
        assertThat("Methods declared by class which are supposed to be reported and annotated by TestAnnotation",
                found,
                contains(toBeReported.toArray()));
    }

    @Test
    public void reflectionTestOfMethodsDeclaredByInterfaces() {
        List<Method> declaredByProxyClass = getMethodsFromClasses(calculator.getClass(),
                CalculatorSteps.class, Object.class);
        List<Method> toBeReported = methodsDeclaredByInterfaces
                .stream()
                .filter(method -> method.getAnnotation(StepMark.class) != null).collect(toList());

        List<Method> toBeReportedByProxy = declaredByProxyClass.stream().filter(method ->
                method.getAnnotation(TestAnnotation.class) != null).collect(toList());

        List<Method> found = new ArrayList<>();

        toBeReported.forEach(method -> {
            for (Method m: toBeReportedByProxy) {
                if (method.getName().equals(m.getName()) &&
                        deepEquals(method.getParameterTypes(), m.getParameterTypes())) {
                    found.add(method);
                    return;
                }
            }
        });
        assertThat("Methods declared by implemented interfaces which are supposed to be reported and annotated by TestAnnotation",
                found,
                containsInAnyOrder(toBeReported.toArray()));
    }
}
