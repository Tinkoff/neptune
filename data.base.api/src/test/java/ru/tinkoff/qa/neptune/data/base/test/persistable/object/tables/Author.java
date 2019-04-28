package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;
import java.util.Collection;
import java.util.Date;

@PersistenceCapable(table = "Authors")
public class Author extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
    @Column(name = "Id")
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "BirthDate", jdbcType = "TIMESTAMP")
    private Date birthDate;

    @Column(name = "DeathDate", jdbcType = "TIMESTAMP")
    private Date deathDate;

    @Column(name = "BiographyDescription", length = 10000)
    private String biography;

    @Persistent
    @Element(column="Author")
    transient Collection<Book> books;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Author setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Author setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Author setBirthDate(java.util.Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public Author setDeathDate(java.util.Date deathDate) {
        this.deathDate = deathDate;
        return this;
    }

    public String getBiography() {
        return biography;
    }

    public Author setBiography(String biography) {
        this.biography = biography;
        return this;
    }

    public Collection<Book> getBooks() {
        return books;
    }

    public void setId(int id) {
        this.id = id;
    }
}
