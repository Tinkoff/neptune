package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.support.*;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

class FindByBuilder extends AbstractFindByBuilder {

    private static final Predicate<Annotation> FILTER_FIND_BY =
            annotation -> annotation.annotationType().equals(FindBy.class);
    private static final Predicate<Annotation> FILTER_FIND_BYS =
            annotation -> annotation.annotationType().equals(FindBys.class);
    private static final Predicate<Annotation> FILTER_FIND_ALL =
            annotation -> annotation.annotationType().equals(FindAll.class);

    static Annotation[] getAnnotations(Class<?> clazz) {
        Class<?> superClass = clazz;
        while (!superClass.equals(Widget.class)) {
            Annotation[] result = superClass.getDeclaredAnnotations();
            if (result != null && result.length > 0 &&
                    stream(result).anyMatch(FILTER_FIND_BY.or(FILTER_FIND_BYS).or(FILTER_FIND_ALL))) {
                return result;
            }
            superClass = superClass.getSuperclass();
        }
        return null;
    }

    private By getSimpleBy(FindBy findBy) {
        return ofNullable(findBy).map(findSimple -> {
            assertValidFindBy(findSimple);
            return buildByFromFindBy(findSimple);
        }).orElse(null);
    }

    private By getChainedBy(FindBys findBys) {
        return ofNullable(findBys)
                .map(findByChained -> {
                    assertValidFindBys(findByChained);

                    FindBy[] findByArray = findByChained.value();
                    By[] byArray = new By[findByArray.length];
                    for (int i = 0; i < findByArray.length; i++) {
                        byArray[i] = buildByFromFindBy(findByArray[i]);
                    }

                    return new ByChained(byArray);
                }).orElse(null);
    }

    private By getByAll(FindAll findAll) {
        return ofNullable(findAll).map(findByAll -> {
            assertValidFindAll(findByAll);
            FindBy[] findByArray = findByAll.value();
            By[] byArray = new By[findByArray.length];
            for (int i = 0; i < findByArray.length; i++) {
                byArray[i] = buildByFromFindBy(findByArray[i]);
            }

            return new ByAll(byArray);
        }).orElse(null);
    }

    private By buildIt(String descriptionOfAnnotatedElement, Annotation[] annotations) {
        return ofNullable(annotations).map(annotations1 -> {
            boolean hasFindBy = stream(annotations1).anyMatch(FILTER_FIND_BY);
            boolean hasFindBys = stream(annotations1).anyMatch(FILTER_FIND_BYS);
            boolean hasFindAll = stream(annotations1).anyMatch(FILTER_FIND_ALL);

            if ((hasFindBy && hasFindBys) || (hasFindBy && hasFindAll)
                    || (hasFindAll && hasFindBys)) {
                throw new IllegalArgumentException(format("%s should be annotated by only %s, %s or %s",
                        descriptionOfAnnotatedElement,
                        FindBy.class.getName(),
                        FindBys.class.getName(),
                        FindAll.class.getName()));
            }

            if (hasFindBy) {
                return getSimpleBy((FindBy) stream(annotations1).filter(FILTER_FIND_BY).findFirst().orElse(null));
            }

            if (hasFindBys) {
                return getChainedBy((FindBys) stream(annotations1).filter(FILTER_FIND_BYS).findFirst().orElse(null));
            }

            if (hasFindAll) {
                return getByAll((FindAll) stream(annotations1).filter(FILTER_FIND_ALL).findFirst().orElse(null));
            }

            return null;
        }).orElse(null);
    }

    By buildIt(Class<?> clazz) {
        return buildIt(format("Class %s or super-classes", clazz.getName()),
                getAnnotations(clazz));
    }

    @Override
    public By buildIt(Object ignored, Field field) {
        return ofNullable(buildIt(format("Field %s declared by %s",
                field.getName(),
                field.getDeclaringClass().getName()),
                field.getDeclaredAnnotations()))

                .orElseGet(() -> new ByIdOrName(field.getName()));
    }
}
