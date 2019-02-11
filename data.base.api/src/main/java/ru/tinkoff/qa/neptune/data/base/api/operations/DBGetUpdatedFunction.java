package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier;

import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isDeleted;
import static javax.jdo.JDOHelper.isPersistent;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.action;

public class DBGetUpdatedFunction<T extends PersistableObject> extends DBGetOperationFunction<T, DBGetUpdatedFunction<T>> {

    private final List<Consumer<T>> updateActions = new ArrayList<>();

    private DBGetUpdatedFunction(OperationSequentialGetSupplier<T> innerSupplier) {
        super(innerSupplier);
    }

    /**
     * Creates an instance of {@link DBGetUpdatedFunction} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeUpdated is a collection of objects to be updated.
     *                     <p>NOTE!</p>
     *                     There should be objects that have been stored previously
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetUpdatedFunction}
     */
    public static <T extends PersistableObject> DBGetUpdatedFunction<T> updated(Collection<T> toBeUpdated) {
        var getUpdated = new GetUpdatedBiFunction<T>();
        var toBeUpdatedList = getObjectsToBeUpdated(toBeUpdated);
        var description = format("%s Updated objects from tables %s", toBeUpdated.size(),
                toBeUpdated
                        .stream()
                        .map(PersistableObject::fromTable)
                        .distinct()
                        .collect(joining(",")));
        var toReturn =  new DBGetUpdatedFunction<>(new OperationSequentialGetSupplier<>(description, getUpdated, toBeUpdatedList));
        getUpdated.setUpdateActions(toReturn.updateActions);
        return toReturn;
    }

    /**
     * Creates an instance of {@link DBGetUpdatedFunction} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeUpdated is an array of objects to be updated.
     *                     <p>NOTE!</p>
     *                     There should be objects that have been stored previously
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetUpdatedFunction}
     */
    @SafeVarargs
    public static <T extends PersistableObject> DBGetUpdatedFunction<T> updated(T... toBeUpdated) {
        return updated(asList(toBeUpdated));
    }

    /**
     * Creates an instance of {@link DBGetUpdatedFunction} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeUpdated is an object to be updated.
     *                     <p>NOTE!</p>
     *                     There should be an object that has been stored previously
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetUpdatedFunction}
     */
    public static <T extends PersistableObject> DBGetUpdatedFunction<T> updated(T toBeUpdated) {
        return updated(of(toBeUpdated));
    }

    /**
     * Creates an instance of {@link DBGetUpdatedFunction} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param select is how to find objects to be updated.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetUpdatedFunction}
     */
    public static <T extends PersistableObject> DBGetUpdatedFunction<T> updated(SelectListGetSupplier<T, ?> select) {
        var getUpdated = new GetUpdatedBiFunction<T>();
        var toReturn =  new DBGetUpdatedFunction<T>(new OperationSequentialGetSupplier<>("Updated objects", getUpdated, select));
        getUpdated.setUpdateActions(toReturn.updateActions);
        return toReturn;
    }

    /**
     * Creates an instance of {@link DBGetUpdatedFunction} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param select is how to find an object to be updated.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetUpdatedFunction}
     */
    public static <T extends PersistableObject> DBGetUpdatedFunction<T> updated(SelectOneGetSupplier<T, ?> select) {
        var getUpdated = new GetUpdatedBiFunction<T>();
        var toReturn =  new DBGetUpdatedFunction<T>(new OperationSequentialGetSupplier<>("Updated objects", getUpdated, select));
        getUpdated.setUpdateActions(toReturn.updateActions);
        return toReturn;
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

    /**
     * Defines the update-action to be performed
     *
     * @param descriptionOfTheChange is a description of update-action
     * @param updateAction is a {@link Consumer} that contains an algorithm of an update-action
     * @return self-reference
     */
    public DBGetUpdatedFunction<T> set(String descriptionOfTheChange, Consumer<T> updateAction) {
        updateActions.add(action(descriptionOfTheChange, updateAction));
        return this;
    }

    private static class GetUpdatedBiFunction<T extends PersistableObject>
            implements BiFunction<Collection<T>, PersistenceManager, List<T>> {

        private List<Consumer<T>> updateActions;

        @Override
        @SuppressWarnings("unchecked")
        public List<T> apply(Collection<T> ts, PersistenceManager ignored) {
            checkArgument(updateActions.size() > 0, "Should be defined at least one updating action");
            ts.forEach(t ->
                    action(format("Update %s", t), o ->
                            updateActions.forEach(consumer -> consumer.accept((T) o))).accept(t));
            return new OperationResultList<>("List of updated data store objects", ts);
        }

        void setUpdateActions(List<Consumer<T>> updateActions) {
            this.updateActions = updateActions;
        }
    }
}
