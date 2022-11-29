package ru.tinkoff.qa.neptune.core.api.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.CombinableMatcher;
import ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle.getFromResourceBundles;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

@Aspect
@SuppressWarnings({"unused", "unchecked"})
public class HamcrestAspect {

    private static final ThreadLocal<Boolean> IS_CALCULATION_STARTED = new ThreadLocal<>();

    private static boolean isLocalizationUsed() {
        return nonNull(DEFAULT_LOCALIZATION_ENGINE.get()) && nonNull(DEFAULT_LOCALE_PROPERTY.get());
    }

    private static boolean isDefaultLocalization() {
        return LocalizationByResourceBundle.class.equals(DEFAULT_LOCALIZATION_ENGINE.get().getClass());
    }

    private static String calculateDescription(Matcher<?> matcher, JoinPoint joinPoint) {
        if (!isDefaultLocalization()) {
            return translate(matcher.toString());
        } else {
            var m = ((MethodSignature) joinPoint.getSignature()).getMethod();
            return translate(getFromResourceBundles(DEFAULT_LOCALE_PROPERTY.get(), m),
                    m,
                    joinPoint.getArgs());
        }
    }

    @Pointcut("execution(* org.hamcrest..*.*(..))")
    public void hamcrest() {
    }

    @Pointcut("execution(* org.hamcrest..*.describedAs(..))")
    public void describedAs() {

    }

    @Pointcut("execution(* org.hamcrest..*.or(..))")
    public void or() {

    }

    @Pointcut("execution(* org.hamcrest..*.and(..))")
    public void and() {

    }

    @Around("hamcrest() && !or() && !and() && !describedAs()")
    public Object aroundMatcherCreating(ProceedingJoinPoint joinPoint) throws Throwable {
        var m = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (m.getDeclaringClass().equals(Matchers.class)) {
            return joinPoint.proceed();
        }

        if (!Matcher.class.isAssignableFrom(m.getReturnType())) {
            return joinPoint.proceed();
        }

        if (nonNull(IS_CALCULATION_STARTED.get())) {
            return joinPoint.proceed();
        }

        IS_CALCULATION_STARTED.set(true);
        try {
            var matcher = (Matcher<?>) joinPoint.proceed();

            if (!isLocalizationUsed()) {
                return matcher;
            }

            String newDescription = calculateDescription(matcher, joinPoint);
            return Matchers.describedAs(newDescription, matcher);
        } finally {
            IS_CALCULATION_STARTED.remove();
        }
    }

    @Around("or() || and()")
    public Object bothOrAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var matcher = (Matcher<?>) joinPoint.proceed();

        if (!isLocalizationUsed()) {
            return matcher;
        }

        String newDescription = calculateDescription(matcher, joinPoint);
        var target = joinPoint.getTarget();

        if (target instanceof Matcher) {
            return new CombinableMatcher<>((Matcher<Object>) matcher) {
                @Override
                public String toString() {
                    return "(" + target + ") " + newDescription;
                }
            };
        } else {
            var f = target.getClass().getDeclaredField("first");
            f.setAccessible(true);
            var first = f.get(target);
            return new CombinableMatcher<>((Matcher<Object>) matcher) {
                @Override
                public String toString() {
                    return first + " " + newDescription;
                }
            };
        }
    }
}
