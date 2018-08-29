package ru.tinkoff.qa.neptune.data.base.api.test;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "Books")
@DatastoreIdentity(strategy= IdGeneratorStrategy.INCREMENT)
public class Book extends PersistableObject {

    @PrimaryKey
    @Column(name = "ID")
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Author")
    private Author author;

    @Column(name = "YearOfFinishing")
    private int yearOfFinishing;

    public int getId() {
        return id;
    }

    public Book setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public Author getAuthor() {
        return author;
    }

    public Book setAuthor(Author author) {
        this.author = author;
        return this;
    }

    public int getYearOfFinishing() {
        return yearOfFinishing;
    }

    public Book setYearOfFinishing(int yearOfFinishing) {
        this.yearOfFinishing = yearOfFinishing;
        return this;
    }
}
