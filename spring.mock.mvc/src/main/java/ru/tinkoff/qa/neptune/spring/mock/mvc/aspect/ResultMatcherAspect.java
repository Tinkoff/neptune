package ru.tinkoff.qa.neptune.spring.mock.mvc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Locale;

import static java.util.Locale.US;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle.getFromResourceBundles;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.localization.SpringMockMvcResultMatcherBundleExtension.getFactoryClasses;

@Aspect
@SuppressWarnings("unused")
public class ResultMatcherAspect {

    private static final Locale DEFAULT_LOCALE = US;
    private static final ThreadLocal<String> FIRST_PART = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> SECOND_PART_IS_BEING_CALCULATED = new ThreadLocal<>();

    private static Object[] substituteArguments(Object[] args) {
        var newArgs = new Object[]{};
        for (var a : args) {
            if (a instanceof MvcUriComponentsBuilder.MethodInvocationInfo) {
                newArgs = add(newArgs, new NeptuneMethodInvocationInfo((MvcUriComponentsBuilder.MethodInvocationInfo) a));
            } else {
                newArgs = add(newArgs, a);
            }
        }
        return newArgs;
    }

    @Pointcut("execution(* org.springframework.test.web.servlet.result..*.*(..))")
    public void resultPointCut() {

    }

    @Around("resultPointCut()")
    public Object setDescriptionToAssertResult(ProceedingJoinPoint joinPoint) throws Throwable {
        var s = ((MethodSignature) joinPoint.getSignature());
        var m = s.getMethod();
        var args = substituteArguments(joinPoint.getArgs());

        if (getFactoryClasses().contains(m.getReturnType())) {
            var firstPart = translate(getFromResourceBundles(DEFAULT_LOCALE, m), m, args);
            if (isBlank(firstPart)) {
                FIRST_PART.set(EMPTY);
            } else {
                FIRST_PART.set(firstPart + ".");
            }

            return joinPoint.proceed();
        }

        if (ResultMatcher.class.isAssignableFrom(m.getReturnType()) && isNull(SECOND_PART_IS_BEING_CALCULATED.get())) {
            SECOND_PART_IS_BEING_CALCULATED.set(true);
            var secondPart = translate(getFromResourceBundles(DEFAULT_LOCALE, m), m, args);
            var mather = (ResultMatcher) joinPoint.proceed();

            secondPart = isBlank(secondPart) ? mather.toString() : secondPart;
            var toStringExpression = ((isBlank(FIRST_PART.get()) ? EMPTY : FIRST_PART.get() + " ") + secondPart).trim();

            SECOND_PART_IS_BEING_CALCULATED.set(null);
            FIRST_PART.set(null);

            return new ResultMatcher() {
                @Override
                public void match(MvcResult result) throws Exception {
                    mather.match(result);
                }

                @Override
                public String toString() {
                    return toStringExpression;
                }
            };
        }

        return joinPoint.proceed();
    }
}
