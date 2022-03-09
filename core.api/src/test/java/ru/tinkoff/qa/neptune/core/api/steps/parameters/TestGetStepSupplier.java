package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@SequentialGetStepSupplier.DefineCriteriaParameterName
@SequentialGetStepSupplier.DefinePollingTimeParameterName
@SequentialGetStepSupplier.DefineTimeOutParameterName
@SequentialGetStepSupplier.DefineFromParameterName
@IncludeParamsOfInnerGetterStep
public class TestGetStepSupplier extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<Object, Object, Object, TestGetStepSupplier> {
    @StepParameter("Parameter 1")
    private Boolean param1;
    private ParamObj paramObj;
    @StepParameter(value = "Parameter 2", doNotReportNullValues = true)
    private String param2;
    private Integer param3;

    protected TestGetStepSupplier() {
        super(o -> new Object[]{new Object()});
    }

    @Description("getTestGetStepSupplier")
    public static TestGetStepSupplier getTestGetStepSupplier() {
        return new TestGetStepSupplier();
    }

    @Override
    protected TestGetStepSupplier from(Object from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier from(Function<Object, ?> from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier from(SequentialGetStepSupplier<Object, ?, ?, ?, ?> from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    public TestGetStepSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    public Boolean getParam1() {
        return param1;
    }

    public TestGetStepSupplier setParam1(Boolean param1) {
        this.param1 = param1;
        return this;
    }

    public String getParam2() {
        return param2;
    }

    public TestGetStepSupplier setParam2(String param2) {
        this.param2 = param2;
        return this;
    }

    public Integer getParam3() {
        return param3;
    }

    public TestGetStepSupplier setParam3(Integer param3) {
        this.param3 = param3;
        return this;
    }

    public ParamObj getParamObj() {
        return paramObj;
    }

    public TestGetStepSupplier setParamObj(ParamObj paramObj) {
        this.paramObj = paramObj;
        return this;
    }

    @Override
    protected Map<String, String> additionalParameters() {
        var result = new LinkedHashMap<String, String>();
        result.put("Get Step 1: test parameter 1", "test value 1");
        result.put("Get Step 1: test parameter 2", "test value 2");
        result.put("Get Step 1: test parameter 3", "test value 3");
        result.put("Get Step 1: test parameter 4", "test value 4");
        return result;
    }
}
