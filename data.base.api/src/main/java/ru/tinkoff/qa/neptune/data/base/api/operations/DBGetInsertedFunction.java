package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isDeleted;
import static javax.jdo.JDOHelper.isPersistent;

public class DBGetInsertedFunction<T extends PersistableObject> extends DBGetOperationFunction<T, DBGetInsertedFunction<T>> {

    private DBGetInsertedFunction(OperationSequentialGetSupplier<T> innerSupplier) {
        super(innerSupplier);
    }

    /**
     * Creates an instance of {@link DBGetInsertedFunction} to build and supply a function that returns a
     * resulted list of INSERT operation.
     *
     * @param toBeInserted is a collection of objects to be inserted.
     *                     <p>NOTE!</p>
     *                     There should be objects that have not been stored or inserted yet
     * @param <T>          is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetInsertedFunction}
     */
    public static <T extends PersistableObject> DBGetInsertedFunction<T> inserted(Collection<T> toBeInserted) {
        var listOfToBeInserted = getObjectsToBeInserted(toBeInserted);
        var description = format("%s Inserted objects mapped by class(es) %s", toBeInserted.size(),
                toBeInserted
                        .stream().map(t -> t.getClass().getName())
                        .distinct()
                        .collect(joining(",")));
        return new DBGetInsertedFunction<>(new OperationSequentialGetSupplier<>(description, (persistableObjects, persistenceManager) ->
                new OperationResultList<>("List of objects inserted to data store",
                        persistenceManager.makePersistentAll(persistableObjects)), listOfToBeInserted));
    }

    /**
     * Creates an instance of {@link DBGetInsertedFunction} to build and supply a function that returns a
     * resulted list of INSERT operation.
     *
     * @param toBeInserted is an array of objects to be inserted.
     *                     <p>NOTE!</p>
     *                     There should be objects that have not been stored or inserted yet
     * @param <T>          is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetInsertedFunction}
     */
    @SafeVarargs
    public static <T extends PersistableObject> DBGetInsertedFunction<T> inserted(T... toBeInserted) {
        return inserted(asList(toBeInserted));
    }

    /**
     * Creates an instance of {@link DBGetInsertedFunction} to build and supply a function that returns a
     * resulted list of INSERT operation.
     *
     * @param toBeInserted is an object of objects to be inserted.
     *                     <p>NOTE!</p>
     *                     There should be an object that has not been stored or inserted yet
     * @param <T>          is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetInsertedFunction}
     */
    public static <T extends PersistableObject> DBGetInsertedFunction<T> inserted(T toBeInserted) {
        return inserted(of(toBeInserted));
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
