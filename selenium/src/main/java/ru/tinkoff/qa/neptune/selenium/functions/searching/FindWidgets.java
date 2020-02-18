package ru.tinkoff.qa.neptune.selenium.functions.searching;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.api.widget.Widget.getWidgetName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CGLibProxyBuilder.createProxy;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindByBuilder.getAnnotations;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.WidgetPriorityComparator.widgetPriorityComparator;

class FindWidgets<R extends Widget> implements Function<SearchContext, List<R>> {

    private static final FindByBuilder BUILDER = new FindByBuilder();
    private static final ScanResult SCAN_RESULT = new ClassGraph()
            .enableAllInfo()
            .scan();

    final Class<? extends R> classOfAWidget;
    private final Predicate<Class<? extends R>> classPredicate;
    private List<Class<? extends R>> classesToInstantiate;

    FindWidgets(Class<R> classOfAWidget, Predicate<Class<? extends R>> classPredicate) {
        checkArgument(nonNull(classOfAWidget), "The class to be instantiated should be defined.");
        this.classOfAWidget = classOfAWidget;
        this.classPredicate = classPredicate;
    }

    private FindWidgets(Class<R> classOfAWidget) {
        this(classOfAWidget, clazz -> !Modifier.isAbstract(clazz.getModifiers())
                && nonNull(getAnnotations(clazz))
                && (stream(clazz.getDeclaredConstructors())
                .anyMatch(constructor -> {
                    var parameters = constructor.getParameterTypes();
                    return parameters.length == 1 &&
                            WebElement.class.isAssignableFrom(parameters[0]);
                })));
    }


    static <R extends Widget> FindWidgets<R> widgets(Class<R> classOfAWidget) {
        return new FindWidgets<>(classOfAWidget);
    }

    private static <R extends Widget> List<Class<? extends R>> findSubclasses(Class<? extends R> classOfAWidget,
                                                                              Predicate<Class<? extends R>> classPredicate) {
        return SCAN_RESULT.getSubclasses(classOfAWidget.getName())
                .loadClasses(classOfAWidget)
                .stream()
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
        var resultList = findSubclasses();

        if (resultList.size() > 0) {
            return resultList;
        }
        throw new IllegalArgumentException(String.format("There is no any non-abstract subclass of %s that " +
                        "is annotated by any org.openqa.selenium.support.Find* annotation " +
                        "and has a constructor with only one parameter of a type extending %s",
                Widget.getWidgetName(classOfAWidget), WebElement.class.getName()));
    }

    @Override
    public List<R> apply(SearchContext searchContext) {
        classesToInstantiate = ofNullable(classesToInstantiate)
                .orElseGet(this::getSubclasses);

        var result = new LoggableElementList<R>() {
            @Override
            public String toString() {
                return String.format("%s elements of type(s) %s", size(),
                        stream().map(r -> getWidgetName(r.getClass()))
                                .distinct()
                                .collect(joining(", ")));
            }
        };

        classesToInstantiate.forEach(clazz -> {
            var by = BUILDER.buildIt(clazz);
            result.addAll(searchContext.findElements(by).stream()
                    .map(webElement -> createProxy(clazz, new WidgetInterceptor(webElement, clazz)))
                    .collect(toList()));
        });
        return result;
    }
}
