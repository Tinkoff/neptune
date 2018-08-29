package ru.tinkoff.qa.neptune.data.base.api.test;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "Catalog")
@DatastoreIdentity(strategy=IdGeneratorStrategy.INCREMENT)
public class Catalog extends PersistableObject {

    @PrimaryKey
    @Column(name = "RecordID")
    private int recordId;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "YearOfPublishing")
    private int yearOfPublishing;

    @Persistent(column = "BookId")
    private Book book;

    @Persistent(column = "PublisherId")
    private Publisher publisher;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYearOfPublishing() {
        return yearOfPublishing;
    }

    public void setYearOfPublishing(int yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
