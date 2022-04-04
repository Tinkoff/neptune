package ru.tinkoff.qa.neptune.spring.mock.mvc.result.matchers;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.test.web.servlet.result.XpathResultMatchers;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import javax.xml.xpath.XPathExpressionException;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Description("Xpath '{expression}'. Namespaces '{namespaces}'. Arguments {args}")
public class NeptuneXpathResultMatchers extends XpathResultMatchers {

    @DescriptionFragment("expression")
    final String expression;

    @DescriptionFragment("namespaces")
    final String namespaces;

    @DescriptionFragment("args")
    final String args;

    public NeptuneXpathResultMatchers(String expression, Map<String, String> namespaces, Object... args) throws XPathExpressionException {
        super(expression, namespaces, args);
        this.expression = expression;
        this.namespaces = ofNullable(namespaces).map(Object::toString).orElseGet(() -> Map.of().toString());
        this.args = ArrayUtils.toString(args);
    }
}
