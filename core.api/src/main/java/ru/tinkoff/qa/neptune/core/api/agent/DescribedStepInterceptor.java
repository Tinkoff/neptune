package ru.tinkoff.qa.neptune.core.api.agent;

import net.bytebuddy.asm.Advice;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.util.Objects.isNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

public class DescribedStepInterceptor {

    @Advice.OnMethodExit(inline = false)
    public static void interceptExit(
        @Advice.Origin Method method,
        @Advice.AllArguments Object[] args,
        @Advice.Return Object returned) throws Exception {

        if (isNull(returned)) {
            return;
        }

        Class<?> cls;
        Field field;

        if (returned instanceof SequentialGetStepSupplier) {
            cls = SequentialGetStepSupplier.class;
            field = cls.getDeclaredField("description");
        } else if (returned instanceof SequentialActionSupplier) {
            cls = SequentialActionSupplier.class;
            field = cls.getDeclaredField("actionDescription");
        } else {
            cls = Criteria.class;
            field = cls.getDeclaredField("description");
        }

        field.setAccessible(true);
        var value = field.get(returned);

        if (isNull(value)) {
            value = translate(returned,
                method,
                args);

            field.set(returned, value);
        }
    }
}
