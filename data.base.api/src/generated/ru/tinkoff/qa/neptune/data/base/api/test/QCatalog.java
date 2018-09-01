package ru.tinkoff.qa.neptune.data.base.api.test;

import javax.annotation.Generated;
import javax.jdo.query.*;
import org.datanucleus.api.jdo.query.*;

@Generated(value="org.datanucleus.jdo.query.JDOQueryProcessor")
public class QCatalog extends PersistableExpressionImpl<Catalog> implements PersistableExpression<Catalog>
{
    public static final QCatalog jdoCandidate = candidate("this");

    public static QCatalog candidate(String name)
    {
        return new QCatalog(null, name, 5);
    }

    public static QCatalog candidate()
    {
        return jdoCandidate;
    }

    public static QCatalog parameter(String name)
    {
        return new QCatalog(Catalog.class, name, ExpressionType.PARAMETER);
    }

    public static QCatalog variable(String name)
    {
        return new QCatalog(Catalog.class, name, ExpressionType.VARIABLE);
    }

    public final NumericExpression<Integer> recordId;
    public final StringExpression isbn;
    public final NumericExpression<Integer> yearOfPublishing;
    public final ru.tinkoff.qa.neptune.data.base.api.test.QBook book;
    public final ru.tinkoff.qa.neptune.data.base.api.test.QPublisher publisher;

    public QCatalog(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.recordId = new NumericExpressionImpl<Integer>(this, "recordId");
        this.isbn = new StringExpressionImpl(this, "isbn");
        this.yearOfPublishing = new NumericExpressionImpl<Integer>(this, "yearOfPublishing");
        if (depth > 0)
        {
            this.book = new ru.tinkoff.qa.neptune.data.base.api.test.QBook(this, "book", depth-1);
        }
        else
        {
            this.book = null;
        }
        if (depth > 0)
        {
            this.publisher = new ru.tinkoff.qa.neptune.data.base.api.test.QPublisher(this, "publisher", depth-1);
        }
        else
        {
            this.publisher = null;
        }
    }

    public QCatalog(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.recordId = new NumericExpressionImpl<Integer>(this, "recordId");
        this.isbn = new StringExpressionImpl(this, "isbn");
        this.yearOfPublishing = new NumericExpressionImpl<Integer>(this, "yearOfPublishing");
        this.book = new ru.tinkoff.qa.neptune.data.base.api.test.QBook(this, "book", 5);
        this.publisher = new ru.tinkoff.qa.neptune.data.base.api.test.QPublisher(this, "publisher", 5);
    }
}
