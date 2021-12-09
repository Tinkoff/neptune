package ru.tinkoff.qa.neptune.selenium.localization;

import io.github.classgraph.ClassGraph;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.localization.BindToPartition;
import ru.tinkoff.qa.neptune.core.api.localization.BundleFillerExtension;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.NameMultiple;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionMetadataFactory.getMultipleNameMetadata;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionMetadataFactory.getNameMetadata;

@SuppressWarnings("unused")
@BindToPartition("selenium")
public final class WebElementBundleFillerExtension extends BundleFillerExtension {

    public WebElementBundleFillerExtension() {
        super(prepareClasses(), "WEB ELEMENTS");
    }

    private static List<Class<?>> prepareClasses() {
        var result = new ClassGraph()
                .enableClassInfo()
                .ignoreClassVisibility()
                .scan()
                .getSubclasses(Widget.class.getName())
                .loadClasses(Widget.class)
                .stream()
                .filter(widgetClass -> !widgetClass.equals(Widget.class)
                        && (widgetClass.getAnnotation(Name.class) != null
                        || widgetClass.getAnnotation(NameMultiple.class) != null))
                .map(cls -> (Class<?>) cls)
                .sorted(comparing(Class::getName))
                .collect(toCollection((Supplier<LinkedList<Class<?>>>) LinkedList::new));

        result.addFirst(Widget.class);
        result.addFirst(WebElement.class);
        return result;
    }

    @Override
    protected List<AnnotatedElement> addFields(Class<?> clazz) {
        var fields = new ArrayList<AnnotatedElement>();

        ofNullable(getNameMetadata(clazz)).ifPresent(fields::add);
        ofNullable(getMultipleNameMetadata(clazz)).ifPresent(fields::add);
        return fields;
    }

    @Override
    protected List<Method> addMethods(Class<?> clazz) {
        return of();
    }
}
