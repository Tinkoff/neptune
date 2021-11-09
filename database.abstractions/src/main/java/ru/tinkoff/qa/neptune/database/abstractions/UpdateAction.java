package ru.tinkoff.qa.neptune.database.abstractions;

import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

public final class UpdateAction<T> {

    private final String description;
    private final Consumer<T> updateAction;

    private UpdateAction(String description, Consumer<T> updateAction) {
        checkArgument(isNotBlank(description), "Description of an update action should not be a null or blank string");
        checkArgument(nonNull(updateAction), "Please define an update action");
        this.description = translate(description);
        this.updateAction = updateAction;
    }

    /**
     * Creates an object of {@link UpdateAction} that describes the updating operation. The sample below:
     * <p>
     * {@code '
     * change("Set name = 'New Name' and new birthday", person -> {
     * person.setName("New Name");
     * person.setBirthDay(date);
     * })
     * '}
     * </p>
     *
     * @param description is a narrative text that describes the updating for the logging
     * @param setAction   is a consumer that supposed to accept an object to be updated
     * @param <T>         <T> is a type to be updated
     * @return a new {@link UpdateAction}
     */
    public static <T> UpdateAction<T> change(String description, Consumer<T> setAction) {
        return new UpdateAction<>(translate(description), setAction);
    }

    @Override
    public String toString() {
        return description;
    }

    public void performUpdate(List<T> toUpdate) {
        ofNullable(toUpdate).ifPresent(ts -> ts.forEach(updateAction));
    }
}
