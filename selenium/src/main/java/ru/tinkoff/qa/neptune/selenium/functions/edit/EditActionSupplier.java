package ru.tinkoff.qa.neptune.selenium.functions.edit;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.SearchContext;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
public final class EditActionSupplier extends
        SequentialActionSupplier<SeleniumStepContext, Editable, EditActionSupplier> {

    private static final String DESCRIPTION = "Edit element %s. Change value with [%s]";

    private final Object toSet;

    private EditActionSupplier(String description, Object value) {
        super(description);
        toSet = value;
    }

    @SuppressWarnings("unchecked")
    private static <T> String addDescriptionOfTheSetValue(T toBeSet) {
        checkArgument(nonNull(toBeSet), "The value to be used for the editing should not be null");
        var clazz = toBeSet.getClass();

        Stream<T> stream;
        if (Iterable.class.isAssignableFrom(clazz)) {
            stream = stream(((Iterable<T>) toBeSet).spliterator(), false);
        } else if (clazz.isArray()) {
            stream = Arrays.stream((T[]) toBeSet);
        } else {
            stream = of(toBeSet).stream();
        }

        return stream.map(t -> {
            checkArgument(nonNull(t), "A null-value is defined to change value of an element");
            if (t.getClass().isEnum()) {
                return ((Enum) t).name();
            }
            return String.valueOf(t);
        }).collect(joining(","));
    }

    /**
     * Builds the edit action on some editable element
     *
     * @param of    is how to find the editable element
     * @param value is the value used to change value of the element
     * @param <R>   is the type of some value which is used to change the value of element
     * @param <S>   if the type of editable element
     * @return built edit action
     */
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier valueOfThe(
            SearchSupplier<S> of, R value) {
        return new EditActionSupplier(format(DESCRIPTION, of, addDescriptionOfTheSetValue(value)), value)
                .performOn(of.get().compose(currentContent()));
    }

    /**
     * Builds the edit action on some editable element
     *
     * @param of    is the editable element
     * @param value is the value used to change value of the element
     * @param <R>   is the type of some value which is used to change the value of element
     * @param <S>   if the type of editable element
     * @return built edit action
     */
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier valueOfThe(S of, R value) {
        return new EditActionSupplier(format(DESCRIPTION, of, addDescriptionOfTheSetValue(value)), value).performOn(of);
    }

    public <T, Q extends SearchContext & Editable<T>> EditActionSupplier andValueOfThe(SearchSupplier<Q> of, T value) {
        checkArgument(nonNull(of), "The searching for the editable element should be defined");
        checkArgument(nonNull(value), "The value which is used to edit the element should be defined");
        return mergeActionSequenceFrom(valueOfThe(of, value));
    }

    public <T, Q extends SearchContext & Editable<T>> EditActionSupplier andValueOfThe(Q of, T value) {
        checkArgument(nonNull(of), "The editable element should be defined");
        checkArgument(nonNull(value), "The value which is used to edit the element should be defined");
        return mergeActionSequenceFrom(valueOfThe(of, value));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void performActionOn(Editable value) {
        value.edit(toSet);
    }
}
