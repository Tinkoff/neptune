package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.AdditionalMetadata;

import java.io.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Boolean.parseBoolean;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.Thread.currentThread;
import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition.getKnownPartitions;

public class ResourceBundleGenerator {

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

        var custom = args.length > 2 && parseBoolean(args[2]);
        List<LocalizationBundlePartition> partitions;
        if (args.length <= 3) {
            partitions = getKnownPartitions();
        } else {
            partitions = new ArrayList<>();
            var known = getKnownPartitions();
            for (int i = 3; i < args.length; i++) {
                var currentIndex = i;
                partitions.add(known.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(args[currentIndex]))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No such bundle partition '" + args[currentIndex] + "'")));
            }
        }

        for (var p : partitions) {
            Properties currentProperties;

            if (custom) {
                currentProperties = propertiesFromStream(getResourceInputStream(p.getCustomBundleName()
                        + "_" + args[0] + ".properties"));
                if (currentProperties == null) {
                    currentProperties = propertiesFromStream(getResourceInputStream(p.getDefaultBundleName()
                            + "_" + args[0] + ".properties"));
                }
            } else {
                currentProperties = propertiesFromStream(getResourceInputStream(p.getDefaultBundleName()
                        + "_" + args[0] + ".properties"));
            }

            File propFile = new File(directory, (custom ? p.getCustomBundleName() : p.getDefaultBundleName())
                    + "_" + args[0] + ".properties");

            if (!propFile.exists()) {
                var created = propFile.createNewFile();
                if (!created) {
                    throw new IOException("Bundle file was not created for some reason");
                }
            }

            fillFile(p, propFile, currentProperties);
        }
    }

    private static void fillFile(LocalizationBundlePartition partition, File file, Properties properties) throws IOException {
        clearFile(file);

        var writer = new FileWriter(file, true);

        try (var output = new BufferedWriter(writer)) {
            output.write("#Values for translation of steps, their parameters, matchers and their descriptions," +
                    " and attachments are defined here. Format key = value");

            new StepBundleFilter(partition).fill(output, properties);
            new CriteriaBundleFilter(partition).fill(output, properties);
            new AttachmentsBundleFilter(partition).fill(output, properties);
            new MatchersBundleFilter(partition).fill(output, properties);
            new MismatchDescriptionBundleFilter(partition).fill(output, properties);
            new MatchedObjectsBundleFilter(partition).fill(output, properties);
            new OtherObjectsBundleFilter(partition).fill(output, properties);

            new ClassGraph().enableAllInfo()
                    .scan()
                    .getSubclasses(BundleFillerExtension.class.getName())
                    .loadClasses(BundleFillerExtension.class)
                    .stream()
                    .filter(cls -> !isAbstract(cls.getModifiers()) &&
                            !cls.equals(StepBundleFilter.class) &&
                            !cls.equals(CriteriaBundleFilter.class) &&
                            !cls.equals(AttachmentsBundleFilter.class) &&
                            !cls.equals(MatchersBundleFilter.class) &&
                            !cls.equals(MismatchDescriptionBundleFilter.class) &&
                            !cls.equals(MatchedObjectsBundleFilter.class) &&
                            !cls.equals(OtherObjectsBundleFilter.class) &&
                            ofNullable(cls.getAnnotation(BindToPartition.class))
                                    .map(b -> b.value().equalsIgnoreCase(partition.getName()))
                                    .orElse(false))
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
                AdditionalMetadata<?> additionalMetadata = ((AdditionalMetadata<?>) annotatedElement);
                key = cutPartOfPath(additionalMetadata.getDeclaringClass().getName()) + "." + additionalMetadata.getName();
            }
        }
        return key.replace(" ", "");
    }

    static String cutPartOfPath(String s) {
        return s.replace("ru.tinkoff.qa.neptune.", "");
    }

    static InputStream getResourceInputStream(String name) {
        return ofNullable(getSystemClassLoader()
                .getResourceAsStream(name))
                .orElseGet(() -> currentThread().getContextClassLoader().getResourceAsStream(name));
    }

    static Properties propertiesFromStream(InputStream is) throws IOException {
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
