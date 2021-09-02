package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.AdditionalMetadata;

import java.lang.annotation.Annotation;

public final class WidgetDescriptionMetadataFactory {

    private WidgetDescriptionMetadataFactory() {
        super();
    }

    private static Name nameAnnotation(String name) {
        return new Name() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Name.class;
            }

            @Override
            public String value() {
                return name;
            }
        };
    }

    private static NameMultiple multipleNameAnnotation(String name) {
        return new NameMultiple() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return NameMultiple.class;
            }

            @Override
            public String value() {
                return name;
            }
        };
    }

    public static AdditionalMetadata<Name> getNameMetadata(Class<?> cls) {
        if (WebElement.class.isAssignableFrom(cls)) {
            return new AdditionalMetadata<>(WebElement.class, "name", Name.class, () -> nameAnnotation("Page element"));
        }

        var clz = cls;
        while (!clz.equals(Object.class)) {
            var name = clz.getAnnotation(Name.class);
            if (name != null) {
                return new AdditionalMetadata<>(clz, "name", Name.class, () -> nameAnnotation(name.value()));
            }
            clz = clz.getSuperclass();
        }

        return null;
    }

    public static AdditionalMetadata<NameMultiple> getMultipleNameMetadata(Class<?> cls) {
        if (WebElement.class.isAssignableFrom(cls)) {
            return new AdditionalMetadata<>(WebElement.class, "multipleName", NameMultiple.class, () -> multipleNameAnnotation("Page elements"));
        }

        var clz = cls;
        while (!clz.equals(Object.class)) {
            var name = clz.getAnnotation(NameMultiple.class);
            if (name != null) {
                return new AdditionalMetadata<>(clz, "multipleName", NameMultiple.class, () -> multipleNameAnnotation(name.value()));
            }
            clz = clz.getSuperclass();
        }

        return null;
    }

    /*private static String getText(String elementText, String name) {
        String text;
        if (isBlank(elementText)) {
            text = EMPTY;
        } else if (elementText.length() < 15) {
            text = elementText;
        } else {
            text = format("%s...", elementText.substring(0, 15));
        }

        if (isBlank(text)) {
            return name;
        }
        return format("%s [%s]", name, text);
    }

    private static String getText(Object o, String name) {
        if (o instanceof WebElement) {
            try {
                return getText(((WebElement) o).getText().trim(), name + o);
            } catch (Exception e) {
                return name + o;
            }
        }

        if (o instanceof Widget) {
            try {
                return getText(((Widget) o).getText().trim(), name);
            } catch (Throwable t) {
                return name;
            }
        }

        return name;
    }

    public static String getDescription(Object o) {
        var name = translate(getNameMetadata(o.getClass()));
        return getText(o, name);
    }*/
}
