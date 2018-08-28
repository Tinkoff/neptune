package ru.tinkoff.qa.neptune.data.base.api.test;

import javax.annotation.Generated;
import javax.jdo.query.*;
import org.datanucleus.api.jdo.query.*;

@Generated(value="org.datanucleus.jdo.query.JDOQueryProcessor")
public class QBook extends PersistableExpressionImpl<Book> implements PersistableExpression<Book>
{
    public static final QBook jdoCandidate = candidate("this");

    public static QBook candidate(String name)
    {
        return new QBook(null, name, 5);
    }

    public static QBook candidate()
    {
        return jdoCandidate;
    }

    public static QBook parameter(String name)
    {
        return new QBook(Book.class, name, ExpressionType.PARAMETER);
    }

    public static QBook variable(String name)
    {
        return new QBook(Book.class, name, ExpressionType.VARIABLE);
    }

    public final NumericExpression<Integer> id;
    public final StringExpression name;
    public final ru.tinkoff.qa.neptune.data.base.api.test.QAuthor author;
    public final NumericExpression<Integer> yearOfFinishing;

    public QBook(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.id = new NumericExpressionImpl<Integer>(this, "id");
        this.name = new StringExpressionImpl(this, "name");
        if (depth > 0)
        {
            this.author = new ru.tinkoff.qa.neptune.data.base.api.test.QAuthor(this, "author", depth-1);
        }
        else
        {
            this.author = null;
        }
        this.yearOfFinishing = new NumericExpressionImpl<Integer>(this, "yearOfFinishing");
    }

    public QBook(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.id = new NumericExpressionImpl<Integer>(this, "id");
        this.name = new StringExpressionImpl(this, "name");
        this.author = new ru.tinkoff.qa.neptune.data.base.api.test.QAuthor(this, "author", 5);
        this.yearOfFinishing = new NumericExpressionImpl<Integer>(this, "yearOfFinishing");
    }
}
