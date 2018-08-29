package ru.tinkoff.qa.neptune.data.base.api.test;


import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@PersistenceCapable(table = "Authors")
@DatastoreIdentity(strategy=IdGeneratorStrategy.INCREMENT)
public class Author extends PersistableObject {

    @PrimaryKey
    @Column(name = "Id")
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "BirthDate")
    private Date birthDate;

    @Column(name = "DeathDate")
    private Date deathDate;

    @Column(name = "BiographyDescription")
    private String biography;

    @Element(column="Author")
    private Collection<Book> books;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Collection<Book> getBooks() {
        return books;
    }
}
