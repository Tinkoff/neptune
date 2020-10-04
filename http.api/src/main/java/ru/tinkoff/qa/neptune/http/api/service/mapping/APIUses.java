package ru.tinkoff.qa.neptune.http.api.service.mapping;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

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
 * and a set of request tuners (at this case the annotation should be used with {@link RequestTuner})
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(APIUsesRepeatable.class)
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

        static URLValuePropertySupplier getDefaultURLProperty(Class<? extends HttpAPI<?>> toBindWith) {
            return new ClassGraph()
                    .enableAllInfo()
                    .scan().getClassesImplementing(URLValuePropertySupplier.class.getName())
                    .loadClasses(URLValuePropertySupplier.class)
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
                    .findFirst()
                    .map(s -> stream(s.getDeclaredFields())
                            .filter(f -> isStatic(f.getModifiers()) && (f.getType().equals(s)))
                            .findFirst()
                            .map(f -> {
                                f.setAccessible(true);
                                try {
                                    return (URLValuePropertySupplier) f.get(s);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .orElseGet(() -> {
                                try {
                                    var c = s.getConstructor();
                                    c.setAccessible(true);
                                    return c.newInstance();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }))
                    .orElse(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY);
        }

        static List<Class<RequestTuner>> getRequestTuners(Class<? extends HttpAPI<?>> toBindWith) {
            return new ClassGraph()
                    .enableAllInfo()
                    .scan().getClassesImplementing(RequestTuner.class.getName())
                    .loadClasses(RequestTuner.class)
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
