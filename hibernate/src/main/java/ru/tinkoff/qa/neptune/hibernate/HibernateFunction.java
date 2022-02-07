package ru.tinkoff.qa.neptune.hibernate;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class HibernateFunction<INPUT, RESULT> implements Function<HibernateContext, RESULT> {

    @StepParameter("Entity class")
    protected Class<INPUT> entity;

    protected HibernateFunction(Class<INPUT> entity) {
        checkNotNull(entity);
        this.entity = entity;
    }
}
