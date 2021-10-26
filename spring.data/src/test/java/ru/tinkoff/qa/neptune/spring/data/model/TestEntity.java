package ru.tinkoff.qa.neptune.spring.data.model;

import java.util.List;

public class TestEntity {

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
}
