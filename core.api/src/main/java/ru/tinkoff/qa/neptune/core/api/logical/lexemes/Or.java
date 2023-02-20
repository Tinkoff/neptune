package ru.tinkoff.qa.neptune.core.api.logical.lexemes;

import ru.tinkoff.qa.neptune.core.api.steps.SelfDescribed;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

/**
 * It is used to link descriptions of operators in descriptions of OR-expressions
 */
@Description("or")
public final class Or extends SelfDescribed {

    public static final Or OR_LEXEME = new Or();

    private Or() {
        super();
    }
}
