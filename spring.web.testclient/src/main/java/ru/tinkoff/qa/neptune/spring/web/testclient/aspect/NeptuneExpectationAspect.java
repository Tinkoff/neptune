package ru.tinkoff.qa.neptune.spring.web.testclient.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.tinkoff.qa.neptune.spring.web.testclient.Expectation;

import java.util.Locale;

import static java.util.Locale.US;
import static org.apache.commons.lang3.StringUtils.*;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle.getFromResourceBundles;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Aspect
@SuppressWarnings({"unchecked", "unused"})
public class NeptuneExpectationAspect {

    private static final Locale DEFAULT_LOCALE = US;
    private static final ThreadLocal<String> ADDITIONAL_DESCRIPTION = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> IS_VALIDATION_STARTED = new ThreadLocal<>();

    public NeptuneExpectationAspect() {
        super();
    }

    @Pointcut("execution(* ru.tinkoff.qa.neptune.spring.web.testclient.Expectation.verify(..))")
    public void validatePointcut() {
    }

    @Pointcut("execution(* org.springframework.test.web.reactive.server.StatusAssertions.*(..))")
    public void statusAssertion() {
    }

    @Pointcut("execution(* org.springframework.test.web.reactive.server.HeaderAssertions.*(..))")
    public void headerAssertion() {
    }

    @Pointcut("execution(* org.springframework.test.web.reactive.server.CookieAssertions.*(..))")
    public void cookieAssertion() {
    }

    @Pointcut("execution(* org.springframework.test.web.reactive.server.JsonPathAssertions.*(..))")
    public void jsonPathAssertion() {
    }

    @Pointcut("execution(* org.springframework.test.web.reactive.server.XpathAssertions.*(..))")
    public void xPathAssertion() {
    }

    @Around("validatePointcut()")
    public <T> T aroundVerification(ProceedingJoinPoint joinPoint) throws Throwable {
        var target = (Expectation<?>) joinPoint.getTarget();
        IS_VALIDATION_STARTED.set(true);
        var result = (T) joinPoint.proceed();
        var additional = ADDITIONAL_DESCRIPTION.get();
        if (isNotBlank(additional)) {
            target.descriptionBuilder().append(". ").append(additional);
        }
        ADDITIONAL_DESCRIPTION.set(null);
        IS_VALIDATION_STARTED.set(null);
        return result;
    }

    @Around("statusAssertion() || headerAssertion() || cookieAssertion() || jsonPathAssertion() || xPathAssertion()")
    public <T> T createDescription(ProceedingJoinPoint joinPoint) throws Throwable {
        if (IS_VALIDATION_STARTED.get() == null) {
            return (T) joinPoint.proceed();
        }

        if (ADDITIONAL_DESCRIPTION.get() != null) {
            return (T) joinPoint.proceed();
        }

        var s = ((MethodSignature) joinPoint.getSignature());
        var m = s.getMethod();
        var args = joinPoint.getArgs();

        var additionalDescription = translate(getFromResourceBundles(DEFAULT_LOCALE, m), m, args);
        if (isBlank(additionalDescription)) {
            ADDITIONAL_DESCRIPTION.set(EMPTY);
        } else {
            ADDITIONAL_DESCRIPTION.set(additionalDescription);
        }

        return (T) joinPoint.proceed();
    }
}
