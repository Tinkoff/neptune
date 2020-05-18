package ru.tinkoff.qa.neptune.selenium.functions.edit;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Element to edit"
)
public final class EditActionSupplier<T> extends
        SequentialActionSupplier<SeleniumStepContext, Editable<T>, EditActionSupplier<T>> {

    @StepParameter(value = "Change value with", makeReadableBy = EditParameterValueGetter.class)
    private final T toSet;

    private EditActionSupplier(String description, T value) {
        super(description);
        toSet = value;
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
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier<R> valueOfThe(
            SearchSupplier<S> of, R value) {
        return new EditActionSupplier<>("Edit element " + of, value)
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
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier<R> valueOfThe(S of, R value) {
        return new EditActionSupplier<>("Edit element " + of, value)
                .performOn(of);
    }

    @Override
    protected void performActionOn(Editable<T> value) {
        value.edit(toSet);
    }
}
