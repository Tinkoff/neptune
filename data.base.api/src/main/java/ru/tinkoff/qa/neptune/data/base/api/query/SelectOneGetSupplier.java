package ru.tinkoff.qa.neptune.data.base.api.query;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.ConditionConcatenation;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.NothingIsSelectedException;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.JDOQLTypedQuery;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.SLEEPING_TIME;
import static ru.tinkoff.qa.neptune.data.base.api.properties.WaitingForQueryResultDuration.WAITING_FOR_SELECTION_RESULT_TIME;

@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
@Deprecated
public class SelectOneGetSupplier<T, M> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<JDOPersistenceManager, T, M, SelectOneGetSupplier<T, M>> {

    private  <S extends Iterable<T>> SelectOneGetSupplier(String description, Function<M, S> originalFunction) {
        super(description, originalFunction);
        timeOut(WAITING_FOR_SELECTION_RESULT_TIME.get());
        pollingInterval(SLEEPING_TIME.get());
    }

    @Override
    public SelectOneGetSupplier<T, M> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SelectOneGetSupplier<T, M> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    public SelectOneGetSupplier<T, M> criteria(ConditionConcatenation concat, Predicate<? super T> condition) {
        return super.criteria(concat, condition);
    }

    @Override
    public SelectOneGetSupplier<T, M> criteria(ConditionConcatenation concat, String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(concat, conditionDescription, condition);
    }

    @Override
    public SelectOneGetSupplier<T, M> criteria(Predicate<? super T> condition) {
        return super.criteria(condition);
    }

    @Override
    public SelectOneGetSupplier<T, M> criteria(String conditionDescription, Predicate<? super T> condition) {
        return super.criteria(conditionDescription, condition);
    }

    /**
     * This methods says that an exception should be thrown when query returns an empty result.
     *
     * @param selectedExceptionSupplier is a suppler of exception to be thrown.
     * @return self-reference.
     */
    public SelectOneGetSupplier<T, M>  throwWhenResultEmpty(Supplier<NothingIsSelectedException> selectedExceptionSupplier) {
        return super.throwOnEmptyResult(selectedExceptionSupplier);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base by id and returns a single element.
     *
     * @param ofType is a class of object to be found.
     * @param id of an object to be found.
     * @param <T> is a type of result element.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectOneGetSupplier<T, JDOPersistenceManager> aSingleOfTypeById(Class<T> ofType, Object id) {
        return new SelectOneGetSupplier<>(
                format("A single stored database object. Type %s. By id %s", ofType.getName(), id),
                new SelectListByIds<>(new Object[] {id}, ofType))
                .from(jdoPersistenceManager -> jdoPersistenceManager);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns a single element.
     *
     * @param queryBuilder is a builder of a query.
     * @param <T> is a type of result element.
     * @return created supplier of a function.
     */
    public static <T extends PersistableObject> SelectOneGetSupplier<T, JDOQLTypedQuery<T>> aSingleByQuery(QueryBuilderFunction<T> queryBuilder) {
        var className = queryBuilder.getTypeOfItemToSelect().getName();
        return new SelectOneGetSupplier<>(format("A single stored database object. Type %s", className),
                new SelectByTypedQuery<T>()).from(queryBuilder);
    }

    /**
     * Creates a supplier of a function that performs selection from a data base and returns a single element.
     *
     * @param queryBuilder is a builder of SELECT-SQL query.
     * @param <T> is a type of result element.
     * @return created supplier of a function.
     */
    public static <T> SelectOneGetSupplier<T, SQLQuery<T>> aSingleByQuery(SQLQueryBuilderFunction<T> queryBuilder) {
        var description = ofNullable(queryBuilder.type)
                .map(aClass -> format("A single stored database object. Type %s", aClass.getName()))
                .orElse("A single record from the database by SQL query");

        return new SelectOneGetSupplier<>(description,
                new SelectBySqlQuery<T>()).from(queryBuilder);
    }
}
