package ru.tinkoff.qa.neptune.data.base.test.persistable.object.equality;

import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

public class EqualityObjectTest {

    @Test //compare object with null-valued fields and null
    public void checkEqualToNull() {
        assertThat(new PersistableObjectOne().equals(null), is(false));
    }

    @Test //compare object with null-valued fields and null
    public void checkEqualityToPersistableOfDifferentClasses() {
        assertThat(new PersistableObjectOne().equals(new PersistableObjectTwo()), is(false));
        assertThat(new PersistableObjectTwo().equals(new PersistableObjectOne()), is(false));
        assertThat(new PersistableObjectTwo().equals(new PersistableObjectThree()), is(false));
        assertThat(new PersistableObjectTwo().equals(new UnpersistableObject()), is(false));
    }

    @Test
    public void checkEqualityToEmptyPersistableOfSameClass() {
        assertThat(new PersistableObjectOne().equals(new PersistableObjectOne()), is(true));
    }

    @Test
    public void checkEqualityToPersistableOfSameClassFilledByDifferentValues() {
        assertThat(new PersistableObjectOne().setBooleanField(true).setIntField(4).setObjectListField(List.of(1))
                        .equals(new PersistableObjectOne().setBooleanField(false).setIntField(5).setObjectListField(List.of(1, 2))),
                is(false));
    }

    @Test
    public void checkEqualityToPersistableOfSameClassFilledBySameValues() {
        assertThat(new PersistableObjectOne().setBooleanField(true).setIntField(4).setObjectListField(List.of(1))
                        .equals(new PersistableObjectOne().setBooleanField(true).setIntField(4).setObjectListField(List.of(1))),
                is(true));
    }

    @Test
    public void checkEqualityToPersistableOfAssignableClassesFilledBySameValues() {
        assertThat(new PersistableObjectOne().setBooleanField(true).setIntField(4).setObjectListField(List.of(1))
                        .equals(new PersistableObjectTwo().setLongField(100)
                                .setCharField('a')
                                .setBooleanField(true)
                                .setIntField(4)
                                .setObjectListField(List.of(1))),
                is(false));
    }

    @Test
    public void checkEqualityToPersistableOfNotAssignableClassesFilledBySameValues() {
        assertThat(new PersistableObjectTwo().setLongField(100)
                .setCharField('a')
                .setBooleanField(true)
                .setIntField(4)
                .setObjectListField(List.of(1)).equals(new PersistableObjectThree().setLongField(100)
                                .setCharField('a')
                                .setBooleanField(true)
                                .setIntField(4)
                                .setObjectListField(List.of(1))),
                is(false));
    }

    @Test
    public void checkEqualityToNotPersistableFilledBySameValues() {
        assertThat(new PersistableObjectThree().setLongField(100)
                        .setCharField('a')
                        .setBooleanField(true)
                        .setIntField(4)
                        .setObjectListField(List.of(1)).equals(new UnpersistableObject().setLongField(100)
                                .setCharField('a')
                                .setBooleanField(true)
                                .setIntField(4)
                                .setObjectListField(List.of(1))),
                is(false));
    }
}
