package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

class FindLabeledWidgets<R extends Widget> extends FindWidgets<R> {

    FindLabeledWidgets(Class<R> classOfAWidget, TimeUnit timeUnit, long time) {
        super(classOfAWidget, timeUnit, time);
    }

    @Override
    List<Class<? extends R>> getSubclasses() {
        Predicate<Class<? extends R>> classPredicate =
                clazz -> !Modifier.isAbstract(clazz.getModifiers())
                        && Labeled.class.isAssignableFrom(clazz)

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
        throw new IllegalArgumentException(format("There is no any non-abstract subclass of %s " +
                        "that also implements %s and has a constructor with only one parameter of a type extending %s",
                classOfAWidget.getName(),
                Labeled.class.getName(),
                WebElement.class));
    }

    static <R extends Widget> Function<SearchContext, List<R>> labeledWidgets(Class<R> classOfAWidget,
                                                                       TimeUnit timeUnit,
                                                                       long time) {
        return toGet(format("Find elements of type %s", classOfAWidget.getName()),
                new FindLabeledWidgets<>(classOfAWidget, timeUnit, time));
    }
}
