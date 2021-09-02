package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.ResultPersistentManager;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;

import java.util.ArrayList;

/**
 * This class is designed to builds step-functions that perform the deleting and return a result
 *
 * @param <T> is a type of objects to be deleted
 * @param <R> is a type of subclass of {@link DeleteOperation}
 */
@SuppressWarnings("unchecked")
public abstract class DeleteOperation<T extends PersistableObject, M, R extends DeleteOperation<T, M, R>> extends DataOperation<T, M, R> {

    DeleteOperation(PersistenceMapWrapper<T> mapWrapper) {
        super(m -> {
            var jdoPersistenceManagerListMap = mapWrapper.get();
            var managerSet = jdoPersistenceManagerListMap.keySet();
            openTransaction(managerSet);

            try {
                var result = new ArrayList<T>();
                jdoPersistenceManagerListMap.forEach((manager, ts) -> {
                    manager.deletePersistentAll(ts);
                    ts.forEach(o -> result.add((T) o.clone()));
                });

                if (managerSet.size() > 1) {
                    preCommit(managerSet);
                }
                commitTransaction(managerSet);
                return result;
            } catch (Throwable t) {
                rollbackTransaction(managerSet);
                throw t;
            }
        }, mapWrapper);
    }

    static final class DeleteSelected<T extends PersistableObject, M> extends DeleteOperation<T, M, DeleteOperation.DeleteSelected<T, M>> {

        @StepParameter("To be deleted")
        final M toDelete;

        DeleteSelected(PersistenceMapWrapper<T> mapWrapper, M toDelete) {
            super(mapWrapper);
            from(toDelete);
            this.toDelete = toDelete;
        }
    }

    @IncludeParamsOfInnerGetterStep
    static final class DeleteBySelection<T extends PersistableObject, M>
            extends DeleteOperation<T, M, DeleteOperation.DeleteBySelection<T, M>> {

        private static final ResultPersistentManager RESULT_PERSISTENT_MANAGER = new ResultPersistentManager() {
        };

        DeleteBySelection(PersistenceMapWrapper<T> mapWrapper,
                          SequentialGetStepSupplier<DataBaseStepContext, M, ?, ?, ?> howToSelect) {
            super(mapWrapper);
            from(howToSelect);

            if (howToSelect instanceof SelectList) {
                RESULT_PERSISTENT_MANAGER.keepResultPersistent((SelectList<?, ?>) howToSelect);
            }

            if (howToSelect instanceof SelectASingle) {
                RESULT_PERSISTENT_MANAGER.keepResultPersistent((SelectASingle<?>) howToSelect);
            }
        }
    }
}
