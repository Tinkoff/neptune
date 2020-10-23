package ru.tinkoff.qa.neptune.http.api.service.mapping;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;

/**
 * This annotation is used to bind subclasses of {@link HttpAPI} with their default settings
 * such as default root of API (at this case the annotation should be used with {@link URLValuePropertySupplier})
 * and a set of request tuners (at this case the annotation should be used with {@link RequestTuner}).
 * <p>
 * This annotation may mark classes which implement {@link URLValuePropertySupplier} or {@link RequestTuner}
 * or fields of enums which implement {@link URLValuePropertySupplier}.
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
@Repeatable(APIUsesRepeatable.class)
@SuppressWarnings("rawtypes")
public @interface APIUses {

    /**
     * @return Subclass of {@link HttpAPI} to apply a setting
     */
    Class<? extends HttpAPI> usedBy();

    /**
     * @return to use th setting by subclasses of a class defined by {@link #usedBy()}
     */
    boolean isUsedBySubclasses() default false;

    /**
     * @return which classes should be excluded by the setting applying.
     * It has sense when {@link #isUsedBySubclasses()} is {@code true}
     */
    Class<? extends HttpAPI>[] exclude() default {};

    class UsedByAPIReader {

        private static final List<Class<URLValuePropertySupplier>> URL_PROPERTIES = new ClassGraph()
                .enableAllInfo()
                .scan().getClassesImplementing(URLValuePropertySupplier.class.getName())
                .loadClasses(URLValuePropertySupplier.class);

        private static final List<Class<RequestTuner>> REQUEST_TUNERS = new ClassGraph()
                .enableAllInfo()
                .scan().getClassesImplementing(RequestTuner.class.getName())
                .loadClasses(RequestTuner.class);

        @SuppressWarnings("unchecked")
        static URLValuePropertySupplier getDefaultURLProperty(Class<? extends HttpAPI<?>> toBindWith) {
            List<AnnotatedElement> annotatedElements = new ArrayList<>();
            URL_PROPERTIES.forEach(urlValueSupplier -> {
                var fieldList = stream(urlValueSupplier.getDeclaredFields())
                        .filter(field -> field.isEnumConstant() && field.getAnnotationsByType(APIUses.class).length > 0)
                        .collect(toList());

                if (fieldList.size() > 0) {
                    annotatedElements.addAll(fieldList);
                    return;
                }

                var a = urlValueSupplier.getAnnotationsByType(APIUses.class);
                if (a.length > 0) {
                    annotatedElements.add(urlValueSupplier);
                }
            });

            return annotatedElements
                    .stream()
                    .filter(annotatedElement -> {
                        var a = annotatedElement.getAnnotationsByType(APIUses.class);
                        if (a.length == 0) {
                            return false;
                        }

                        return stream(a)
                                .anyMatch(a1 -> {
                                    var clz = a1.usedBy();
                                    return (clz.equals(toBindWith) || (a1.isUsedBySubclasses()
                                            && clz.isAssignableFrom(toBindWith)
                                            && !contains(a1.exclude(), toBindWith)));
                                });
                    })
                    .findFirst()
                    .map(annotatedElement -> {
                        if (annotatedElement instanceof Field) {
                            var f = (Field) annotatedElement;
                            f.setAccessible(true);
                            var cls = f.getDeclaringClass();
                            if (cls.isAnonymousClass()) {
                                cls = cls.getSuperclass();
                            }

                            for (var ec : cls.getEnumConstants()) {
                                if (((Enum<?>) ec).name().equals(f.getName())) {
                                    return (URLValuePropertySupplier) ec;
                                }
                            }
                            return null;
                        } else {
                            var c = (Class<URLValuePropertySupplier>) annotatedElement;
                            return stream(c.getDeclaredFields())
                                    .filter(f -> isStatic(f.getModifiers()) && (f.getType().equals(c)))
                                    .findFirst()
                                    .map(f -> {
                                        f.setAccessible(true);
                                        try {
                                            return (URLValuePropertySupplier) f.get(c);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .orElseGet(() -> {
                                        try {
                                            var constructor = c.getConstructor();
                                            constructor.setAccessible(true);
                                            return constructor.newInstance();
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    });

                        }
                    })
                    .orElse(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY);
        }

        static List<Class<RequestTuner>> getRequestTuners(Class<? extends HttpAPI<?>> toBindWith) {
            return REQUEST_TUNERS
                    .stream()
                    .filter(c -> {
                        var a = c.getAnnotationsByType(APIUses.class);
                        if (a.length == 0) {
                            return false;
                        }

                        return stream(a)
                                .anyMatch(a1 -> {
                                    var clz = a1.usedBy();
                                    return (clz.equals(toBindWith) || (a1.isUsedBySubclasses()
                                            && clz.isAssignableFrom(toBindWith)
                                            && !contains(a1.exclude(), toBindWith)));
                                });
                    })
                    .collect(toList());
        }
    }
}
