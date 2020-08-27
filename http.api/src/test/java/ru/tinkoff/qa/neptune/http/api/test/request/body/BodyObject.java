package ru.tinkoff.qa.neptune.http.api.test.request.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://www.example.com")
public class BodyObject extends MappedObject {

    @XmlElement(namespace = "http://www.test.com", name = "A1")
    @JsonProperty("A")
    private String a;

    @XmlElement(namespace = "http://www.test.com", name = "B1")
    @JsonProperty("B")
    private Integer b;

    @XmlElement(namespace = "http://www.test.com", name = "C1")
    @JsonProperty("C")
    private Boolean c;

    public String getA() {
        return a;
    }

    public BodyObject setA(String a) {
        this.a = a;
        return this;
    }


    public Integer getB() {
        return b;
    }

    public BodyObject setB(Integer b) {
        this.b = b;
        return this;
    }

    public Boolean getC() {
        return c;
    }

    public BodyObject setC(Boolean c) {
        this.c = c;
        return this;
    }
}
