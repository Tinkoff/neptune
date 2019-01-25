package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.query.*;

import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isDeleted;
import static javax.jdo.JDOHelper.isPersistent;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.action;

/**
 * This is the class that builds and supplies functions to return results of UPDATE operation.
 * Transactions are used to keep data of the data store valid when something is going wrong. UPDATE
 * operations are supposed to return lists of updated objects.
 *
 * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
 */
public class UpdatedSequentialGetStepSupplier <T extends PersistableObject>
        extends OperationSequentialGetSupplier<T, UpdatedSequentialGetStepSupplier<T>> {

    private final List<Consumer<T>> updateActions = new ArrayList<>();
    private static final String DESCRIPTION = "Updated objects";

    private UpdatedSequentialGetStepSupplier(Collection<T> toBeUpdated) {
        super(getDescription(format("%s:", DESCRIPTION), toBeUpdated), getObjectsToBeUpdated(toBeUpdated));
    }

    private UpdatedSequentialGetStepSupplier(SelectSequentialGetStepSupplier<?, ?, ?> select) {
        super(DESCRIPTION, select);
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeUpdated is a collection of objects to be updated.
     *                     <p>NOTE!</p>
     *                     There should be objects that have been stored or inserted
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link UpdatedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> UpdatedSequentialGetStepSupplier<T> updated(Collection<T> toBeUpdated) {
        return new UpdatedSequentialGetStepSupplier<>(toBeUpdated);
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeUpdated is an array of objects to be updated.
     *                     <p>NOTE!</p>
     *                     There should be objects that have been stored or inserted
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link UpdatedSequentialGetStepSupplier}
     */
    @SafeVarargs
    public static <T extends PersistableObject> UpdatedSequentialGetStepSupplier<T> updated(T... toBeUpdated) {
        return new UpdatedSequentialGetStepSupplier<>(asList(toBeUpdated));
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeUpdated is an object to be updated.
     *                     <p>NOTE!</p>
     *                     There should be objects that have been stored or inserted
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link UpdatedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> UpdatedSequentialGetStepSupplier<T> updated(T toBeUpdated) {
        return new UpdatedSequentialGetStepSupplier<>(of(toBeUpdated));
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param selectId is how to find objects to be updated.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link UpdatedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> UpdatedSequentialGetStepSupplier<T> updated(SelectListByIdsSupplier<T> selectId) {
        return new UpdatedSequentialGetStepSupplier<>(selectId);
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param selectId is how to find an object to be updated.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link UpdatedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> UpdatedSequentialGetStepSupplier<T> updated(SelectSingleObjectByIdSupplier<T> selectId) {
        return new UpdatedSequentialGetStepSupplier<>(selectId);
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param selectByQuery is how to find objects to be updated.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link UpdatedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> UpdatedSequentialGetStepSupplier<T> updated(SelectListByQuerySupplier<T, ?> selectByQuery) {
        return new UpdatedSequentialGetStepSupplier<>(selectByQuery);
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param selectByQuery is how to find an object to be updated.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link UpdatedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> UpdatedSequentialGetStepSupplier<T> updated(SelectSingleObjectByQuerySupplier<T, ?> selectByQuery) {
        return new UpdatedSequentialGetStepSupplier<>(selectByQuery);
    }

    /**
     * Defines the update-action to be performed
     *
     * @param descriptionOfTheChange is a description of update-action
     * @param updateAction is a {@link Consumer} that contains an algorithm of an update-action
     * @return self-reference
     */
    public UpdatedSequentialGetStepSupplier<T> set(String descriptionOfTheChange, Consumer<T> updateAction) {
        updateActions.add(action(descriptionOfTheChange, updateAction));
        return this;
    }

    private static <T extends PersistableObject> List<T> getObjectsToBeUpdated(Collection<T> toBeInserted) {
        var toBeUpdated = getOperationObjects(toBeInserted);

        List<T> notStored = toBeUpdated
                .stream()
                .filter(t -> !isPersistent(t) || isDeleted(t))
                .collect(toList());

        checkArgument(notStored.size() == 0,
                format("There are objects that are not stored in DB: %s", notStored));


        return toBeUpdated;
    }

    @Override
    @SuppressWarnings("unchecked")
    List<T> getResult(PersistenceManager ignored, List<T> listOfObjectsForTransaction) {
        checkArgument(updateActions.size() > 0, "Should be defined at least one updating action");
        listOfObjectsForTransaction.forEach(t ->
                action(format("Update %s", t), o ->
                        updateActions.forEach(consumer -> consumer.accept((T) o))).accept(t));
        return new OperationResultList<>("List of updated data store objects", listOfObjectsForTransaction);
    }
}
