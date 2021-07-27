package ru.tinkoff.qa.neptune.selenium.functions.searching;

import io.github.classgraph.ClassGraph;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CGLibProxyBuilder.createProxy;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindByBuilder.getAnnotations;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.ToStringFormer.getMultipleToString;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.WidgetPriorityComparator.widgetPriorityComparator;

class FindWidgets<R extends Widget> implements Function<SearchContext, List<R>> {

    private static final FindByBuilder BUILDER = new FindByBuilder();
    private static final List<Class<? extends Widget>> SCAN_RESULT = new ArrayList<>(new ClassGraph()
            .enableAllInfo()
            .scan()
            .getSubclasses(Widget.class.getName())
            .loadClasses(Widget.class));

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

    @SuppressWarnings("unchecked")
    private static <R extends Widget> List<Class<? extends R>> findSubclasses(Class<? extends R> classOfAWidget,
                                                                              Predicate<Class<? extends R>> classPredicate) {
        return SCAN_RESULT
                .stream()
                .filter(classOfAWidget::isAssignableFrom)
                .map(cls -> (Class<? extends R>) cls)
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
                classOfAWidget.getName(), WebElement.class.getName()));
    }

    @Override
    public List<R> apply(SearchContext searchContext) {
        classesToInstantiate = ofNullable(classesToInstantiate)
                .orElseGet(this::getSubclasses);

        var result = new ArrayList<R>() {
            @Override
            public String toString() {
                if (this.size() == 0) {
                    return "<...>";
                }

                return getMultipleToString(this);
            }
        };

        classesToInstantiate.forEach(clazz -> {
            var by = BUILDER.buildIt(clazz);
            result.addAll(searchContext.findElements(by).stream()
                    .map(webElement -> createProxy(clazz, new WidgetInterceptor(webElement, clazz, by)))
                    .collect(toList()));
        });
        return result;
    }
}
