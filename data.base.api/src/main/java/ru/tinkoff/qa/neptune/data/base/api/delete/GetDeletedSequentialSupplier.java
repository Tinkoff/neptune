package ru.tinkoff.qa.neptune.data.base.api.delete;

import ru.tinkoff.qa.neptune.data.base.api.DBSequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;
import ru.tinkoff.qa.neptune.data.base.api.PersistableList;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

/**
 * This class is designed to perform delete operations. A built function performs an operation and returns
 * deleted persistable objects.
 *
 * @param <T> is a type of objects to be deleted.
 */
@SuppressWarnings("unchecked")
public final class GetDeletedSequentialSupplier<T extends PersistableObject>
        extends DBSequentialGetStepSupplier<List<T>, DataBaseSteps, GetDeletedSequentialSupplier<T>> {

    private final List<T> toBeDeleted;

    private GetDeletedSequentialSupplier(List<T> toBeDeleted) {
        checkArgument(toBeDeleted != null, "Objects to be deleted should not be defined as a null-value");
        checkArgument(toBeDeleted.size() > 0, "Should be defined at least one object to delete");
        this.toBeDeleted = toBeDeleted;
    }

    /**
     * Creates a supplier of a function that performs a delete operation and returns deleted persistable
     * objects.
     *
     * @param toBeDeleted is an array of objects to be deleted
     * @param <T> is a type of objects to be deleted
     * @return a new instance of {@link GetDeletedSequentialSupplier}
     */
    @SafeVarargs
    public static <T extends PersistableObject> GetDeletedSequentialSupplier<T> deleted(T... toBeDeleted) {
        return deleted(asList(toBeDeleted));
    }

    /**
     * Creates a supplier of a function that performs a delete operation and returns deleted persistable
     * objects.
     *
     * @param toBeDeleted is a collection of objects to be deleted
     * @param <T> is a type of objects to be deleted
     * @return a new instance of {@link GetDeletedSequentialSupplier}
     */
    public static <T extends PersistableObject> GetDeletedSequentialSupplier<T> deleted(List<T> toBeDeleted) {
        return new GetDeletedSequentialSupplier<>(toBeDeleted);
    }

    @Override
    public Function<DataBaseSteps, List<T>> get() {
        super.from(dataBaseSteps -> dataBaseSteps);
        return super.get();
    }

    @Override
    protected Function<DataBaseSteps, List<T>> getEndFunction() {
        String description = format("Delete objects: \n %s", toBeDeleted.toString());
        return toGet(description, dataBaseSteps -> {
            dataBaseSteps.getCurrentPersistenceManager().deletePersistentAll(toBeDeleted);
            PersistableList<T> result = new PersistableList<>();
            toBeDeleted.forEach(o -> result.add((T) o.clone()));
            return result;
        });
    }
}
