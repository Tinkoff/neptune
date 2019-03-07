package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.*;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CGLibProxyBuilder.createProxy;
import static ru.tinkoff.qa.neptune.selenium.api.widget.Widget.getWidgetName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindByBuilder.getAnnotations;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.WidgetPriorityComparator.widgetPriorityComparator;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

class FindWidgets<R extends Widget> implements Function<SearchContext, List<R>> {

    private static final FindByBuilder builder = new FindByBuilder();
    private static final Reflections reflections = new Reflections("");

    final Class<? extends R> classOfAWidget;
    private final Predicate<Class<? extends R>> classPredicate;
    private List<Class<? extends R>> classesToInstantiate;
    private Supplier<String> criteriaDescription;
    private String labels;

    FindWidgets(Class<R> classOfAWidget, Predicate<Class<? extends R>> classPredicate) {
        checkArgument(nonNull(classOfAWidget), "The class to be instantiated should be defined.");
        this.classOfAWidget = classOfAWidget;
        this.classPredicate = classPredicate;
    }

    private FindWidgets(Class<R> classOfAWidget) {
        this(classOfAWidget, clazz -> !Modifier.isAbstract(clazz.getModifiers())

                && nonNull(getAnnotations(clazz))

                && (Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> {
                    var parameters = constructor.getParameterTypes();
                    return parameters.length == 1 &&
                            WebElement.class.isAssignableFrom(parameters[0]);
                }).collect(toList()).size() > 0));
    }



    static <R extends Widget> FindWidgets<R> widgets(Class<R> classOfAWidget) {
        return new FindWidgets<>(classOfAWidget);
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
                var stringDescription = String.format("%s elements of type %s", size(), getWidgetName(classOfAWidget));
                var criteria = ofNullable(criteriaDescription)
                        .map(Supplier::get)
                        .orElse(EMPTY);

                if (!isBlank(criteria)) {
                    stringDescription = format("%s and meet criteria ['%s']", stringDescription, criteria);
                }
                return stringDescription;
            }
        };

        classesToInstantiate.forEach(clazz -> {
            var by = builder.buildIt(clazz);
            result.addAll(searchContext.findElements(by).stream()
                    .map(webElement -> {
                        var stringDescription = getWidgetName(clazz);
                        var criteria = ofNullable(criteriaDescription)
                                .map(Supplier::get)
                                .orElse(EMPTY);

                        if (!isBlank(labels)) {
                            stringDescription = format("%s '%s'", stringDescription, labels);
                        }

                        if (!isBlank(criteria)) {
                            stringDescription = format("%s ['%s']", stringDescription, criteria);
                        }
                        return createProxy(clazz, new WidgetInterceptor(webElement, clazz, stringDescription));
                    })
                    .collect(toList()));
        });
        return result;
    }

    void setCriteriaDescription(Supplier<String> criteriaDescription) {
        checkNotNull(criteriaDescription);
        this.criteriaDescription = criteriaDescription;
    }

    void setLabels(String labels) {
        this.labels = labels;
    }
}
