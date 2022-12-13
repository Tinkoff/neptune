package ru.tinkoff.qa.neptune.kafka;

import com.google.common.base.Objects;

public class DraftDto {
    private String name;

    public String getName() {
        return name;
    }

    public DraftDto setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DraftDto draftDto = (DraftDto) o;
        return Objects.equal(name, draftDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
