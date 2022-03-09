package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Description("Test action")
@SequentialActionSupplier.DefinePerformOnParameterName
@IncludeParamsOfInnerGetterStep
public class TestActionStepSupplier extends SequentialActionSupplier<Object, Object, TestActionStepSupplier> {

    @StepParameter("Action Parameter 1")
    private Boolean param1;
    private ParamObj paramObj;
    @StepParameter(value = "Action Parameter 2", doNotReportNullValues = true)
    private String param2;
    private Integer param3;

    protected TestActionStepSupplier() {
        super();
    }

    public static TestActionStepSupplier getTestActionStepSupplier() {
        return new TestActionStepSupplier();
    }

    @Override
    protected TestActionStepSupplier performOn(Object value) {
        return super.performOn(value);
    }

    @Override
    protected TestActionStepSupplier performOn(Function<Object, ?> function) {
        return super.performOn(function);
    }

    @Override
    protected TestActionStepSupplier performOn(SequentialGetStepSupplier<Object, ?, ?, ?, ?> supplier) {
        return super.performOn(supplier);
    }

    @Override
    protected void howToPerform(Object value) {
    }

    public TestActionStepSupplier setParam1(Boolean param1) {
        this.param1 = param1;
        return this;
    }

    public String getParam2() {
        return param2;
    }

    public TestActionStepSupplier setParam2(String param2) {
        this.param2 = param2;
        return this;
    }

    public Integer getParam3() {
        return param3;
    }

    public TestActionStepSupplier setParam3(Integer param3) {
        this.param3 = param3;
        return this;
    }

    public ParamObj getParamObj() {
        return paramObj;
    }

    public TestActionStepSupplier setParamObj(ParamObj paramObj) {
        this.paramObj = paramObj;
        return this;
    }

    @Override
    protected Map<String, String> additionalParameters() {
        var result = new LinkedHashMap<String, String>();
        result.put("Action Step 1: test parameter 1", "test value 1");
        result.put("Action Step 1: test parameter 2", "test value 2");
        result.put("Action Step 1: test parameter 3", "test value 3");
        result.put("Action Step 1: test parameter 4", "test value 4");
        return result;
    }
}
