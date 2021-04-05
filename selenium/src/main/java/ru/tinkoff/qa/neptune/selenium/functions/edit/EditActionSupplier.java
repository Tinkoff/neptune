package ru.tinkoff.qa.neptune.selenium.functions.edit;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.captors.WebElementImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@CaptureOnFailure(by = WebElementImageCaptor.class)
@CaptureOnSuccess(by = WebElementImageCaptor.class)
@SequentialActionSupplier.DefinePerformOnParameterName("Element to edit")
public final class EditActionSupplier<T> extends
        SequentialActionSupplier<SeleniumStepContext, Editable<T>, EditActionSupplier<T>> {

    @StepParameter(value = "Change value with", makeReadableBy = EditParameterValueGetter.class)
    private final T toSet;

    private EditActionSupplier(T value) {
        super();
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
    @Description("Edit element {of}")
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier<R> valueOfThe(
            @DescriptionFragment("of") SearchSupplier<S> of, R value) {
        return new EditActionSupplier<>(value)
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
    @Description("Edit element {of}")
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier<R> valueOfThe(@DescriptionFragment("of") S of, R value) {
        return new EditActionSupplier<>(value)
                .performOn(of);
    }

    @Override
    protected void performActionOn(Editable<T> value) {
        value.edit(toSet);
    }
}
