package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

class FindWidgets<R extends Widget> implements Function<SearchContext, List<R>> {

    final Class<? extends R> classOfAWidget;
    private final TimeUnit timeUnit;
    private final long time;

    FindWidgets(Class<R> classOfAWidget, TimeUnit timeUnit, long time) {
        checkArgument(classOfAWidget != null, "The class to be instantiated should be defined.");
        checkArgument(timeUnit != null, "The waiting time unit should be defined.");
        checkArgument(time >= 0, "The waiting time should be positive.");
        this.classOfAWidget = classOfAWidget;
        this.timeUnit = timeUnit;
        this.time = time;
    }

    protected static <T extends Annotation> T getAnnotation(Class<? extends Widget> clazz,
                                                            Class<T> desiredAnnotation) {
        Class<?> superClass = clazz;
        while (!superClass.equals(Widget.class)) {
            T result = superClass.getAnnotation(desiredAnnotation);
            if (result != null) {
                return result;
            }
            superClass = superClass.getSuperclass();
        }
        return null;
    }

    List<Class<? extends R>> getSubclasses() {
        Predicate<Class<? extends R>> classPredicate =
                clazz -> !Modifier.isAbstract(clazz.getModifiers())

                        && (getAnnotation(clazz, FindBy.class) != null ||
                        getAnnotation(clazz, FindBys.class) != null ||
                        getAnnotation(clazz, FindAll.class) != null)

                        && (Arrays.stream(clazz.getDeclaredConstructors())
                        .filter(constructor -> {
                            Class<?>[] parameters = constructor.getParameterTypes();
                            return parameters.length == 1 &&
                                    WebElement.class.isAssignableFrom(parameters[0]);
                        }).collect(toList()).size() > 0);

        Reflections reflections = new Reflections("");

        ArrayList<Class<? extends R>> resultList = new ArrayList<>();
        resultList.addAll(reflections.getSubTypesOf(classOfAWidget).stream()
                .filter(classPredicate).collect(toList()));

        if (classPredicate.test(classOfAWidget)) {
            resultList.add(classOfAWidget);
        }

        if (resultList.size() > 0) {
            return resultList;
        }
        throw new IllegalArgumentException(format("There is no any non-abstract subclass of %s which " +
                        "has a constructor with only one parameter of a type extending %s",
                classOfAWidget.getName(), WebElement.class.getName()));
    }

    static <R extends Widget> Function<SearchContext, List<R>> widgets(Class<R> classOfAWidget,
                                                                       TimeUnit timeUnit,
                                                                       long time) {
        return toGet(format("Find elements of type %s", classOfAWidget.getName()),
                new FindWidgets<>(classOfAWidget, timeUnit, time));
    }

    @Override
    public List<R> apply(SearchContext searchContext) {
        return null;
    }
}
