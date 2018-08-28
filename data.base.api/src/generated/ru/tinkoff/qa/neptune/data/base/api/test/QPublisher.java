package ru.tinkoff.qa.neptune.data.base.api.test;

import javax.annotation.Generated;
import javax.jdo.query.*;
import org.datanucleus.api.jdo.query.*;

@Generated(value="org.datanucleus.jdo.query.JDOQueryProcessor")
public class QPublisher extends PersistableExpressionImpl<Publisher> implements PersistableExpression<Publisher>
{
    public static final QPublisher jdoCandidate = candidate("this");

    public static QPublisher candidate(String name)
    {
        return new QPublisher(null, name, 5);
    }

    public static QPublisher candidate()
    {
        return jdoCandidate;
    }

    public static QPublisher parameter(String name)
    {
        return new QPublisher(Publisher.class, name, ExpressionType.PARAMETER);
    }

    public static QPublisher variable(String name)
    {
        return new QPublisher(Publisher.class, name, ExpressionType.VARIABLE);
    }

    public final NumericExpression<Integer> id;
    public final StringExpression name;

    public QPublisher(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.id = new NumericExpressionImpl<Integer>(this, "id");
        this.name = new StringExpressionImpl(this, "name");
    }

    public QPublisher(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.id = new NumericExpressionImpl<Integer>(this, "id");
        this.name = new StringExpressionImpl(this, "name");
    }
}
