package ru.tinkoff.qa.neptune.core.api.steps.localization;

import io.github.classgraph.ClassGraph;
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
            output.write("#Values for translation of steps and parameters are defined here. Format key = value");

            var children = new ArrayList<Class<?>>();
            of(SequentialActionSupplier.class, SequentialGetStepSupplier.class).forEach(cls -> {
                var nestMembers = asList(cls.getNestMembers());
                children.addAll(new ClassGraph()
                        .enableAllInfo()
                        .scan()
                        .getSubclasses(cls.getName())
                        .loadClasses(cls)
                        .stream()
                        .filter(c -> !nestMembers.contains(c))
                        .collect(toList()));
            });

            children.sort(comparing(Class::getName));

            children.forEach(child -> {
                try {
                    output.newLine();
                    output.write("#=============================================================================================");
                    output.newLine();
                    output.write("#====================== " + cutPartOfPath(child.getName()));
                    output.newLine();
                    output.write("#=============================================================================================");

                    var description = child.getAnnotation(Description.class);

                    if (description != null) {
                        var key = getKey(child);
                        var value = ofNullable(properties)
                                .map(p -> p.get(key))
                                .orElse(description.value());

                        output.newLine();
                        output.write("#_________________________________ Class Description _________________________________________");
                        output.newLine();
                        output.write("#Original text = " + description.value());
                        output.newLine();
                        output.write(key + " = " + value);
                    }

                    output.newLine();
                    output.write("#_________________________________ DefaultParameterNames _____________________________________");

                    pseudoFieldWriter(output, getFromPseudoField(child), properties);
                    pseudoFieldWriter(output, getPollingTimePseudoField(child), properties);
                    pseudoFieldWriter(output, getTimeOutPseudoField(child), properties);
                    pseudoFieldWriter(output, getCriteriaPseudoField(child), properties);
                    pseudoFieldWriter(output, getPerformOnPseudoField(child), properties);

                    var fields = stream(child.getDeclaredFields())
                            .filter(field -> field.getAnnotation(StepParameter.class) != null)
                            .collect(toList());

                    if (!fields.isEmpty()) {
                        output.newLine();
                        output.write("#__________________________________ StepParameter ____________________________________________");
                        fields.forEach(field -> {
                            field.setAccessible(true);
                            fill(output, field, properties);
                        });
                    }

                    var methods = stream(child.getDeclaredMethods())
                            .filter(method -> method.getAnnotation(Description.class) != null)
                            .collect(toList());

                    if (!methods.isEmpty()) {
                        output.newLine();
                        output.write("#__________________________________ Method Description _______________________________________");
                        methods.forEach(method -> {
                            method.setAccessible(true);
                            fill(output, method, properties);
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    static void fill(BufferedWriter writer, AnnotatedElement annotatedElement, Properties properties) {
        if (annotatedElement instanceof Method) {
            Description annotation = annotatedElement.getAnnotation(Description.class);
            var key = getKey(annotatedElement);
            var value = ofNullable(properties)
                    .map(p -> p.get(key))
                    .orElse(annotation.value());
            newLine(writer, annotation.value(), key, value);
        }
        if (annotatedElement instanceof Field) {
            StepParameter annotation = annotatedElement.getAnnotation(StepParameter.class);
            var key = getKey(annotatedElement);
            var value = ofNullable(properties)
                    .map(p -> p.get(key))
                    .orElse(annotation.value());
            newLine(writer, annotation.value(), key, value);
        }
    }

    static void newLine(BufferedWriter writer, String originalText, String key, Object value) {
        try {
            writer.newLine();
            writer.write("#Original text = " + originalText);
            writer.newLine();
            writer.write(key + " = " + value);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    static String cutPartOfPath(String s) {
        return s.replace("ru.tinkoff.qa.neptune.", "");
    }

    static void pseudoFieldWriter(BufferedWriter writer, PseudoField field, Properties properties) {
        ofNullable(field).ifPresent(pseudoField -> {
            try {
                var key = getKey(pseudoField);
                var value = ofNullable(properties)
                        .map(p -> p.get(key))
                        .orElse(pseudoField.getAnnotation(StepParameter.class).value());

                writer.newLine();
                writer.write("#Original text = " + pseudoField.getAnnotation(StepParameter.class).value());
                writer.newLine();
                writer.write(key + " = " + value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
