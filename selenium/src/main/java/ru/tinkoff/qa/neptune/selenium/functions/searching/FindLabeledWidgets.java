package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindByBuilder.getAnnotation;
import static java.util.stream.Collectors.toList;

class FindLabeledWidgets<R extends Widget> extends FindWidgets<R> {

    private FindLabeledWidgets(Class<R> classOfAWidget, String conditionString) {
        super(classOfAWidget, conditionString, clazz -> !Modifier.isAbstract(clazz.getModifiers())
                && Labeled.class.isAssignableFrom(clazz)

                && (getAnnotation(clazz, FindBy.class) != null ||
                getAnnotation(clazz, FindBys.class) != null ||
                getAnnotation(clazz, FindAll.class) != null)

                && (Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> {
                    Class<?>[] parameters = constructor.getParameterTypes();
                    return parameters.length == 1 &&
                            WebElement.class.isAssignableFrom(parameters[0]);
                }).collect(toList()).size() > 0));
    }

    static <R extends Widget> Function<SearchContext, List<R>> labeledWidgets(Class<R> classOfAWidget,
                                                                              String conditionString) {
        return new FindLabeledWidgets<>(classOfAWidget, conditionString);
    }
}
