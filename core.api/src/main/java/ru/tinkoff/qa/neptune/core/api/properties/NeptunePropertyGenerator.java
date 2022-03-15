package ru.tinkoff.qa.neptune.core.api.properties;

import io.github.classgraph.ClassGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.AnnotatedElement;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.Map.Entry.comparingByKey;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.StringUtils.*;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.*;

public class NeptunePropertyGenerator {

    /**
     * Generates {@link GeneralPropertyInitializer#PROPERTIES} or {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES} files.
     * it depends on arguments.
     *
     * @param args there should be:
     *             - first argument - generate {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES} or
     *             {@link GeneralPropertyInitializer#PROPERTIES}. Acceptable values 'true' (global properties, for all dependent modules)
     *             and 'false'(local, only for this project/module)
     *             <p>
     *             - second argument - to inherit properties from {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES} or not.
     *             This value is ignored when the first argument is 'true' (to generate global properties, for all dependent modules).
     *             <p>
     *             When value is 'true' then a new {@link GeneralPropertyInitializer#PROPERTIES} will contain properties
     *             which have no value defined in {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES} of a core module.
     *             <p>
     *             When value is 'false' then a new {@link GeneralPropertyInitializer#PROPERTIES} will contain full list
     *             of properties. Properties which have values in {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES}
     *             will contain same values in a new {@link GeneralPropertyInitializer#PROPERTIES}.
     *             <p>
     *             - third argument - is a path to the directory where a new {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES}
     *             or {@link GeneralPropertyInitializer#PROPERTIES} should be saved.
     */
    public static void main(String... args) throws Exception {
        checkArgument(args.length >= 3, "There should be 3 arguments. " +
                "The first (true/false) is to generate global properties or not. The second is to inherit properties or not." +
                "The third is a path to the directory where property-file should be saved");
        File directory;
        if (isBlank(args[2])) {
            directory = new File(new File(args[2]).getAbsolutePath());
        } else {
            directory = new File(args[2]);
        }

        var dirPath = directory.toPath();
        var path = new String[]{dirPath.getRoot().toString()};

        path = addAll(path, StreamSupport.stream(dirPath.spliterator(), false)
                .map(Path::toString)
                .toArray(String[]::new));

        checkArgument(directory.exists(), "File " + directory.getAbsolutePath() + " doesn't exist");
        checkArgument(directory.isDirectory(), "File " + directory.getAbsolutePath() + " is not a directory");

        var isGlobal = parseBoolean(args[0]);
        var properties = getPropertyFile(directory, isGlobal);
        var all = ofNullable(getAllProperties(path)).orElseGet(Properties::new);
        var global = ofNullable(getGlobalProperties(path)).orElseGet(Properties::new);

        var propertyMap = isGlobal ? getPropertiesMetaMap(global) : getPropertiesMetaMap(all);
        var propertyList = new ArrayList<Property>();
        propertyMap.values().forEach(propertyList::addAll);

        writeFile(properties,
                propertyList,
                propertyMap,
                global,
                isGlobal,
                parseBoolean(args[1]));
    }

    private static void writeFile(File propFile, List<Property> properties, Map<String, List<Property>> propHashMap, Properties global, boolean isGlobal, boolean toInherit) throws Exception {
        var writer = new PrintWriter(propFile);
        writer.print(EMPTY);
        writer.close();

        if (!isGlobal && toInherit) {
            properties.forEach(p -> {
                if (global.containsKey(p.getName()) && Objects.equals(p.getValue(), global.get(p.getName()))) {
                    p.setCommented();
                }
            });
            writeFile(propFile, propHashMap);
            return;
        }
        writeFile(propFile, propHashMap);
    }

    private static void writeFile(File propFile, Map<String, List<Property>> propHashMap) throws Exception {
        var writer = new FileWriter(propFile, true);

        try (var output = new BufferedWriter(writer)) {
            output.write("#Neptune properties and some properties of JDK or 3rd party frameworks are defined there");

            propHashMap.forEach((s, properties) -> {
                try {
                    output.newLine();
                    output.write("#===========================" + s);

                    for (var p : properties) {
                        for (var c : p.getComment()) {
                            if (isNoneBlank(c)) {
                                output.newLine();
                                output.write("#" + c);
                            }
                        }
                        output.newLine();
                        output.write(p.getNameForFile() + "=" + p.getValue());
                    }
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            });
        }

    }

    private static Map<String, List<Property>> getPropertiesMetaMap(Properties props) {
        var propertyMap = new HashMap<String, List<Property>>();
        new ClassGraph()
                .enableClassInfo()
                .ignoreClassVisibility()
                .scan()
                .getClassesImplementing(PropertySupplier.class.getName())
                .loadClasses(PropertySupplier.class)
                .forEach(cls -> {
                    if (!cls.isEnum() && cls.getAnnotation(ExcludeFromExport.class) == null) {
                        var list = propertyMap.computeIfAbsent(getSection(cls), s -> new LinkedList<>());
                        var p = getProperty(cls, props);
                        ofNullable(p).ifPresent(list::add);
                        return;
                    }

                    stream(cls.getDeclaredFields())
                            .filter(field -> field.isEnumConstant() && field.getAnnotation(ExcludeFromExport.class) == null)
                            .forEach(field -> {
                                field.setAccessible(true);
                                var list = propertyMap.computeIfAbsent(getSection(field), s -> new LinkedList<>());
                                var p = getProperty(field, props);
                                ofNullable(p).ifPresent(list::add);
                            });
                });

        var propNames = new ArrayList<>();
        propertyMap.values().forEach(properties -> propNames.addAll(properties
                .stream()
                .map(Property::getName)
                .collect(toList())));


        props.forEach((o, o2) -> {
            if (!propNames.contains(valueOf(o))) {
                var list = propertyMap.computeIfAbsent("=== Properties of JDK or 3rd-party frameworks", s -> new LinkedList<>());
                list.add(new Property(valueOf(o), new String[]{}).setValue(valueOf(o2)));
            }
        });

        return propertyMap
                .entrySet()
                .stream()
                .filter(e -> !e.getValue().isEmpty())
                .sorted(comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    private static File getPropertyFile(File directory, boolean isGlobal) throws Exception {
        File properties;
        if (isGlobal) {
            properties = new File(directory, GLOBAL_PROPERTIES);
        } else {
            properties = new File(directory, PROPERTIES);
        }

        if (!properties.exists()) {
            properties.createNewFile();
        }

        return properties;
    }

    private static Property getProperty(AnnotatedElement e, Properties p) {
        var propName = e.getAnnotation(PropertyName.class);
        if (propName == null) {
            return null;
        }

        var name = propName.value();

        var propDefaultValue = ofNullable(e.getAnnotation(PropertyDefaultValue.class))
                .map(PropertyDefaultValue::value)
                .orElse(null);

        var propertyComment = ofNullable(e.getAnnotation(PropertyDescription.class))
                .map(propertyDescription -> ofNullable(propertyDescription.description()).orElse(new String[]{}))
                .orElseGet(() -> new String[]{});

        var value = p.containsKey(name) ? valueOf(p.get(name)) : propDefaultValue;

        return new Property(name, propertyComment)
                .setValue(value);
    }

    private static String getSection(AnnotatedElement e) {
        return ofNullable(e.getAnnotation(PropertyDescription.class))
                .map(PropertyDescription::section)
                .orElse(EMPTY);
    }


    private static class Property {
        private final String name;
        private final String[] comment;
        private String value;
        private String nameForFile;

        private Property(String name, String[] comment) {
            this.name = name;
            nameForFile = name;
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public Property setValue(String value) {
            this.value = ofNullable(value).orElse(EMPTY);
            return this;
        }

        public String[] getComment() {
            return comment;
        }

        public Property setCommented() {
            nameForFile = "#" + name;
            return this;
        }

        public String getNameForFile() {
            return nameForFile;
        }
    }
}
