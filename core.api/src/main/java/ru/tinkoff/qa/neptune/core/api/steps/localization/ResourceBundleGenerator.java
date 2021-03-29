package ru.tinkoff.qa.neptune.core.api.steps.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.*;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import java.io.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefaultParameterNames.DefaultActionParameterReader.getPerformOnPseudoField;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefaultParameterNames.DefaultGetParameterReader.*;

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
            propFile.createNewFile();
        }

        fillFile(propFile, currentProperties);
    }

    private static void fillFile(File file, Properties properties) throws IOException {
        clearFile(file);

        var writer = new FileWriter(file, true);

        try (var output = new BufferedWriter(writer)) {
            output.write("#Values for translation of steps, their parameters and attachments are defined here. Format key = value");

            var steps = new ArrayList<Class<?>>();
            of(SequentialActionSupplier.class, SequentialGetStepSupplier.class).forEach(cls -> {
                var nestMembers = asList(cls.getNestMembers());
                steps.addAll(new ClassGraph()
                        .enableAllInfo()
                        .scan()
                        .getSubclasses(cls.getName())
                        .loadClasses(cls)
                        .stream()
                        .filter(c -> !nestMembers.contains(c))
                        .collect(toList()));
            });

            steps.sort(comparing(Class::getName));

            if (!steps.isEmpty()) {
                output.newLine();
                output.newLine();
                output.write("#============================================ Steps " +
                        "============================================ ");

                for (var step : steps) {
                    addClass(output, step, properties);

                    var fields = new ArrayList<AnnotatedElement>();
                    var isNotAbsenceAndPresence = !step.equals(Absence.class) && !step.equals(Presence.class);

                    if (isNotAbsenceAndPresence) {
                        ofNullable(getFromPseudoField(step)).ifPresent(fields::add);
                        ofNullable(getPollingTimePseudoField(step)).ifPresent(fields::add);
                    }

                    if (!step.equals(Presence.class)) {
                        ofNullable(getTimeOutPseudoField(step)).ifPresent(fields::add);
                    }

                    if (isNotAbsenceAndPresence) {
                        ofNullable(getCriteriaPseudoField(step)).ifPresent(fields::add);
                    }

                    ofNullable(getPerformOnPseudoField(step)).ifPresent(fields::add);

                    fields.addAll(stream(step.getDeclaredFields())
                            .filter(field -> field.getAnnotation(StepParameter.class) != null)
                            .collect(toList()));

                    if (!fields.isEmpty()) {
                        output.newLine();
                        output.write("#_________________________________Parameters_____________________________________");
                    }

                    for (var field : fields) {
                        if (field instanceof Field) {
                            ((Field) field).setAccessible(true);
                        }
                        fill(output, field, properties);
                    }

                    var methods = stream(step.getDeclaredMethods())
                            .filter(method -> method.getAnnotation(Description.class) != null)
                            .collect(toList());

                    if (!methods.isEmpty()) {
                        output.newLine();
                        output.write("#__________________________________ Methods _______________________________________");
                    }

                    for (var method : methods) {
                        method.setAccessible(true);
                        fill(output, method, properties);
                    }
                }

                output.newLine();
                output.newLine();
                output.write("#============================================ Steps " +
                        "============================================ ");
            }

            var attachments = new ArrayList<Class<?>>(new ClassGraph()
                    .enableAllInfo()
                    .scan()
                    .getSubclasses(Captor.class.getName())
                    .loadClasses(Captor.class));

            if (!attachments.isEmpty()) {
                output.newLine();
                output.newLine();
                output.write("#======================================= Attachments " +
                        "============================================");

                for (var attachment : attachments) {
                    if (attachment.getAnnotation(Description.class) != null) {
                        addClass(output, attachment, properties);
                    }
                }
                output.newLine();
                output.newLine();
                output.write("#======================================= Attachments " +
                        "============================================");
            }
        }
    }

    private static void addClass(BufferedWriter output, Class<?> clazz, Properties properties) throws IOException {
        output.newLine();
        output.newLine();
        output.write("#====================== " + cutPartOfPath(clazz.getName()));

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

    private static void fill(BufferedWriter writer, AnnotatedElement annotatedElement, Properties properties) throws IOException {
        if (annotatedElement instanceof Method) {
            Description annotation = annotatedElement.getAnnotation(Description.class);
            var key = getKey(annotatedElement);
            var value = ofNullable(properties)
                    .map(p -> p.get(key))
                    .orElse(annotation.value());
            newLine(writer, annotation.value(), key, value);
            return;
        }

        StepParameter annotation = annotatedElement.getAnnotation(StepParameter.class);
        var key = getKey(annotatedElement);
        var value = ofNullable(properties)
                .map(p -> p.get(key))
                .orElse(annotation.value());
        newLine(writer, annotation.value(), key, value);
    }

    static void newLine(BufferedWriter writer, String originalText, String key, Object value) throws IOException {
        writer.newLine();
        writer.write("#Original text = " + originalText);
        writer.newLine();
        writer.write(key + " = " + value);
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

    private static Properties propertiesFromStream(InputStream is) {
        return ofNullable(is)
                .map(inputStream -> {
                    var prop = new Properties();
                    try {
                        prop.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return prop;
                })
                .orElse(null);
    }

    private static void clearFile(File file) throws FileNotFoundException {
        var writer = new PrintWriter(file);
        writer.print(EMPTY);
        writer.close();
    }
}
