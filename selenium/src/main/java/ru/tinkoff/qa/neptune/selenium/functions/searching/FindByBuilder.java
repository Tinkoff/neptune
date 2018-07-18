package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.support.AbstractFindByBuilder;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

class FindByBuilder extends AbstractFindByBuilder {

    static <T extends Annotation> T getAnnotation(Class<?> clazz,
                                                  Class<T> desiredAnnotation) {
        Class<?> superClass = clazz;
        while (!superClass.equals(Widget.class)) {
            T result = superClass.getAnnotation(desiredAnnotation);
            if (result != null) {
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

    private By buildIt(String descriptionOfAnnotatedElement, FindBy findBy, FindBys findBys, FindAll findAll) {

        if ((findBy != null && findBys != null) || (findBy != null && findAll != null)
                || (findAll != null && findBys != null)) {
            throw new IllegalArgumentException(format("%s should be annotated by only %s, %s or %s",
                    descriptionOfAnnotatedElement,
                    FindBy.class.getName(),
                    FindBys.class.getName(),
                    FindAll.class.getName()));
        }

        return ofNullable(ofNullable(getSimpleBy(findBy))
                .orElseGet(() -> getChainedBy(findBys))).orElseGet(() -> getByAll(findAll));
    }

    By buildIt(Class<?> clazz) {
        return buildIt(format("Class %s or super-classes", clazz.getName()),
                getAnnotation(clazz, FindBy.class),
                getAnnotation(clazz, FindBys.class),
                getAnnotation(clazz, FindAll.class));
    }

    @Override
    public By buildIt(Object ignored, Field field) {
        return buildIt(format("Field %s declared by %s",
                field.getName(),
                field.getDeclaringClass().getName()),
                field.getAnnotation(FindBy.class),
                field.getAnnotation(FindBys.class),
                field.getAnnotation(FindAll.class));
    }
}
