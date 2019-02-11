package ru.tinkoff.qa.neptune.data.base.api.query;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.ConditionConcatenation;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.WAITING_FOR_SELECTION_RESULT_TIME;

@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
public final class SelectListGetSupplier<T, M> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<JDOPersistenceManager, List<T>, M, T, SelectListGetSupplier<T, M>> {

    private SelectListGetSupplier(String description, Function<M, List<T>> originalFunction) {
        super(description, originalFunction);
        timeOut(WAITING_FOR_SELECTION_RESULT_TIME.get(), SLEEPING_TIME.get());
    }

    @Override
    public SelectListGetSupplier<T, M> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectListGetSupplier<T, M> timeOut(Duration timeOut, Duration sleepingTime) {
        return super.timeOut(timeOut, sleepingTime);
    }

    @Override
    public SelectListGetSupplier<T, M> criteria(ConditionConcatenation concat, Predicate<? super T> condition) {
        return super.criteria(concat, condition);
    }

    @Override
    public SelectListGetSupplier<T, M> criteria(ConditionConcatenation concat, String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(concat, conditionDescription, condition);
    }

    @Override
    public SelectListGetSupplier<T, M> criteria(Predicate<? super T> condition) {
        return super.criteria(condition);
    }

    @Override
    public SelectListGetSupplier<T, M> criteria(String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(conditionDescription, condition);
    }

    /**
     * This methods says that an exception should be thrown when query returns an empty result.
     *
     * @param selectedExceptionSupplier is a suppler of exception to be thrown.
     * @return self-reference.
     */
    public SelectListGetSupplier<T, M> throwWhenResultEmpty(Supplier<NothingIsSelectedException> selectedExceptionSupplier) {
        return super.throwOnEmptyResult(selectedExceptionSupplier);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base by ids and returns the result as a list.
     *
     * @param ofType is a class of objects to be found.
     * @param ids of objects to be found.
     * @param <T> is a type of an item from the result list.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectListGetSupplier<T, JDOPersistenceManager> listOfTypeByIds(Class<T> ofType, Object... ids) {
        return new SelectListGetSupplier<>(
                format("List of stored database objects. Type %s. By ids: %s", ofType.getName(), Arrays.toString(ids)),
                new SelectListByIds<>(ids, ofType))
                .from(jdoPersistenceManager -> jdoPersistenceManager);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns the result as a list.
     *
     * @param queryBuilder is a builder of a typed query.
     * @param <T> is a type of an item from the result list.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectListGetSupplier<T, JDOQLTypedQuery<T>> listByQuery(QueryBuilderFunction<T> queryBuilder) {
        var className = queryBuilder.getTypeOfItemToSelect().getName();
        return new SelectListGetSupplier<>(format("List of stored database objects. Type %s", className),
                new SelectByTypedQuery<T>()).from(queryBuilder);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns the result as a list.
     *
     * @param queryBuilder is a builder of SELECT-SQL query.
     * @param <T>          is a type of an item from the result list.
     * @return created supplier of a function.
     */
    public static <T> SelectListGetSupplier<T, SQLQuery<T>> listByQuery(SQLQueryBuilderFunction<T> queryBuilder) {
        var description = ofNullable(queryBuilder.type)
                .map(aClass -> format("List of stored database objects. Type %s", aClass.getName()))
                .orElse("List of records from the database by SQL query");

        return new SelectListGetSupplier<>(description,
                new SelectBySqlQuery<T>()).from(queryBuilder);
    }
}
