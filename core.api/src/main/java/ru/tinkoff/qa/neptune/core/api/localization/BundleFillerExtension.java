package ru.tinkoff.qa.neptune.core.api.localization;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Metadata;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator.cutPartOfPath;
import static ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator.getKey;

public abstract class BundleFillerExtension {

    private final List<? extends Class<?>> toAdd;
    private final String sectionName;

    protected BundleFillerExtension(List<? extends Class<?>> toAdd, String sectionName) {
        this.toAdd = toAdd;
        this.sectionName = sectionName;
    }

    private static LocalizationItem getLocalizationItem(Class<?> clazz, BundleFillerExtension ext, BufferedWriter output, Properties properties) {
        var item = new LocalizationItem(clazz, output, properties);
        return item.addFields(ext.addFields(clazz)).addMethods(ext.addMethods(clazz));
    }

    protected abstract List<AnnotatedElement> addFields(Class<?> clazz);

    protected abstract List<Method> addMethods(Class<?> clazz);

    final void fill(BufferedWriter output, Properties properties) throws IOException {
        if (!toAdd.isEmpty()) {
            output.newLine();
            output.newLine();
            output.write("#============================================ "
                    + sectionName
                    + " ============================================ ");

            var items = toAdd.stream()
                    .map(cls -> getLocalizationItem(cls, this, output, properties))
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

                newLine(description.value(), key, value);
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
                        .orElseGet(() -> ofNullable(annotation).map(Description::value).orElse(null));

                value = ofNullable(value).orElse(EMPTY);
                newLine(ofNullable(annotation).map(Description::value).orElse(EMPTY), key, value);
                return;
            }

            var memberAnnotation = stream(annotatedElement.getAnnotations())
                    .filter(annotation -> annotation.annotationType().getAnnotation(Metadata.class) != null)
                    .findFirst()
                    .orElse(null);

            try {
                var m = memberAnnotation.annotationType().getDeclaredMethod("value");
                m.setAccessible(true);
                var annotationValue = (String) m.invoke(memberAnnotation);

                var key = getKey(annotatedElement);
                var value = ofNullable(properties)
                        .map(p -> p.get(key))
                        .orElse(annotationValue);
                newLine(annotationValue, key, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void newLine(String originalText, String key, Object value) throws IOException {
            output.newLine();

            var textLines = originalText.lines().collect(toList());
            if (textLines.size() == 1 || isBlank(originalText)) {
                output.write("#Original text = " + originalText);
            } else {
                output.write("#Original text = " + textLines.get(0));
                for (int i = 1; i < textLines.size(); i++) {
                    output.newLine();
                    output.write("#" + textLines.get(i));
                }
            }

            var stringVal = valueOf(value);
            var valueLines = stringVal.lines().collect(toList());
            output.newLine();

            if (valueLines.size() == 1 || isBlank(stringVal)) {
                output.write(key + "=" + value);
            } else {
                output.write(key + "=" + valueLines.get(0) + "\\r\\n\\");
                for (int i = 1; i < valueLines.size(); i++) {
                    output.newLine();
                    if (i < valueLines.size() - 1) {
                        output.write(valueLines.get(i) + "\\r\\n\\");
                    } else {
                        output.write(valueLines.get(i));
                    }
                }
            }
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
