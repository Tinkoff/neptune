package ru.tinkoff.qa.neptune.core.api.steps;

import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefaultGetParameterReader.getResultMetadata;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

/**
 * This class allows to perform steps and to create it as a java object.
 *
 * @param <T> is a type of resulted value of step execution
 */
public abstract class Step<T> {

    private final String description;

    Step(String description) {
        checkArgument(isNotBlank(description), "Description of the step should not be blank or null string");
        this.description = description;
    }

    /**
     * Creates a step and performs it immediately. The result is void.
     *
     * @param description Is description of the step.
     * @param runnable    is algorithm of the step.
     */
    public static void $(String description, Runnable runnable) {
        createStep(description, runnable).perform();
    }

    /**
     * Creates a step and performs it immediately. The result is some value.
     *
     * @param description Is description of the step.
     * @param supplier    is algorithm of the step.
     * @param <T>         is a type of resulted value
     * @return result of the step performing
     */
    public static <T> T $(String description, Supplier<T> supplier) {
        return createStep(description, supplier).perform();
    }

    private static Step<Void> createStep(String description, Runnable runnable) {
        return new ActionStep(description, runnable);
    }

    private static <T> Step<T> createStep(String description, Supplier<T> supplier) {
        return new GetStep<>(description, supplier);
    }

    public String toString() {
        return description;
    }

    /**
     * Performs the step
     *
     * @return result of performed step
     */
    public abstract T perform();

    private static class ActionStep extends Step<Void> {
        private final Runnable stepRunnable;

        private ActionStep(String description, Runnable stepRunnable) {
            super(description);
            checkNotNull(stepRunnable, "Step algorithm should not be a null value");
            this.stepRunnable = stepRunnable;
        }

        @Override
        public Void perform() {
            try {
                fireEventStarting(translate(toString()), emptyMap());
                stepRunnable.run();
                return null;
            } catch (Throwable thrown) {
                fireThrownException(thrown);
                throw thrown;
            } finally {
                fireEventFinishing();
            }
        }
    }

    private static class GetStep<T> extends Step<T> {

        private final Supplier<T> stepSupplier;

        private GetStep(String description, Supplier<T> stepSupplier) {
            super(description);
            checkNotNull(stepSupplier, "Step algorithm should not be a null value");
            this.stepSupplier = stepSupplier;
        }

        @Override
        public T perform() {
            try {
                fireEventStarting(translate(toString()), emptyMap());

                T result = stepSupplier.get();
                if (isLoggable(result)) {
                    fireReturnedValue(translate(getResultMetadata(SequentialGetStepSupplier.class, false)), result);
                }

                return result;
            } catch (Throwable thrown) {
                fireThrownException(thrown);
                throw thrown;
            } finally {
                fireEventFinishing();
            }
        }
    }
}
