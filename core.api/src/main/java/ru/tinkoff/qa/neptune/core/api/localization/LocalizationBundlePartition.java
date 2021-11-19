package ru.tinkoff.qa.neptune.core.api.localization;

import java.io.IOException;
import java.util.*;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
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
    private final Map<Locale, Map<String, String>> bundleContent = new HashMap<>();

    protected LocalizationBundlePartition(String name, String... packageName) {
        this.name = name;
        this.packageName = asList(packageName);
        this.defaultBundleName = "neptune_Localization" + "_" + name;
        customBundleName = this.defaultBundleName + "_CUSTOM";
    }

    static List<LocalizationBundlePartition> getKnownPartitions() {
        knownPartitions = ofNullable(knownPartitions)
                .orElseGet(() -> {
                    var iterator = load(LocalizationBundlePartition.class).iterator();
                    Iterable<LocalizationBundlePartition> iterable = () -> iterator;
                    return stream(iterable.spliterator(), false).collect(toList());
                });

        return knownPartitions;
    }

    private static Properties getBundleBuName(String name, Locale l) {
        try {
            return propertiesFromStream(
                    getResourceInputStream(name + "_" + l + ".properties"));
        } catch (IOException e) {
            return null;
        }
    }

    private static void fillMap(Map<String, String> result, Properties properties) {
        properties.forEach((k, v) -> {
            if (nonNull(v)) {
                result.put(valueOf(k), valueOf(v));
            } else {
                result.put(valueOf(k), null);
            }
        });
    }

    final synchronized Map<String, String> getResourceBundle(Locale l) {
        var result = new LinkedHashMap<String, String>();

        if (!bundleContent.containsKey(l)) {
            var defaultBundle = getBundleBuName(getDefaultBundleName(), l);
            var customBundle = getBundleBuName(getCustomBundleName(), l);
            if (nonNull(defaultBundle)) {
                fillMap(result, defaultBundle);
            }
            if (nonNull(customBundle)) {
                fillMap(result, customBundle);
            }
            bundleContent.put(l, result);
            return result;
        } else {
            return bundleContent.get(l);
        }
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
