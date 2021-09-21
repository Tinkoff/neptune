package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Check expectation:")
@Description("{description}")
final class CheckMockMvcExpectation extends SequentialActionSupplier<ResultActions, ResultActions, CheckMockMvcExpectation> {

    @DescriptionFragment("description")
    private final ResultMatcher matcher;

    CheckMockMvcExpectation(ResultMatcher matcher) {
        checkNotNull(matcher);
        this.matcher = matcher;
    }

    static CheckMockMvcExpectation checkExpectation(ResultMatcher matcher) {
        return new CheckMockMvcExpectation(matcher).performOn(mvcResult -> mvcResult);
    }

    @Override
    protected void howToPerform(ResultActions value) {
        try {
            value.andExpect(matcher);
        } catch (Throwable e) {
            if (e instanceof AssertionError) {
                throw (AssertionError) e;
            }
            throw new AssertionError(e.getMessage(), e);
        }
    }
}
