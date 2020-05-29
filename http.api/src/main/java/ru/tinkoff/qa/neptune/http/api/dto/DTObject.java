package ru.tinkoff.qa.neptune.http.api.dto;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

/**
 * This class represents formatted string bodies of sent http-requests/received http responses.
 */
public abstract class DTObject {

    public abstract String serialize();

    @Override
    public abstract String toString();


    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() == this.getClass()) {
            return reflectionEquals(this, obj);
        }
        return false;
    }
}
