package ru.tinkoff.qa.neptune.selenium.properties;

import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import ru.tinkoff.qa.neptune.selenium.auto.scrolling.AutoScroller;

import static java.lang.Class.forName;
import static java.lang.String.format;
import static java.util.Arrays.stream;

@PropertyDescription(description = {"Defines a class that extends ru.tinkoff.qa.neptune.selenium.auto.scrolling.AutoScroller.",
        "Instances of this class are used to perform the scrolling into view implicitly",
        "on interaction with elements of a page"},
        section = "Selenium. Web driver scrolling/focusing")
@PropertyName("WEB_DRIVER_DEFAULT_AUTO_SCROLLER")
public final class DefaultScrollerProperty implements PropertySupplier<Class<? extends AutoScroller>> {


    public static final DefaultScrollerProperty DEFAULT_SCROLLER_PROPERTY = new DefaultScrollerProperty();

    private DefaultScrollerProperty() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends AutoScroller> parse(String value) {
        try {
            var clz = forName(value);
            if (!AutoScroller.class.isAssignableFrom(clz)) {
                throw new UnsupportedOperationException(format("%s does not extend %s", clz, AutoScroller.class.getName()));
            }

            stream(clz.getDeclaredConstructors())
                    .filter(constructor -> {
                        var p = constructor.getParameterTypes();
                        return p.length == 1 && WebDriver.class.equals(p[0]);
                    }).findFirst()
                    .orElseThrow(() -> new UnsupportedOperationException(format("%s should have a constructor " +
                                    "with only one parameter of type %s",
                            clz.getName(),
                            WebDriver.class.getName())));

            return (Class<? extends AutoScroller>) clz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
