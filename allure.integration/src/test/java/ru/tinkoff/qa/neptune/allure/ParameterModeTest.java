package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.model.Parameter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static io.qameta.allure.model.Parameter.Mode.DEFAULT;
import static io.qameta.allure.model.Parameter.Mode.MASKED;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.allure.AllureEventLogger.createParameter;
import static ru.tinkoff.qa.neptune.allure.properties.MaskedParametersProperty.MASKED_PARAMETERS;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfIterable;

public class ParameterModeTest {

    private final static Map<String, String> PARAMS_AND_VALUES = new LinkedHashMap<>() {
        {
            put("paramA", "A");
            put("paramA1", "A1");
            put("paramB", "B");
            put("paramC", "B");
        }
    };


    @Test
    public void defaultModeTest() {
        var params = new LinkedList<Parameter>();

        PARAMS_AND_VALUES.forEach((s, o) -> params.addLast(createParameter(s, o)));
        assertThat(params.stream().map(Parameter::getMode).collect(toList()), eachOfIterable(is(DEFAULT)));
    }

    @Test
    public void maskedModeTest() {
        var params = new LinkedList<Parameter>();
        var resultMap = new LinkedHashMap<String, Parameter.Mode>();
        MASKED_PARAMETERS.accept(List.of("paramA", "paramC"));

        PARAMS_AND_VALUES.forEach((s, o) -> params.addLast(createParameter(s, o)));
        params.forEach(p -> resultMap.put(p.getName(), p.getMode()));

        assertThat(resultMap, mapInOrder(
            mapEntry("paramA", MASKED),
            mapEntry("paramA1", MASKED),
            mapEntry("paramB", DEFAULT),
            mapEntry("paramC", MASKED)
        ));
    }

    @AfterMethod
    public void afterEveryTest() {
        MASKED_PARAMETERS.accept(null);
    }

}
