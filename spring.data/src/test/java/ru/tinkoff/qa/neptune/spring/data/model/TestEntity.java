package ru.tinkoff.qa.neptune.spring.data.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.copyOf;

public class TestEntity implements Cloneable {

    private Long id;

    private String name;

    private List<String> listData;

    private String[] arrayData;

    public Long getId() {
        return id;
    }

    public TestEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestEntity setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getListData() {
        return listData;
    }

    public TestEntity setListData(List<String> listData) {
        this.listData = listData;
        return this;
    }

    public String[] getArrayData() {
        return arrayData;
    }

    public TestEntity setArrayData(String[] arrayData) {
        this.arrayData = arrayData;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity that = (TestEntity) o;
        return id.equals(that.id)
                && name.equals(that.name)
                && listData.equals(that.listData)
                && Arrays.equals(arrayData, that.arrayData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, listData);
        result = 31 * result + Arrays.hashCode(arrayData);
        return result;
    }

    @Override
    public TestEntity clone() {
        try {
            TestEntity clone = (TestEntity) super.clone();
            clone.setArrayData(copyOf(getArrayData(), getArrayData().length));
            clone.setListData(List.copyOf(getListData()));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
