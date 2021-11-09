package ru.tinkoff.qa.neptune.core.api.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.lang.reflect.Method;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Aspect
@SuppressWarnings("unused")
public class LocalizationAspect {

    private static final String POINTCUT_CRITERIA_METHOD =
            "execution(static ru.tinkoff.qa.neptune.core.api.steps.Criteria *(..))";

    private static final String POINTCUT_CONDITION =
            "execution(static ru.tinkoff.qa.neptune.core.api.steps.Criteria condition(..))";

    private static final String POINTCUT_AND =
            "execution(static ru.tinkoff.qa.neptune.core.api.steps.Criteria AND(..))";

    private static final String POINTCUT_OR =
            "execution(static ru.tinkoff.qa.neptune.core.api.steps.Criteria OR(..))";

    private static final String POINTCUT_XOR =
            "execution(static ru.tinkoff.qa.neptune.core.api.steps.Criteria ONLY_ONE(..))";

    private static final String POINTCUT_NOT =
            "execution(static ru.tinkoff.qa.neptune.core.api.steps.Criteria NOT(..))";

    private static final String POINTCUT_PUBLIC_STATIC_METHOD_ACTION_SUPPLIER =
            "execution(static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier+ *(..))";

    private static final String POINTCUT_PUBLIC_STATIC_METHOD_GET_SUPPLIER =
            "execution(static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier+ *(..))";

    private static final String POINTCUT_ACTION_SUPPLIER =
            "execution(* ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier+.*(..))";

    private static final String POINTCUT_GET_SUPPLIER =
            "execution(* ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier+.*(..))";

    @Pointcut(POINTCUT_CRITERIA_METHOD)
    public void criteriaMethod() {
    }

    @Pointcut(POINTCUT_CONDITION)
    public void conditions() {
    }

    @Pointcut(POINTCUT_AND)
    public void AND() {
    }

    @Pointcut(POINTCUT_OR)
    public void OR() {
    }

    @Pointcut(POINTCUT_XOR)
    public void XOR() {
    }

    @Pointcut(POINTCUT_NOT)
    public void NOT() {
    }

    @Pointcut(POINTCUT_PUBLIC_STATIC_METHOD_ACTION_SUPPLIER)
    public void returnActionSupplier() {
    }

    @Pointcut(POINTCUT_PUBLIC_STATIC_METHOD_GET_SUPPLIER)
    public void returnGetSupplier() {
    }

    @Pointcut(POINTCUT_ACTION_SUPPLIER)
    public void allSequentialActionSupplierSubclass() {
    }

    @Pointcut(POINTCUT_GET_SUPPLIER)
    public void allSequentialGetStepSupplierSubclass() {
    }

    @Around("returnActionSupplier() && allSequentialActionSupplierSubclass()")
    public Object translateActionSupplier(ProceedingJoinPoint joinPoint) throws Throwable {
        var result = joinPoint.proceed();

        if (result != null) {
            var description = translate(result,
                    ((MethodSignature) joinPoint.getSignature()).getMethod(),
                    joinPoint.getArgs());

            if (description != null) {
                Method method = SequentialActionSupplier.class.getDeclaredMethod("setDescription", String.class);
                method.setAccessible(true);
                method.invoke(result, description);
            }
            return result;
        }
        return null;
    }

    @Around("returnGetSupplier() && allSequentialGetStepSupplierSubclass()")
    public Object translateGetSupplier(ProceedingJoinPoint joinPoint) throws Throwable {
        var result = joinPoint.proceed();

        if (result != null) {
            var description = translate(result,
                    ((MethodSignature) joinPoint.getSignature()).getMethod(),
                    joinPoint.getArgs());

            if (description != null) {
                Method method = SequentialGetStepSupplier.class.getDeclaredMethod("setDescription", String.class);
                method.setAccessible(true);
                method.invoke(result, description);
            }

            return result;
        }
        return null;
    }

    @Around("criteriaMethod() && !conditions() && !AND() && !OR() && !XOR() && !NOT()")
    public Object translateCriteria(ProceedingJoinPoint joinPoint) throws Throwable {
        var result = joinPoint.proceed();

        if (result != null) {

            var description = translate(result,
                    ((MethodSignature) joinPoint.getSignature()).getMethod(),
                    joinPoint.getArgs());

            if (description != null) {
                Method method = Criteria.class.getDeclaredMethod("setDescription", String.class);
                method.setAccessible(true);
                method.invoke(result, description);
            }

            return result;
        }
        return null;
    }
}