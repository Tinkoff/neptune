package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.EMPTY;

final class BundleFiller {

    private final File file;
    private final Properties actual;
    private final List<BundleFillerExtension> bundleFillerExtensions = new LinkedList<>();

    BundleFiller(LocalizationBundlePartition partition, File file, Properties actual) {
        this.file = file;
        this.actual = actual;
        bundleFillerExtensions.add(new StepBundleFilter(partition));
        bundleFillerExtensions.add(new CriteriaBundleFilter(partition));
        bundleFillerExtensions.add(new AttachmentsBundleFilter(partition));
        bundleFillerExtensions.add(new MatchersBundleFilter(partition));
        bundleFillerExtensions.add(new MismatchDescriptionBundleFilter(partition));
        bundleFillerExtensions.add(new MatchedObjectsBundleFilter(partition));
        bundleFillerExtensions.add(new ParameterPojoBundleFilter(partition));

        new ClassGraph().enableAllInfo()
                .enableClassInfo()
                .ignoreClassVisibility()
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
                        !cls.equals(ParameterPojoBundleFilter.class) &&
                        !cls.equals(OtherObjectsBundleFilter.class) &&
                        stream(cls.getAnnotationsByType(BindToPartition.class))
                                .anyMatch(a -> a.value().equalsIgnoreCase(partition.getName())))
                .forEach(cls -> {
                    try {
                        var c = cls.getConstructor();
                        var instance = c.newInstance();
                        bundleFillerExtensions.add(instance);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                });

        var processedClasses = new ArrayList<Class<?>>();
        bundleFillerExtensions.forEach(e -> processedClasses.addAll(e.getProcessedClasses()));
        bundleFillerExtensions.add(new OtherObjectsBundleFilter(processedClasses, partition));
    }

    List<BundleFillerExtension> getBundleFillerExtensions() {
        return bundleFillerExtensions;
    }

    private void clearFile() throws FileNotFoundException {
        var writer = new PrintWriter(file);
        writer.print(EMPTY);
        writer.close();
    }

    void fillFile() throws IOException {
        clearFile();
        var writer = new FileWriter(file, true);

        try (var output = new BufferedWriter(writer)) {
            output.write("#Values for translation of steps, their parameters, matchers and their descriptions," +
                    " and attachments are defined here. Format key = value");

            for (var e : getBundleFillerExtensions()) {
                e.fill(output, actual);
            }
        }
    }
}
