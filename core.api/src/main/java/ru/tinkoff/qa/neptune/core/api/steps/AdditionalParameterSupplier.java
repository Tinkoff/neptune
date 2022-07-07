package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

final class AdditionalParameterSupplier implements Supplier<Map<String, String>> {

    private final Object calculateBy;
    private final boolean includeParametersOfInnerSteps;
    private final Supplier<Map<String, String>> extraParameterSupplier;

    AdditionalParameterSupplier(Object calculateBy, Object calculateFor, Supplier<Map<String, String>> additionalParameterSupplier) {
        this.calculateBy = calculateBy;
        includeParametersOfInnerSteps = calculateFor.getClass().getAnnotation(IncludeParamsOfInnerGetterStep.class) != null;
        this.extraParameterSupplier = additionalParameterSupplier;
    }

    @Override
    public Map<String, String> get() {
        var result = new LinkedHashMap<String, String>();

        if ((calculateBy instanceof SequentialGetStepSupplier<?, ?, ?, ?, ?>)
                && includeParametersOfInnerSteps) {
            var get = (SequentialGetStepSupplier<?, ?, ?, ?, ?>) calculateBy;
            var additional = get.calculatedParameters();

            if (!additional.isEmpty()) {
                result.putAll(additional);
            }
        }

        var additional = extraParameterSupplier.get();
        if (nonNull(additional) && !additional.isEmpty()) {
            result.putAll(additional);
        }
        return result;
    }
}
