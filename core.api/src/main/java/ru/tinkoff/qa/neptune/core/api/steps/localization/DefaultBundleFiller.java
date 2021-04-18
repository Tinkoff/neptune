package ru.tinkoff.qa.neptune.core.api.steps.localization;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultActionParameterReader.getPerformOnPseudoField;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefaultGetParameterReader.*;

final class DefaultBundleFiller extends BundleFillerExtension {

    protected DefaultBundleFiller(List<? extends Class<?>> toAdd, String sectionName) {
        super(toAdd, sectionName);
    }

    @Override
    protected List<AnnotatedElement> addFields(Class<?> clazz) {
        var fields = new ArrayList<AnnotatedElement>();

        ofNullable(getImperativePseudoField(clazz, false)).ifPresent(fields::add);
        ofNullable(getFromPseudoField(clazz, false)).ifPresent(fields::add);
        ofNullable(getPollingTimePseudoField(clazz, false)).ifPresent(fields::add);
        ofNullable(getTimeOutPseudoField(clazz, false)).ifPresent(fields::add);
        ofNullable(getCriteriaPseudoField(clazz, false)).ifPresent(fields::add);

        ofNullable(SequentialActionSupplier.DefaultActionParameterReader
                .getImperativePseudoField(clazz, false)).ifPresent(fields::add);
        ofNullable(getPerformOnPseudoField(clazz, false)).ifPresent(fields::add);
        ofNullable(getResultPseudoField(clazz, false)).ifPresent(fields::add);

        fields.addAll(stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(StepParameter.class) != null)
                .collect(toList()));

        return fields;
    }

    @Override
    protected List<Method> addMethods(Class<?> clazz) {
        return stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(Description.class) != null)
                .collect(toList());
    }
}
