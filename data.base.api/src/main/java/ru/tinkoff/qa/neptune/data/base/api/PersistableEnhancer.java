package ru.tinkoff.qa.neptune.data.base.api;

import io.github.classgraph.ClassGraph;
import org.datanucleus.enhancer.DataNucleusEnhancer;

import javax.jdo.annotations.PersistenceCapable;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.datanucleus.PropertyNames.PROPERTY_METADATA_ALWAYS_DETACHABLE;

public final class PersistableEnhancer {
    /**
     * Enhances persistable classes. Classes that need to be be enhanced should implement {@link PersistableObject}.
     *
     * @param ignored ignored parameters
     */
    public static void main(String[] ignored) {
        var props = new Properties();
        props.setProperty(PROPERTY_METADATA_ALWAYS_DETACHABLE, "true");

        var enhancer = new DataNucleusEnhancer("JDO", props);
        enhancer.setVerbose(true);

        enhancer.addClasses(new ClassGraph().enableClassInfo()
                .enableAllInfo()
                .scan()
                .getSubclasses(PersistableObject.class.getName())
                .loadClasses(PersistableObject.class)
                .stream()
                .filter(clazz -> nonNull(clazz.getAnnotation(PersistenceCapable.class)))
                .map(Class::getName).collect(Collectors.toList()).toArray(new String[]{}));
        enhancer.setDetachListener(true);

        enhancer.enhance();
    }
}
