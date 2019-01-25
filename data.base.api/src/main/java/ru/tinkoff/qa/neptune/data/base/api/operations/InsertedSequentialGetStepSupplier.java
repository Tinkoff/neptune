package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.PersistenceManager;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isDeleted;
import static javax.jdo.JDOHelper.isPersistent;

/**
 * This is the class that builds and supplies functions to return results of INSERT operation.
 * Transactions are used to keep data of the data store valid when something is going wrong. INSERT
 * operations are supposed to return lists of inserted objects.
 *
 * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
 */
public class InsertedSequentialGetStepSupplier<T extends PersistableObject>
        extends OperationSequentialGetSupplier<T, InsertedSequentialGetStepSupplier<T>> {

    private static final String DESCRIPTION = "Inserted objects";

    private InsertedSequentialGetStepSupplier(Collection<T> toBeInserted) {
        super(getDescription(format("%s:", DESCRIPTION), toBeInserted), getObjectsToBeInserted(toBeInserted));
    }

    /**
     * Creates an instance of {@link InsertedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of INSERT operation.
     *
     * @param toBeInserted is a collection of objects to be inserted.
     *                     <p>NOTE!</p>
     *                     There should be objects that have not been stored or inserted yet
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link InsertedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> InsertedSequentialGetStepSupplier<T> inserted(Collection<T> toBeInserted) {
        return new InsertedSequentialGetStepSupplier<>(toBeInserted);
    }

    /**
     * Creates an instance of {@link InsertedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of INSERT operation.
     *
     * @param toBeInserted is an array of objects to be inserted.
     *                     <p>NOTE!</p>
     *                     There should be objects that have not been stored or inserted yet
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link InsertedSequentialGetStepSupplier}
     */
    @SafeVarargs
    public static <T extends PersistableObject> InsertedSequentialGetStepSupplier<T> inserted(T... toBeInserted) {
        return new InsertedSequentialGetStepSupplier<>(asList(toBeInserted));
    }

    /**
     * Creates an instance of {@link InsertedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of INSERT operation.
     *
     * @param toBeInserted is an object of objects to be inserted.
     *                     <p>NOTE!</p>
     *                     There should be an object that has not been stored or inserted yet
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link InsertedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> InsertedSequentialGetStepSupplier<T> inserted(T toBeInserted) {
        return new InsertedSequentialGetStepSupplier<>(of(toBeInserted));
    }

    @Override
    List<T> getResult(PersistenceManager persistenceManager, List<T> listOfObjectsForTransaction) {
        return new OperationResultList<>("List of objects inserted to data store",
                persistenceManager.makePersistentAll(listOfObjectsForTransaction));
    }

    @SuppressWarnings("unchecked")
    private static <T extends PersistableObject> List<T> getObjectsToBeInserted(Collection<T> toBeInserted) {
        var toBeInsertedList = getOperationObjects(toBeInserted);
        checkArgument(toBeInsertedList.size() > 0,
                "Should be defined at least one db object");

        List<T> alreadyInserted = toBeInsertedList
                .stream()
                .filter(t -> isPersistent(t)
                        && !isDeleted(t))
                .collect(toList());

        checkArgument(alreadyInserted.size() == 0,
                format("There are objects already inserted: %s", alreadyInserted));


        return toBeInsertedList
                .stream()
                .map(t -> {
                    if (!isPersistent(t)) {
                        return t;
                    }
                    return (T) t.clone();
                }).collect(toList());
    }
}
