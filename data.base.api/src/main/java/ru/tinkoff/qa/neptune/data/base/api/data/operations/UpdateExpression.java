package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * This class describes the updating operation.
 *
 * @param <T> is a type of {@link PersistableObject} to be updated
 */
@Deprecated(forRemoval = true)
public final class UpdateExpression<T extends PersistableObject> {

    private final String description;
    private final Consumer<T> updateAction;

    private UpdateExpression(String description, Consumer<T> updateAction) {
        checkArgument(isNotBlank(description), "Description of an update action should not be a null or blank string");
        checkArgument(nonNull(updateAction), "Please define an update action");
        this.description = description;
        this.updateAction = updateAction;
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
        return new UpdateExpression<>(translate(description), setAction);
    }

    @Override
    public String toString() {
        return description;
    }

    void performUpdate(List<T> toUpdate) {
        ofNullable(toUpdate).ifPresent(ts -> ts.forEach(updateAction));
    }
}
