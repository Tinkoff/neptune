package ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Jsonpath '{expression}'. Arguments {args}")
public class NeptuneJsonPathResultMatchers extends JsonPathResultMatchers {

    @DescriptionFragment("expression")
    final String expression;

    @DescriptionFragment("args")
    final String args;

    public NeptuneJsonPathResultMatchers(String expression, Object... args) {
        super(expression, args);
        this.expression = expression;
        this.args = ArrayUtils.toString(args);
    }
}
