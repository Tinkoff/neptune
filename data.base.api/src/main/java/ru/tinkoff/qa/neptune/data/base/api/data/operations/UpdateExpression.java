package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class describes the updating operation.
 *
 * @param <T> is a type of {@link PersistableObject} to be updated
 */
public final class UpdateExpression<T extends PersistableObject> {

    private final List<Consumer<T>> updateActions = new ArrayList<>();

    private UpdateExpression() {
        super();
    }

    /**
     * Creates an object of {@link UpdateExpression} that describes the updating operation. The sample below:
     * <p>
     * {@code '
     * change("Set name = 'New Name' and new birthday", person -> {
     *     person.setName("New Name");
     *     person.setBirthDay(date);
     * })
     * '}
     * </p>
     *
     * @param description is a narrative text that describes the updating for the logging
     * @param setAction is a consumer that supposed to accept an object to be updated
     * @param <T> <T> is a type of {@link PersistableObject} to be updated
     * @return a new {@link UpdateExpression}
     */
    public static <T extends PersistableObject> UpdateExpression<T> change(String description, Consumer<T> setAction) {
        return new UpdateExpression<T>().changeAlso(description, setAction);
    }

    /**
     * This method is designed to support the chain of update-actions. The sample below:
     *
     * <p>
     * {@code '
     * change("Set name = 'New Name' and new birthday", person -> {
     *     person.setName("New Name");
     *     person.setBirthDay(date);
     * })
     * .changeAlso("Set new marital status", person -> {
     *      person.setMaritalStatus(newMarital);
     * })
     * '}
     * </p>
     *
     * @param description s a narrative text that describes the updating for the logging
     * @param setAction is a consumer that supposed to accept an object to be updated
     * @return self-reference
     */
    public UpdateExpression<T> changeAlso(String description, Consumer<T> setAction) {
        checkArgument(isNotBlank(description), "Description of an update action should not be a null or blank string");
        checkArgument(nonNull(setAction), "Please define an update action");
        updateActions.add(new Consumer<>() {
            @Override
            public void accept(T t) {
                setAction.accept(t);
            }

            @Override
            public String toString() {
                return description;
            }
        });
        return this;
    }

    List<Consumer<List<T>>> getUpdateAction() {
        var result = new ArrayList<Consumer<List<T>>>();
        updateActions.forEach(tConsumer ->
                result.add(new Consumer<>() {
                    @Override
                    public void accept(List<T> ts) {
                        ts.forEach(tConsumer);
                    }

                    @Override
                    public String toString() {
                        return tConsumer.toString();
                    }
                }));
        return result;
    }
}
