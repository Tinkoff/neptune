package ru.tinkoff.qa.neptune.core.api.steps.localization.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.lang.reflect.Method;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

@Aspect
public class LocalizationAspect {
    public LocalizationAspect() {
        super();
    }

    /**
     * Pointcut for public static methods returns Criteria>
     */
    @Pointcut("execution(static ru.tinkoff.qa.neptune.core.api.steps.Criteria *(java.util.function.Predicate))")
    public void criteriaMethod() {
    }

    /**
     * Pointcut for public static methods
     */
    @Pointcut("execution(public static * *(..))")
    public void allPublicStaticMethod() {
    }

    /**
     * Pointcut for subclasses {@link SequentialActionSupplier}
     */
    @Pointcut("execution(* ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier+.*(..))")
    public void allSequentialActionSupplierSubclass() {
    }

    /**
     * Pointcut for subclasses {@link SequentialGetStepSupplier}
     */
    @Pointcut("execution(* ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier+.*(..))")
    public void allSequentialGetStepSupplierSubclass() {
    }

    @Around("allPublicStaticMethod() && allSequentialActionSupplierSubclass()")
    public Object translateActionSupplier(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        var description = translate(((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());

        Method method = SequentialActionSupplier.class.getDeclaredMethod("setDescription", String.class);
        method.setAccessible(true);
        method.invoke(result, description);

        return result;
    }

    @Around("allPublicStaticMethod() && allSequentialGetStepSupplierSubclass()")
    public Object translateGetSupplier(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        var description = translate(((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());

        Method method = SequentialGetStepSupplier.class.getDeclaredMethod("setDescription", String.class);
        method.setAccessible(true);
        method.invoke(result, description);

        return result;
    }

    @Around("criteriaMethod()")
    public Object translateCriteria(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        var description = translate(((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());

        Method method = Criteria.class.getDeclaredMethod("setDescription", String.class);
        method.setAccessible(true);
        method.invoke(result, description);

        return result;
    }
}
