package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.openqa.selenium.*;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindByBuilder.getAnnotations;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.WidgetPriorityComparator.widgetPriorityComparator;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

@SuppressWarnings("unchecked")
class FindWidgets<R extends Widget> implements Function<SearchContext, List<R>> {

    private static final FindByBuilder builder = new FindByBuilder();
    private static final Reflections reflections = new Reflections("");

    final Class<? extends R> classOfAWidget;
    private final Predicate<Class<? extends R>> classPredicate;
    private final String conditionString;
    private List<Class<? extends R>> classesToInstantiate;

    FindWidgets(Class<R> classOfAWidget, String conditionString, Predicate<Class<? extends R>> classPredicate) {
        checkArgument(classOfAWidget != null, "The class to be instantiated should be defined.");
        checkArgument(conditionString != null, "Description of the conditions should be defined.");
        this.classOfAWidget = classOfAWidget;
        this.conditionString = conditionString;
        this.classPredicate = classPredicate;
    }

    private FindWidgets(Class<R> classOfAWidget, String conditionString) {
        this(classOfAWidget, conditionString, clazz -> !Modifier.isAbstract(clazz.getModifiers())

                && (getAnnotations(clazz) != null)

                && (Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> {
                    Class<?>[] parameters = constructor.getParameterTypes();
                    return parameters.length == 1 &&
                            WebElement.class.isAssignableFrom(parameters[0]);
                }).collect(toList()).size() > 0));
    }



    static <R extends Widget> Function<SearchContext, List<R>> widgets(Class<R> classOfAWidget,
                                                                       String conditionString) {
        return new FindWidgets<>(classOfAWidget, conditionString);
    }

    private static <R extends Widget> List<Class<? extends R>> findSubclasses(Class<? extends R> classOfAWidget,
                                                                              Predicate<Class<? extends R>> classPredicate) {
        return reflections.getSubTypesOf(classOfAWidget).stream()
                .filter(classPredicate)
                .sorted(widgetPriorityComparator()).collect(toList());
    }

    List<Class<? extends R>> findSubclasses() {
        List<Class<? extends R>> resultList = findSubclasses(classOfAWidget, classPredicate);

        if (classPredicate.test(classOfAWidget)) {
            resultList.add(classOfAWidget);
        }

        return resultList;
    }

    List<Class<? extends R>> getSubclasses() {
        List<Class<? extends R>> resultList = findSubclasses();

        if (resultList.size() > 0) {
            return resultList;
        }
        throw new IllegalArgumentException(String.format("There is no any non-abstract subclass of %s which " +
                        "is annotated by any org.openqa.selenium.support.Find* annotation " +
                        "and has a constructor with only one parameter of a type extending %s",
                Widget.getWidgetName(classOfAWidget), WebElement.class.getName()));
    }

    private <T> T createProxy(Class<T> tClass, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();

        enhancer.setUseCache(false);
        enhancer.setCallbackType(interceptor.getClass());
        enhancer.setSuperclass(tClass);
        Class<?> proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(tClass.getClassLoader());

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
