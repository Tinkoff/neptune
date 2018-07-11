package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Widget;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
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

import static com.github.toy.constructor.selenium.api.widget.Widget.getWidgetName;
import static com.github.toy.constructor.selenium.functions.searching.FindByBuilder.getAnnotation;
import static com.github.toy.constructor.selenium.functions.searching.WidgetPriorityComparator.widgetPriorityComparator;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

@SuppressWarnings("unchecked")
class FindWidgets<R extends Widget> implements Function<SearchContext, List<R>> {

    private static final FindByBuilder builder = new FindByBuilder();
    final Class<? extends R> classOfAWidget;
    private final String conditionString;
    private List<Class<? extends R>> classesToInstantiate;

    FindWidgets(Class<R> classOfAWidget, String conditionString) {
        checkArgument(classOfAWidget != null, "The class to be instantiated should be defined.");
        checkArgument(conditionString != null, "Description of the conditions should be defined.");
        this.classOfAWidget = classOfAWidget;
        this.conditionString = conditionString;
    }

    static <R extends Widget> Function<SearchContext, List<R>> widgets(Class<R> classOfAWidget,
                                                                       String conditionString) {
        return new FindWidgets<>(classOfAWidget, conditionString);
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
                .filter(classPredicate)
                .sorted(widgetPriorityComparator()).collect(toList());

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

    private <T> T createProxy(Class<T> tClass, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();

        enhancer.setUseCache(false);
        enhancer.setCallbackType(interceptor.getClass());
        enhancer.setSuperclass(tClass);
        Class<?> proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(tClass.getClass().getClassLoader());

        Objenesis objenesis = new ObjenesisStd();
        Object proxy = objenesis.newInstance(proxyClass);
        return (T) proxy;
    }

    @Override
    public List<R> apply(SearchContext searchContext) {
        classesToInstantiate = ofNullable(classesToInstantiate)
                .orElseGet(this::getSubclasses);
        List<R> result = new ArrayList<>();
        classesToInstantiate.forEach(clazz -> {
            By by = builder.buildIt(clazz);
            result.addAll(searchContext.findElements(by).stream()
                    .map(webElement -> createProxy(clazz, new WidgetInterceptor(webElement, clazz, conditionString)))
                    .collect(toList()));
        });
        return (List<R>) createProxy(result.getClass(),
                new WidgetListInterceptor(classOfAWidget, result, conditionString));
    }
}
