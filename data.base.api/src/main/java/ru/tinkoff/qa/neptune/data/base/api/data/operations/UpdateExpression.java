package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.action;

public final class UpdateExpression<T extends PersistableObject> {

    private final List<Consumer<T>> updateActions = new ArrayList<>();

    private UpdateExpression() {
        super();
    }

    public static <T extends PersistableObject> UpdateExpression<T> set(String description,  Consumer<T> setAction) {
        return new UpdateExpression<T>().setElse(description, setAction);
    }

    public UpdateExpression<T> setElse(String description,  Consumer<T> setAction) {
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
                result.add(action(tConsumer.toString(), ts -> ts.forEach(tConsumer))));
        return result;
    }
}
