package com.github.toy.constructor.selenium.functions.edit;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier;
import org.openqa.selenium.SearchContext;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;


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
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier valueOf(
            SequentialSearchSupplier<S> of, R value) {
        checkArgument(of != null, "The searching for the editable element should be defined");
        checkArgument(value != null, "The value which is used to edit the element should be defined");
        return new EditActionSupplier().andThen(format("Edit. Set new value %s", value),
                of.get(), value);
    }

    /**
     * Builds the edit action on some editable element
     * @param of is the editable element
     * @param value is the value used to change value of the element
     * @param <R> is the type of some value which is used to change the value of element
     * @param <S> if the type of editable element
     * @return built edit action
     */
    public static <R, S extends SearchContext & Editable<R>> EditActionSupplier valueOf(S of, R value) {
        checkArgument(of != null, "The editable element should be defined");
        checkArgument(value != null, "The value which is used to edit the element should be defined");
        return new EditActionSupplier().andThen(format("Edit. Set new value %s", value), of, value);
    }

    public <T, Q extends SearchContext & Editable<T>> EditActionSupplier andValueOf(SequentialSearchSupplier<Q> of, T value) {
        checkArgument(of != null, "The searching for the editable element should be defined");
        checkArgument(value != null, "The value which is used to edit the element should be defined");
        return andThen(format("Edit. Set new value %s", value),
                of.get(), value);
    }

    public <T, Q extends SearchContext & Editable<T>> EditActionSupplier andValueOf(Q of, T value) {
        checkArgument(of != null, "The WWWeditable element should be defined");
        checkArgument(value != null, "The value which is used to edit the element should be defined");
        return andThen(format("Edit. Set new value %s", value), of, value);
    }

    @Override
    protected void performActionOn(Editable value, Object... additionalArgument) {
        value.edit(additionalArgument[0]);
    }
}
