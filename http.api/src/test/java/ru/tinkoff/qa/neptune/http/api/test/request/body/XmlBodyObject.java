package ru.tinkoff.qa.neptune.http.api.test.request.body;

import ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://www.example.com")
public class XmlBodyObject extends XmlDTObject {

    @XmlElement(namespace = "http://www.test.com", name = "A1")
    private String a;

    @XmlElement(namespace = "http://www.test.com", name = "B1")
    private Integer b;

    @XmlElement(namespace = "http://www.test.com", name = "C1")
    private Boolean c;

    public String getA() {
        return a;
    }

    public XmlBodyObject setA(String a) {
        this.a = a;
        return this;
    }


    public Integer getB() {
        return b;
    }

    public XmlBodyObject setB(Integer b) {
        this.b = b;
        return this;
    }

    public Boolean getC() {
        return c;
    }

    public XmlBodyObject setC(Boolean c) {
        this.c = c;
        return this;
    }
}
