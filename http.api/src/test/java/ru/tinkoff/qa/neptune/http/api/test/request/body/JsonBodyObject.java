package ru.tinkoff.qa.neptune.http.api.test.request.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject;

public class JsonBodyObject extends JsonDTObject {

    @JsonProperty("A")
    private String a;

    @JsonProperty("B")
    private Integer b;

    @JsonProperty("C")
    private Boolean c;

    public String getA() {
        return a;
    }

    public JsonBodyObject setA(String a) {
        this.a = a;
        return this;
    }


    public Integer getB() {
        return b;
    }

    public JsonBodyObject setB(Integer b) {
        this.b = b;
        return this;
    }

    public Boolean getC() {
        return c;
    }

    public JsonBodyObject setC(Boolean c) {
        this.c = c;
        return this;
    }
}
