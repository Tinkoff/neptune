package ru.tinkoff.qa.neptune.data.base.test.persistable.object;

import com.google.gson.Gson;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ToStringTest {

    @Test
    public void toStringTest() {
        PersistableObjectThree p1 = new PersistableObjectThree().setLongField(100)
                .setCharField('b')
                .setBooleanField(true)
                .setIntField(4)
                .setObjectListField(List.of(1.5, 2.1, 3.2));
        String stringValue = p1.toString();
        PersistableObjectThree p2 = new Gson().fromJson(stringValue, PersistableObjectThree.class);
        assertThat(p1.toString(), equalTo(p2.toString()));
    }
}
