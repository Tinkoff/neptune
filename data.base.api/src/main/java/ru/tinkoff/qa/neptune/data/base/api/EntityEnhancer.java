package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.enhancer.DataNucleusEnhancer;
import org.reflections.Reflections;

import java.util.stream.Collectors;

public final class EntityEnhancer {
    private static final Reflections REFLECTIONS = new Reflections("");

    /**
     * Enhances entity classes. Classes that need to be be enhanced should implement {@link PersistableObject}.
     *
     * @param ignored ignored parameters
     */
    public static void main(String[] ignored) {
        DataNucleusEnhancer enhancer = new DataNucleusEnhancer("JPA", null);
        enhancer.setVerbose(true);

        REFLECTIONS.getSubTypesOf(PersistableObject.class).stream()
                .map(Class::getName).collect(Collectors.toList()).forEach(enhancer::addClasses);
        enhancer.enhance();
    }
}
