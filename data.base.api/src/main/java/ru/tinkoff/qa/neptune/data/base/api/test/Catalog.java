package ru.tinkoff.qa.neptune.data.base.api.test;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "Catalog", objectIdClass = CatalogCompositeKey.class)
public class Catalog extends PersistableObject {

    @PrimaryKey
    @Column(name = "YearOfPublishing")
    private Integer yearOfPublishing;

    @PrimaryKey
    @Persistent(column = "BookId")
    private Book book;

    @PrimaryKey
    @Persistent(column = "PublisherId")
    private Publisher publisher;

    public Integer getYearOfPublishing() {
        return yearOfPublishing;
    }

    public Catalog setYearOfPublishing(Integer yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
        return this;
    }

    public Book getBook() {
        return book;
    }

    public Catalog setBook(Book book) {
        this.book = book;
        return this;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Catalog setPublisher(Publisher publisher) {
        this.publisher = publisher;
        return this;
    }
}
