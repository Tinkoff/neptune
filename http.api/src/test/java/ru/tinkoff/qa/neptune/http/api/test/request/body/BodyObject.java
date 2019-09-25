package ru.tinkoff.qa.neptune.http.api.test.request.body;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import java.util.Objects;

import static java.util.Objects.nonNull;

@XmlType(namespace = "http://www.example.com")
public class BodyObject {

    @SerializedName("A")
    @XmlElement(namespace = "http://www.test.com", name = "A1")
    private String a;

    @SerializedName("B")
    @XmlElement(namespace = "http://www.test.com", name = "B1")
    private Integer b;

    @SerializedName("C")
    @XmlElement(namespace = "http://www.test.com", name = "C1")
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

    @Override
    public boolean equals(Object o) {
        return nonNull(o) && BodyObject.class.equals(o.getClass())
                && Objects.equals(this.a, ((BodyObject) o).a)
                && Objects.equals(this.b, ((BodyObject) o).b)
                && Objects.equals(this.c, ((BodyObject) o).c);
    }
}
