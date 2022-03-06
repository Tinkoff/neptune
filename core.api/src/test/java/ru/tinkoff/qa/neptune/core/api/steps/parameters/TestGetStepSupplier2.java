package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@SequentialGetStepSupplier.DefineTimeOutParameterName("Custom Time out")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Custom criteria")
@SequentialGetStepSupplier.DefinePollingTimeParameterName("Custom sleeping")
@SequentialGetStepSupplier.DefineFromParameterName("Custom from")
@Description("getTestGetStepSupplier2")
public class TestGetStepSupplier2 extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<Object, Object, Object, TestGetStepSupplier2> {

    @StepParameter("Parameter 11")
    private Boolean param1;
    private ParamObj paramObj;
    @StepParameter(value = "Parameter 21", doNotReportNullValues = true)
    private String param2;
    private Integer param3;


    protected TestGetStepSupplier2() {
        super(o -> new Object[]{new Object()});
    }

    public static TestGetStepSupplier2 getTestGetStepSupplier2() {
        return new TestGetStepSupplier2();
    }

    @Override
    protected TestGetStepSupplier2 from(Object from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier2 from(Function<Object, ?> from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier2 from(SequentialGetStepSupplier<Object, ?, ?, ?, ?> from) {
        return super.from(from);
    }

    @Override
    protected TestGetStepSupplier2 pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    protected TestGetStepSupplier2 timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    public Boolean getParam1() {
        return param1;
    }

    public TestGetStepSupplier2 setParam1(Boolean param1) {
        this.param1 = param1;
        return this;
    }

    public String getParam2() {
        return param2;
    }

    public TestGetStepSupplier2 setParam2(String param2) {
        this.param2 = param2;
        return this;
    }

    public Integer getParam3() {
        return param3;
    }

    public TestGetStepSupplier2 setParam3(Integer param3) {
        this.param3 = param3;
        return this;
    }

    public ParamObj getParamObj() {
        return paramObj;
    }

    public TestGetStepSupplier2 setParamObj(ParamObj paramObj) {
        this.paramObj = paramObj;
        return this;
    }

    @Override
    protected Map<String, String> additionalParameters() {
        var result = new LinkedHashMap<String, String>();
        result.put("Get Step 2: test parameter 1", "test value 1");
        result.put("Get Step 2: test parameter 2", "test value 2");
        result.put("Get Step 2: test parameter 3", "test value 3");
        result.put("Get Step 2: test parameter 4", "test value 4");
        return result;
    }
}
