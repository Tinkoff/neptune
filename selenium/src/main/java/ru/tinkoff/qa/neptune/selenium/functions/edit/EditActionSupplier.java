package ru.tinkoff.qa.neptune.selenium.functions.edit;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.captors.ImageCaptorAfterActionOnElement;
import ru.tinkoff.qa.neptune.selenium.captors.WebElementImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

@CaptureOnFailure(by = WebElementImageCaptor.class)
@CaptureOnSuccess(by = ImageCaptorAfterActionOnElement.class)
@Description("Edit element {toEdit}")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class EditActionSupplier<T> extends
        SequentialActionSupplier<Object, Editable<T>, EditActionSupplier<T>> {

    @DescriptionFragment("toEdit")
    final Object toEdit;

    @StepParameter(value = "New value / Change value with", makeReadableBy = EditParameterValueGetter.class)
    private final T toSet;

    private EditActionSupplier(Object toEdit, T value) {
        super();
        this.toEdit = toEdit;
        toSet = value;
    }

    private <S extends SearchContext & Editable<T>> EditActionSupplier(S toEdit, T value) {
        this((Object) toEdit, value);
        performOn(toEdit);
    }

    private <S extends SearchContext & Editable<T>> EditActionSupplier(SearchSupplier<S> toEdit, T value) {
        this((Object) toEdit, value);
        performOn(toEdit);
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
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier<R> valueOfThe(SearchSupplier<S> of, R value) {
        return new EditActionSupplier<>(of, value);
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
        return new EditActionSupplier<>(of, value);
    }

    @Override
    protected void howToPerform(Editable<T> value) {
        value.edit(toSet);
    }
}
