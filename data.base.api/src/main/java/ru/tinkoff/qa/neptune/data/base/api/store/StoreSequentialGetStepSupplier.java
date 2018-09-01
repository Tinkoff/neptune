package ru.tinkoff.qa.neptune.data.base.api.store;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.DBSequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseSteps;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.PersistableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.jdo.JDOHelper.isDeleted;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

/**
 * This class is designed to perform insert/update operations. A built function performs an operation and returns
 * created/updated persistable objects.
 *
 * @param <T> is a type of objects to be created/updated.
 */
@SuppressWarnings("unchecked")
public final class StoreSequentialGetStepSupplier<T extends PersistableObject>
        extends DBSequentialGetStepSupplier<List<T>, DataBaseSteps, StoreSequentialGetStepSupplier<T>> {

    private final Collection<T> toBePersisted;

    private StoreSequentialGetStepSupplier(Collection<T> toBePersisted) {
        checkArgument(toBePersisted != null, "Collection of persistable objects to be stored should not be null");
        checkArgument(toBePersisted.size() > 0, "Should be defined at least one persistable object to be stored");
        this.toBePersisted = toBePersisted;
    }

    /**
     * Creates a supplier of a function that performs an insert/update operation and returns created/updated persistable
     * objects.
     *
     * @param toBeStored is an array of objects to be saved/updated
     * @param <T> is a type of objects to be created/updated.
     * @return a new instance of {@link StoreSequentialGetStepSupplier}
     */
    @SafeVarargs
    public static <T extends PersistableObject> StoreSequentialGetStepSupplier<T> storedObjects(T... toBeStored) {
        return storedObjects(asList(toBeStored));
    }

    /**
     * Creates a supplier of a function that performs an insert/update operation and returns created/updated persistable
     * objects.
     *
     * @param toBeStored is a collection of objects to be saved/updated
     * @param <T> is a type of objects to be created/updated.
     * @return a new instance of {@link StoreSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> StoreSequentialGetStepSupplier<T> storedObjects(Collection<T> toBeStored) {
        return new StoreSequentialGetStepSupplier<>(toBeStored);
    }

    @Override
    protected Function<DataBaseSteps, List<T>> getEndFunction() {
        String description = format("Store objects: \n %s", toBePersisted.toString());
        return toGet(description, dataBaseSteps -> {
            JDOPersistenceManager manager = dataBaseSteps.getCurrentPersistenceManager();
            ArrayList<T> toBeStored = new ArrayList<>(toBePersisted);
            toBePersisted.forEach(o -> {
                if (isDeleted(o)) {
                    toBeStored.remove(o);
                    T clone = (T) o.clone();
                    toBeStored.add(clone);
                }
            });

            return new PersistableList<>(manager.makePersistentAll(toBeStored));
        });
    }

    @Override
    public Function<DataBaseSteps, List<T>> get() {
        super.from(dataBaseSteps -> dataBaseSteps);
        return super.get();
    }
}
