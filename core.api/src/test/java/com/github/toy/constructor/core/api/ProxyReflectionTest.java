package com.github.toy.constructor.core.api;

import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static com.github.toy.constructor.core.api.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.Substitution.getSubstituted;
import static java.util.Arrays.asList;
import static java.util.Arrays.deepEquals;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

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

    private void createWithDefinedAnnotationFactory() throws Exception {
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }

            @Override
            public String value() {
                return "Test value";
            }
        };

        calculator = getSubstituted(CalculatorSteps.class, params(), List.of(new StepAnnotationFactory() {
            @Override
            public Annotation forPerform() {
                return testAnnotation;
            }

            @Override
            public Annotation forGet() {
                return testAnnotation;
            }

            @Override
            public Annotation forReturn() {
                return testAnnotation;
            }
        })).perform(action("Something", calculatorSteps -> {}));;
    }

    @Test
    public void reflectionTestOfNotTheFinalClass() throws Exception {
        createWithDefinedAnnotationFactory();
        assertThat("Check that class of resulted object is not final",
                !Modifier.isFinal(calculator.getClass().getModifiers()),
                is(true));
    }

    private boolean checkAnnotatedMethods(Class<? extends Annotation> annotationClass) {
        List<Method> declaredByProxyClass = getMethodsFromClasses(calculator.getClass(),
                CalculatorSteps.class, Object.class);
        List<Method> toBeReported = methodsDeclaredByInterfaces
                .stream()
                .filter(method -> method.getAnnotation(StepMarkPerform.class) != null
                        || method.getAnnotation(StepMarkGet.class) != null
                        || method.getAnnotation(StepMarkReturn.class) != null).collect(toList());

        List<Method> toBeReportedByProxy = declaredByProxyClass.stream().filter(method ->
                method.getAnnotation(annotationClass) != null).collect(toList());

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
        assertThat("Methods declared by implemented interfaces which are supposed to be reported and annotated by "
                        + annotationClass.getSimpleName(),
                found,
                containsInAnyOrder(toBeReported.toArray()));
        return true;
    }

    @Test
    public void reflectionTestOfAnnotatedMethodsDeclaredByInterfaces() throws Exception {
        createWithDefinedAnnotationFactory();
        assertTrue(checkAnnotatedMethods(TestAnnotation.class));
    }

    @Test
    public void reflectionTestOfAnnotatedMethodsDeclaredByInterfaces2() throws Exception {
        calculator = getSubstituted(CalculatorSteps.class, params())
                .perform(action("Something", calculatorSteps -> {}));;
        assertTrue(checkAnnotatedMethods(TestAnnotation2.class));
    }
}
