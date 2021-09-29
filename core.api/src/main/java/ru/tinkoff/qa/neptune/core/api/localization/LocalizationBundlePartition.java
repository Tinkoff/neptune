package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;

import java.io.IOException;
import java.util.*;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator.getResourceInputStream;
import static ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator.propertiesFromStream;

/**
 * This class is needful for partitions of localization bundles
 */
public abstract class LocalizationBundlePartition {

    private static List<LocalizationBundlePartition> knownPartitions;

    private final String name;
    private final String defaultBundleName;
    private final String customBundleName;
    private final List<String> packageName;
    private final Map<Locale, Properties> defaultBundles = new HashMap<>();
    private final Map<Locale, Properties> customBundles = new HashMap<>();

    protected LocalizationBundlePartition(String name, String... packageName) {
        this.name = name;
        this.packageName = asList(packageName);
        this.defaultBundleName = "neptune_Localization" + "_" + name;
        customBundleName = this.defaultBundleName + "_CUSTOM";
    }

    static List<LocalizationBundlePartition> getKnownPartitions() {
        knownPartitions = ofNullable(knownPartitions)
                .orElseGet(() -> new ClassGraph()
                        .enableAllInfo()
                        .scan()
                        .getSubclasses(LocalizationBundlePartition.class.getName())
                        .loadClasses(LocalizationBundlePartition.class)
                        .stream()
                        .filter(c -> !isAbstract(c.getModifiers()))
                        .map(cls -> {
                            try {
                                var c = cls.getConstructor();
                                c.setAccessible(true);
                                return c.newInstance();
                            } catch (Throwable t) {
                                throw new RuntimeException(t);
                            }
                        })
                        .collect(toList()));

        return knownPartitions;
    }

    final synchronized Properties getResourceBundle(Locale l) {
        Properties defaultBundle;
        Properties customBundle;
        if (!defaultBundles.containsKey(l)) {
            try {
                defaultBundle = propertiesFromStream(
                        getResourceInputStream(getDefaultBundleName() + "_" + l + ".properties"));
            } catch (IOException e) {
                defaultBundle = null;
            }
            defaultBundles.put(l, defaultBundle);
        } else {
            defaultBundle = defaultBundles.get(l);
        }

        if (!customBundles.containsKey(l)) {
            try {
                customBundle = propertiesFromStream(
                        getResourceInputStream(getCustomBundleName() + "_" + l + ".properties"));
            } catch (IOException e) {
                customBundle = null;
            }
            customBundles.put(l, customBundle);
        } else {
            customBundle = customBundles.get(l);
        }

        if (defaultBundle == null && customBundle == null) {
            return null;
        }

        if (customBundle != null) {
            return customBundle;
        }

        return defaultBundle;
    }

    /**
     * @return name of a partition
     */
    public final String getName() {
        return name;
    }

    /**
     * @return name of a root package of a module
     */
    public final List<String> getPackageNames() {
        return packageName;
    }

    public String getDefaultBundleName() {
        return defaultBundleName;
    }

    public String getCustomBundleName() {
        return customBundleName;
    }
}
