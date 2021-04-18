package ru.tinkoff.qa.neptune.core.api.steps.localization;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultActionParameterReader.getPerformOnPseudoField;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefaultGetParameterReader.*;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator.cutPartOfPath;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator.getKey;

public abstract class BundleFillerExtension {

    private final List<? extends Class<?>> toAdd;
    private final String sectionName;

    protected BundleFillerExtension(List<? extends Class<?>> toAdd, String sectionName) {
        this.toAdd = toAdd;
        this.sectionName = sectionName;
    }

    private static LocalizationItem getLocalizationItem(Class<?> clazz, BufferedWriter output, Properties properties) {
        var item = new LocalizationItem(clazz, output, properties);

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

        var methods = stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(Description.class) != null)
                .collect(toList());

        return item.addFields(fields).addMethods(methods);
    }

    final void fill(BufferedWriter output, Properties properties) throws IOException {
        if (toAdd.size() > 0) {
            output.newLine();
            output.newLine();
            output.write("#============================================ "
                    + sectionName
                    + " ============================================ ");

            var items = toAdd.stream()
                    .map(cls -> getLocalizationItem(cls, output, properties))
                    .collect(toList());

            for (var item : items) {
                item.fill();
            }
        }
    }

    private final static class LocalizationItem {
        private final Class<?> clazz;
        private final BufferedWriter output;
        private final Properties properties;
        private final List<AnnotatedElement> fields = new ArrayList<>();
        private final List<Method> methods = new ArrayList<>();

        private LocalizationItem(Class<?> aClass, BufferedWriter output, Properties properties) {
            this.clazz = aClass;
            this.output = output;
            this.properties = properties;
        }

        private void addClass() throws IOException {
            output.newLine();
            output.newLine();
            output.write("######################## " + cutPartOfPath(clazz.getName()) + " #");

            var description = clazz.getAnnotation(Description.class);

            if (description != null) {
                var key = getKey(clazz);
                var value = ofNullable(properties)
                        .map(p -> p.get(key))
                        .orElse(description.value());

                output.newLine();
                output.write("#Original text = " + description.value());
                output.newLine();
                output.write(key + " = " + value);
            }
        }

        private void fill() throws IOException {
            if (clazz.getAnnotation(Description.class) == null && fields.isEmpty() && methods.isEmpty()) {
                return;
            }

            addClass();
            if (!fields.isEmpty()) {
                output.newLine();
                output.write("#_________________________________Parameters_____________________________________");
            }

            for (var field : fields) {
                if (field instanceof Field) {
                    ((Field) field).setAccessible(true);
                }
                fill(field);
            }

            if (!methods.isEmpty()) {
                output.newLine();
                output.write("#__________________________________ Methods _______________________________________");
            }

            for (var method : methods) {
                method.setAccessible(true);
                fill(method);
            }
        }

        private void fill(AnnotatedElement annotatedElement) throws IOException {
            if (annotatedElement instanceof Method) {
                Description annotation = annotatedElement.getAnnotation(Description.class);
                var key = getKey(annotatedElement);
                var value = ofNullable(properties)
                        .map(p -> p.get(key))
                        .orElse(annotation.value());
                newLine(annotation.value(), key, value);
                return;
            }

            StepParameter annotation = annotatedElement.getAnnotation(StepParameter.class);
            var key = getKey(annotatedElement);
            var value = ofNullable(properties)
                    .map(p -> p.get(key))
                    .orElse(annotation.value());
            newLine(annotation.value(), key, value);
        }

        private void newLine(String originalText, String key, Object value) throws IOException {
            output.newLine();
            output.write("#Original text = " + originalText);
            output.newLine();
            output.write(key + " = " + value);
        }

        private LocalizationItem addFields(Collection<AnnotatedElement> annotatedElements) {
            fields.addAll(annotatedElements);
            return this;
        }

        private LocalizationItem addMethods(Collection<Method> annotatedElements) {
            methods.addAll(annotatedElements);
            return this;
        }
    }
}
