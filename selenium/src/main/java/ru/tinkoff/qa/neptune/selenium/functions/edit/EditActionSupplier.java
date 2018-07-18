package ru.tinkoff.qa.neptune.selenium.functions.edit;

import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.selenium.CurrentContentFunction;

import static com.google.common.base.Preconditions.checkArgument;


public final class EditActionSupplier extends
        SequentialActionSupplier<SeleniumSteps, Editable, EditActionSupplier> {

    private EditActionSupplier() {
        super();
    }

    /**
     * Builds the edit action on some editable element
     * @param of is how to find the editable element
     * @param value is the value used to change value of the element
     * @param <R> is the type of some value which is used to change the value of element
     * @param <S> if the type of editable element
     * @return built edit action
     */
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier valueOfThe(
            SearchSupplier<S> of, R value) {
        return new EditActionSupplier().andValueOfThe(of, value);
    }

    /**
     * Builds the edit action on some editable element
     * @param of is the editable element
     * @param value is the value used to change value of the element
     * @param <R> is the type of some value which is used to change the value of element
     * @param <S> if the type of editable element
     * @return built edit action
     */
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier valueOfThe(S of, R value) {
        return new EditActionSupplier().andValueOfThe(of, value);
    }

    public <T, Q extends SearchContext & Editable<T>> EditActionSupplier andValueOfThe(SearchSupplier<Q> of, T value) {
        checkArgument(of != null, "The searching for the editable element should be defined");
        checkArgument(value != null, "The value which is used to edit the element should be defined");
        return andThen("Edit",
                of.get().compose(CurrentContentFunction.currentContent()), value);
    }

    public <T, Q extends SearchContext & Editable<T>> EditActionSupplier andValueOfThe(Q of, T value) {
        checkArgument(of != null, "The editable element should be defined");
        checkArgument(value != null, "The value which is used to edit the element should be defined");
        return andThen("Edit", of, value);
    }

    @Override
    protected void performActionOn(Editable value, Object... additionalArgument) {
        value.edit(additionalArgument[0]);
    }
}
