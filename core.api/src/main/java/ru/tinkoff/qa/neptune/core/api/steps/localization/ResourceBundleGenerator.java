package ru.tinkoff.qa.neptune.core.api.steps.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.PseudoField;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import java.io.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultActionParameterReader.getPerformOnPseudoField;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefaultGetParameterReader.*;

public class ResourceBundleGenerator {
    public static final String RESOURCE_BUNDLE = "neptune_Localization";

    public static void main(String[] args) throws IOException {
        checkArgument(args.length >= 2, "There should be 2 arguments. " +
                "the first is a string in format language_country for ResourceBundle " +
                "The second is a path to the directory where property-file should be saved");

        File directory;
        if (isBlank(args[1])) {
            directory = new File(new File(args[1]).getAbsolutePath());
        } else {
            directory = new File(args[1]);
        }
        checkArgument(directory.exists(), "File " + directory.getAbsolutePath() + " doesn't exist");
        checkArgument(directory.isDirectory(), "File " + directory.getAbsolutePath() + " is not a directory");

        var currentProperties = propertiesFromStream(getResourceInputStream(RESOURCE_BUNDLE + "_" + args[0] + ".properties"));

        File propFile = new File(directory, RESOURCE_BUNDLE + "_" + args[0] + ".properties");

        if (!propFile.exists()) {
            var created = propFile.createNewFile();
            if (!created) {
                throw new IOException("Bundle file was not created for some reason");
            }
        }

        fillFile(propFile, currentProperties);
    }

    private static void fillFile(File file, Properties properties) throws IOException {
        clearFile(file);

        var writer = new FileWriter(file, true);

        try (var output = new BufferedWriter(writer)) {
            output.write("#Values for translation of steps, their parameters and attachments are defined here. Format key = value");

            var steps = new LinkedList<LocalizationItem>();
            of(SequentialActionSupplier.class, SequentialGetStepSupplier.class).forEach(cls -> {
                var nestMembers = asList(cls.getNestMembers());
                steps.addAll(new ClassGraph()
                        .enableAllInfo()
                        .scan()
                        .getSubclasses(cls.getName())
                        .loadClasses(cls)
                        .stream()
                        .filter(c -> !nestMembers.contains(c))
                        .map(aClass -> getLocalizationItem(aClass, output, properties))
                        .collect(toList()));
            });

            steps.sort(comparing(localizationItem -> localizationItem.getClazz().getName()));
            steps.addFirst(getLocalizationItem(SequentialActionSupplier.class, output, properties));
            steps.addFirst(getLocalizationItem(SequentialGetStepSupplier.class, output, properties));

            var attachments = new ClassGraph()
                    .enableAllInfo()
                    .scan()
                    .getSubclasses(Captor.class.getName())
                    .loadClasses(Captor.class)
                    .stream()
                    .map(aClass -> getLocalizationItem(aClass, output, properties))
                    .sorted(comparing(localizationItem -> localizationItem.getClazz().getName()))
                    .collect(toCollection(ArrayList::new));

            var criteriaList = new ClassGraph()
                    .enableAllInfo()
                    .scan()
                    .getClassesWithMethodAnnotation(Description.class.getName())
                    .loadClasses(true)
                    .stream()
                    .filter(classClass ->
                            !SequentialActionSupplier.class.isAssignableFrom(classClass)
                                    &&
                                    !SequentialGetStepSupplier.class.isAssignableFrom(classClass))
                    .map(aClass -> getLocalizationItem(aClass, output, properties))
                    .sorted(comparing(localizationItem -> localizationItem.getClazz().getName()))
                    .collect(toCollection(ArrayList::new));

            if (!steps.isEmpty()) {
                output.newLine();
                output.newLine();
                output.write("#============================================ STEPS " +
                        "============================================ ");

                for (var step : steps) {
                    step.fill();
                }
            }

            if (!criteriaList.isEmpty()) {
                output.newLine();
                output.newLine();
                output.write("#============================================ CRITERIA " +
                        "============================================ ");

                for (var criteria : criteriaList) {
                    criteria.fill();
                }
            }

            if (!attachments.isEmpty()) {
                output.newLine();
                output.newLine();
                output.write("#======================================= ATTACHMENTS " +
                        "============================================");

                for (var attachment : attachments) {
                    attachment.fill();
                }
            }
        }
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

    static String getKey(AnnotatedElement annotatedElement) {
        String key;

        if (annotatedElement instanceof Method) {
            Method method = (Method) annotatedElement;
            var clsName = method.getDeclaringClass().getName();

            key = cutPartOfPath(clsName) + "." + method.getName();
            key = key + "(" +
                    stream(method.getParameters())
                            .map(parameter -> cutPartOfPath(parameter.getParameterizedType().getTypeName()))
                            .collect(Collectors.joining(","))
                    + ")";
        } else if (annotatedElement instanceof Class) {
            key = cutPartOfPath(((Class<?>) annotatedElement).getName());
        } else {
            if (annotatedElement instanceof Field) {
                Field field = ((Field) annotatedElement);
                key = cutPartOfPath(field.getDeclaringClass().getName()) + "." + field.getName();
            } else {
                PseudoField pseudoField = ((PseudoField) annotatedElement);
                key = cutPartOfPath(pseudoField.getDeclaringClass().getName()) + "." + pseudoField.getName();
            }
        }
        return key.replace(" ", "");
    }

    private static String cutPartOfPath(String s) {
        return s.replace("ru.tinkoff.qa.neptune.", "");
    }

    private static InputStream getResourceInputStream(String name) {
        return ofNullable(getSystemClassLoader()
                .getResourceAsStream(name))
                .orElseGet(() -> currentThread().getContextClassLoader().getResourceAsStream(name));
    }

    private static Properties propertiesFromStream(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }

        var prop = new Properties();
        prop.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        return prop;
    }

    private static void clearFile(File file) throws FileNotFoundException {
        var writer = new PrintWriter(file);
        writer.print(EMPTY);
        writer.close();
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

        private Class<?> getClazz() {
            return clazz;
        }
    }
}
