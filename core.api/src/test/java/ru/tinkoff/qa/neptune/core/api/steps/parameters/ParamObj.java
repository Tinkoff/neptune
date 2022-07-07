package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

public class ParamObj implements StepParameterPojo {
    @StepParameter("Pojo Parameter 1")
    private Boolean param1;
    @StepParameter(value = "Pojo Parameter 2", doNotReportNullValues = true)
    private String param2;
    private Integer param3;

    public Boolean getParam1() {
        return param1;
    }

    public ParamObj setParam1(Boolean param1) {
        this.param1 = param1;
        return this;
    }

    public String getParam2() {
        return param2;
    }

    public ParamObj setParam2(String param2) {
        this.param2 = param2;
        return this;
    }

    public Integer getParam3() {
        return param3;
    }

    public ParamObj setParam3(Integer param3) {
        this.param3 = param3;
        return this;
    }
}
