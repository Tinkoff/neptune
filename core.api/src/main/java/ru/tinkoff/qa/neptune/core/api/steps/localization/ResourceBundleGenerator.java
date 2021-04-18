package ru.tinkoff.qa.neptune.core.api.steps.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.PseudoField;

import java.io.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.Thread.currentThread;
import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

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

            var steps = new LinkedList<Class<?>>();
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
            steps.addFirst(SequentialActionSupplier.class);
            steps.addFirst(SequentialGetStepSupplier.class);

            var attachments = new ClassGraph()
                    .enableAllInfo()
                    .scan()
                    .getSubclasses(Captor.class.getName())
                    .loadClasses(Captor.class)
                    .stream()
                    .map(cls -> (Class<?>) cls)
                    .sorted(comparing(Class::getName))
                    .collect(toList());

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
                    .sorted(comparing(Class::getName))
                    .collect(toCollection(ArrayList::new));

            new DefaultBundleFiller(steps, "STEPS").fill(output, properties);
            new DefaultBundleFiller(criteriaList, "CRITERIA").fill(output, properties);
            new DefaultBundleFiller(attachments, "ATTACHMENTS").fill(output, properties);

            new ClassGraph().enableAllInfo()
                    .scan()
                    .getSubclasses(BundleFillerExtension.class.getName())
                    .loadClasses(BundleFillerExtension.class)
                    .stream()
                    .filter(cls -> !isAbstract(cls.getModifiers()) && !cls.equals(DefaultBundleFiller.class))
                    .forEach(cls -> {
                        try {
                            var c = cls.getConstructor();
                            c.setAccessible(true);
                            c.newInstance().fill(output, properties);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
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
                PseudoField<?> pseudoField = ((PseudoField<?>) annotatedElement);
                key = cutPartOfPath(pseudoField.getDeclaringClass().getName()) + "." + pseudoField.getName();
            }
        }
        return key.replace(" ", "");
    }

    static String cutPartOfPath(String s) {
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
}
