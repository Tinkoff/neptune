package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Optional.ofNullable;
import static java.util.ResourceBundle.getBundle;
import static java.util.stream.Collectors.toList;

/**
 * This class is needful for partitions of localization bundles
 */
public abstract class LocalizationBundlePartition {

    private static List<LocalizationBundlePartition> knownPartitions;

    private final String name;
    private final String defaultBundleName;
    private final String customBundleName;
    private final String packageName;
    private ResourceBundle defaultBundle;
    private ResourceBundle customBundle;
    private boolean isRead;

    protected LocalizationBundlePartition(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
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

    public final ResourceBundle getResourceBundle(Locale l) {
        if (!isRead) {
            try {
                defaultBundle = getBundle(defaultBundleName, l);
            } catch (MissingResourceException e) {
                defaultBundle = null;
            }

            try {
                customBundle = getBundle(customBundleName, l);
            } catch (MissingResourceException e) {
                customBundle = null;
            }

            isRead = true;
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
    public final String getPackageName() {
        return packageName;
    }

    public String getDefaultBundleName() {
        return defaultBundleName;
    }

    public String getCustomBundleName() {
        return customBundleName;
    }
}
