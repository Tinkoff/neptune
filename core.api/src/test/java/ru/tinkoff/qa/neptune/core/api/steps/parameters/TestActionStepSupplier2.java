package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@SequentialActionSupplier.DefinePerformOnParameterName("Perform on custom")
@Description("TestActionStepSupplier2")
public class TestActionStepSupplier2 extends SequentialActionSupplier<Object, Object, TestActionStepSupplier2> {

    @StepParameter("Action Parameter 1")
    private Boolean param1;
    private ParamObj paramObj;
    @StepParameter(value = "Action Parameter 2", doNotReportNullValues = true)
    private String param2;
    private Integer param3;

    protected TestActionStepSupplier2() {
        super();
    }

    public static TestActionStepSupplier2 getTestActionStepSupplier2() {
        return new TestActionStepSupplier2();
    }

    @Override
    protected TestActionStepSupplier2 performOn(Object value) {
        return super.performOn(value);
    }

    @Override
    protected TestActionStepSupplier2 performOn(Function<Object, ?> function) {
        return super.performOn(function);
    }

    @Override
    protected TestActionStepSupplier2 performOn(SequentialGetStepSupplier<Object, ?, ?, ?, ?> supplier) {
        return super.performOn(supplier);
    }

    @Override
    protected void howToPerform(Object value) {
    }

    public TestActionStepSupplier2 setParam1(Boolean param1) {
        this.param1 = param1;
        return this;
    }

    public String getParam2() {
        return param2;
    }

    public TestActionStepSupplier2 setParam2(String param2) {
        this.param2 = param2;
        return this;
    }

    public Integer getParam3() {
        return param3;
    }

    public TestActionStepSupplier2 setParam3(Integer param3) {
        this.param3 = param3;
        return this;
    }

    public ParamObj getParamObj() {
        return paramObj;
    }

    public TestActionStepSupplier2 setParamObj(ParamObj paramObj) {
        this.paramObj = paramObj;
        return this;
    }

    @Override
    protected Map<String, String> additionalParameters() {
        var result = new LinkedHashMap<String, String>();
        result.put("Action Step 2: test parameter 1", "test value 1");
        result.put("Action Step 2: test parameter 2", "test value 2");
        result.put("Action Step 2: test parameter 3", "test value 3");
        result.put("Action Step 2: test parameter 4", "test value 4");
        return result;
    }
}
