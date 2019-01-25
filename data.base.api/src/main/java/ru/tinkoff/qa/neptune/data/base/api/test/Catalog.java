package ru.tinkoff.qa.neptune.data.base.api.test;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "Catalog")
public class Catalog extends PersistableObject {

    @PrimaryKey
    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "YearOfPublishing")
    private int yearOfPublishing;

    @Persistent(column = "BookId")
    private Book book;

    @Persistent(column = "PublisherId")
    private Publisher publisher;

    public String getIsbn() {
        return isbn;
    }

    public Catalog setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public int getYearOfPublishing() {
        return yearOfPublishing;
    }

    public Catalog setYearOfPublishing(int yearOfPublishing) {
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
