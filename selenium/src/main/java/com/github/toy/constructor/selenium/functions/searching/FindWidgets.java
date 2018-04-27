package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Widget;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.selenium.api.widget.Widget.getWidgetName;
import static com.github.toy.constructor.selenium.functions.searching.FindByBuilder.getAnnotation;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;
import static org.apache.commons.lang3.StringUtils.isBlank;

class FindWidgets<R extends Widget> implements Function<SearchContext, List<R>> {

    private static final FindByBuilder builder = new FindByBuilder();
    final Class<? extends R> classOfAWidget;
    private final String conditionString;

    FindWidgets(Class<R> classOfAWidget, String conditionString) {
        checkArgument(classOfAWidget != null, "The class to be instantiated should be defined.");
        checkArgument(!isBlank(conditionString), "Description of the condition should not be empty.");
        this.classOfAWidget = classOfAWidget;
        this.conditionString = conditionString;
    }

    static <R extends Widget> Function<SearchContext, List<R>> widgets(Class<R> classOfAWidget,
                                                                       String conditionString) {
        return toGet(format("Elements of type %s", getWidgetName(classOfAWidget)),
                new FindWidgets<>(classOfAWidget, conditionString));
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

        List<Class<? extends R>> resultList = reflections.getSubTypesOf(classOfAWidget).stream()
                .filter(classPredicate).collect(toList());

        if (classPredicate.test(classOfAWidget)) {
            resultList.add(classOfAWidget);
        }

        if (resultList.size() > 0) {
            return resultList;
        }
        throw new IllegalArgumentException(format("There is no any non-abstract subclass of %s which " +
                        "is annotated by any org.openqa.selenium.support.Find* annotation " +
                        "and has a constructor with only one parameter of a type extending %s",
                getWidgetName(classOfAWidget), WebElement.class.getName()));
    }

    private R createWidget(WebElement webElement) {
        Enhancer enhancer = new Enhancer();
        WidgetInterceptor interceptor = new WidgetInterceptor(webElement, classOfAWidget, conditionString);

        enhancer.setUseCache(false);
        enhancer.setCallbackType(WidgetInterceptor.class);
        enhancer.setSuperclass(classOfAWidget);
        Class<?> proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(classOfAWidget.getClassLoader());

        Objenesis objenesis = new ObjenesisStd();
        Object proxy = objenesis.newInstance(proxyClass);
        return (R) proxy;
    }

    @Override
    public List<R> apply(SearchContext searchContext) {
        List<Class<? extends R>> classesToInstantiate = getSubclasses();
        List<R> result = new ArrayList<>();
        classesToInstantiate.forEach(clazz -> {
            By by = builder.buildIt(clazz);
            result.addAll(searchContext.findElements(by).stream()
                    .map(this::createWidget).collect(toList()));
        });
        return result;
    }
}
