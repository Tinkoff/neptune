package ru.tinkoff.qa.neptune.core.api.steps;

import com.google.common.collect.Iterables;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;

@SequentialGetStepSupplier.DefineResultDescriptionParameterName("Is present")
@IncludeParamsOfInnerGetterStep
@MaxDepthOfReporting(0)
public final class Presence<T> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, Boolean, Object, Presence<T>> {

    private final Set<Class<? extends Throwable>> ignored2 = new HashSet<>();
    private final Set<Captor<Object, Object>> successCaptors = new HashSet<>();

    private Presence() {
        super(Presence::isValuable);
    }

    private Presence(SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
        this();
        from(turnReportingOff(toBePresent.clone()));
        this.successCaptors.addAll(toBePresent.successCaptors);
    }

    private static boolean isValuable(Object o) {
        return ofNullable(o)
                .map(o1 -> {
                    Class<?> clazz = o1.getClass();

                    if (Boolean.class.isAssignableFrom(clazz)) {
                        return (Boolean) o1;
                    }

                    if (Iterable.class.isAssignableFrom(clazz)) {
                        return Iterables.size((Iterable<?>) o1) > 0;
                    }

                    if (clazz.isArray()) {
                        return Array.getLength(o1) > 0;
                    }
                    return true;
                })
                .orElse(false);
    }

    /**
     * Creates an instance of {@link Presence}.
     *
     * @param toBePresent as a supplier of a function. If the result of {@link Function#apply(Object)} is not {@code null},
     *                    it is not an empty iterable/array or it is {@link Boolean} {@code true} then this is considered present.
     * @param <T>         is a type of {@link Context}
     * @return an instance of {@link Presence}.
     */
    @Description("Presence of {toBePresent}")
    public static <T> Presence<T> presence(@DescriptionFragment("toBePresent") SequentialGetStepSupplier<T, ?, ?, ?, ?> toBePresent) {
        checkArgument(nonNull(toBePresent), "Supplier of a function should not be a null-value");
        return new Presence<>(toBePresent);
    }

    @Override
    String getExceptionMessage(String messageStarting) {
        var stringBuilder = new StringBuilder(messageStarting)
                .append(SPACE)
                .append(((SequentialGetStepSupplier<?, ?, ?, ?, ?>) from).getDescription());
        getParameters().forEach((key, value) -> stringBuilder.append("\r\n").append(key).append(":").append(value));
        return stringBuilder.toString();
    }

    protected Function<T, Object> preparePreFunction() {
        var preFunction = super.preparePreFunction();
        if (Get.class.isAssignableFrom(preFunction.getClass())) {
            ((Get<?, ?>) preFunction).addIgnored(ignored2);
        }

        return ((Function<Object, Object>) o -> ofNullable(o).map(o1 -> {
            if (toReport && isValuable(o1)) {
                catchValue(o1, successCaptors);
            }
            return o1;
        }).orElse(false)).compose(preFunction);
    }

    @Override
    protected void onSuccess(Boolean result) {
        if (!result) {
            ofNullable(exceptionSupplier)
                    .ifPresent(supplier -> {
                        throw supplier.get();
                    });
        }
    }

    @Override
    public Presence<T> addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
        ignored2.addAll(toBeIgnored);
        return this;
    }

    @Override
    public Presence<T> addIgnored(Class<? extends Throwable> toBeIgnored) {
        ignored2.add(toBeIgnored);
        return this;
    }
}
