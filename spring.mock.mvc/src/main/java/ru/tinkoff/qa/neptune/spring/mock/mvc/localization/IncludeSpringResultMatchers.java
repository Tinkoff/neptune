package ru.tinkoff.qa.neptune.spring.mock.mvc.localization;

import org.springframework.test.web.servlet.result.*;
import ru.tinkoff.qa.neptune.core.api.localization.ToIncludeClassDescription;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

class IncludeSpringResultMatchers implements ToIncludeClassDescription {

    static final List<Class<?>> CLASSES = prepareClasses();

    private static synchronized List<Class<?>> prepareClasses() {
        var result = new ArrayList<Class<?>>();
        result.add(RequestResultMatchers.class);
        result.add(HandlerResultMatchers.class);
        result.add(ModelResultMatchers.class);
        result.add(ViewResultMatchers.class);
        result.add(FlashAttributeResultMatchers.class);
        result.add(StatusResultMatchers.class);
        result.add(HeaderResultMatchers.class);
        result.add(ContentResultMatchers.class);
        result.add(JsonPathResultMatchers.class);
        result.add(XpathResultMatchers.class);
        result.add(CookieResultMatchers.class);
        return result.stream().sorted(comparing(Class::getName)).collect(toList());
    }

    @Override
    public boolean toIncludeClass(Class<?> clazz) {
        if (clazz.equals(XpathResultMatchers.class)
                || clazz.equals(JsonPathResultMatchers.class)) {
            return false;
        }
        return CLASSES.contains(clazz);
    }

    @Override
    public String description(Class<?> clazz) {
        return EMPTY;
    }
}
