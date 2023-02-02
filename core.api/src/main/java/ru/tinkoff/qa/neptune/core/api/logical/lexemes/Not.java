package ru.tinkoff.qa.neptune.core.api.logical.lexemes;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

/**
 * It is used to link descriptions of operators in descriptions of NOT-expressions
 */
@Description("not")
public final class Not extends SelfDescribed {
    public static final Not NOT_LEXEME = new Not();

    private Not() {
        super();
    }
}
