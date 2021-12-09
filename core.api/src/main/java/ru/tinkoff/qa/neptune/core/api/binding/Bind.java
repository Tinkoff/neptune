package ru.tinkoff.qa.neptune.core.api.binding;

import io.github.classgraph.ClassGraph;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.contains;

/**
 * This annotation is used to bind classes to each other.
 * <p></p>
 * This annotation is applicable to classes and fields.
 * <p></p>
 * There is no universal usage of this annotation.
 * It is implemented specifically for each module of Neptune.
 * To make it with more comfort you can use {@link DefaultBindReader}.
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
@Repeatable(BindRepeatable.class)
public @interface Bind {

    /**
     * @return Class to apply the binding
     */
    Class<?> to();

    /**
     * @return to apply the binding to subclasses of a class defined by {@link #to()}
     */
    boolean withSubclasses() default false;

    /**
     * @return which classes should be excluded from the binding.
     * It has sense when {@link #withSubclasses()} is {@code true}
     */
    Class<?>[] exclude() default {};


    /**
     * Finds classes and fields annotated by {@link BindRepeatable} and provides easy access to
     * found metadata.
     */
    class DefaultBindReader {

        private static final List<Class<?>> ANNOTATED_CLASSES = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .ignoreClassVisibility()
                .scan()
                .getClassesWithAnnotation(Bind.class.getName())
                .loadClasses(true);

        private static final Set<Field> ANNOTATED_FIELDS = getAnnotatedFields();

        private static Set<Field> getAnnotatedFields() {
            var classes = new ClassGraph()
                    .enableClassInfo()
                    .enableFieldInfo()
                    .ignoreFieldVisibility()
                    .ignoreClassVisibility()
                    .enableAnnotationInfo()
                    .scan()
                    .getClassesWithFieldAnnotation(Bind.class.getName())
                    .loadClasses(true);

            var result = new HashSet<Field>();
            classes.forEach(aClass -> result.addAll(stream(aClass.getDeclaredFields())
                    .filter(f -> f.getAnnotationsByType(Bind.class).length > 0)
                    .peek(f -> f.setAccessible(true))
                    .collect(toList())));
            return result;
        }

        /**
         * Finds classes and fields bound to defined class by {@link Bind}
         *
         * @param cls is a class to get bound classes and fields
         * @return list of {@link AnnotatedElement}
         */
        public static List<AnnotatedElement> getBoundTo(Class<?> cls) {
            checkNotNull(cls);

            var annotatedElements = new ArrayList<AnnotatedElement>();
            annotatedElements.addAll(ANNOTATED_CLASSES);
            annotatedElements.addAll(ANNOTATED_FIELDS);

            return annotatedElements
                    .stream()
                    .filter(ae -> {
                        var a = ae.getAnnotationsByType(Bind.class);

                        return stream(a)
                                .anyMatch(a1 -> {
                                    var clz = a1.to();
                                    return (clz.equals(cls) || (a1.withSubclasses()
                                            && clz.isAssignableFrom(cls)
                                            && !contains(a1.exclude(), cls)));
                                });
                    }).collect(toList());
        }

    }
}
