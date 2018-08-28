package ru.tinkoff.qa.neptune.data.base.api.test;

import javax.annotation.Generated;
import javax.jdo.query.*;
import org.datanucleus.api.jdo.query.*;

@Generated(value="org.datanucleus.jdo.query.JDOQueryProcessor")
public class QAuthor extends PersistableExpressionImpl<Author> implements PersistableExpression<Author>
{
    public static final QAuthor jdoCandidate = candidate("this");

    public static QAuthor candidate(String name)
    {
        return new QAuthor(null, name, 5);
    }

    public static QAuthor candidate()
    {
        return jdoCandidate;
    }

    public static QAuthor parameter(String name)
    {
        return new QAuthor(Author.class, name, ExpressionType.PARAMETER);
    }

    public static QAuthor variable(String name)
    {
        return new QAuthor(Author.class, name, ExpressionType.VARIABLE);
    }

    public final NumericExpression<Integer> id;
    public final StringExpression firstName;
    public final StringExpression lastName;
    public final DateTimeExpression birthDate;
    public final DateTimeExpression deathDate;
    public final StringExpression biography;
    public final CollectionExpression books;

    public QAuthor(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.id = new NumericExpressionImpl<Integer>(this, "id");
        this.firstName = new StringExpressionImpl(this, "firstName");
        this.lastName = new StringExpressionImpl(this, "lastName");
        this.birthDate = new DateTimeExpressionImpl(this, "birthDate");
        this.deathDate = new DateTimeExpressionImpl(this, "deathDate");
        this.biography = new StringExpressionImpl(this, "biography");
        this.books = new CollectionExpressionImpl(this, "books");
    }

    public QAuthor(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.id = new NumericExpressionImpl<Integer>(this, "id");
        this.firstName = new StringExpressionImpl(this, "firstName");
        this.lastName = new StringExpressionImpl(this, "lastName");
        this.birthDate = new DateTimeExpressionImpl(this, "birthDate");
        this.deathDate = new DateTimeExpressionImpl(this, "deathDate");
        this.biography = new StringExpressionImpl(this, "biography");
        this.books = new CollectionExpressionImpl(this, "books");
    }
}
