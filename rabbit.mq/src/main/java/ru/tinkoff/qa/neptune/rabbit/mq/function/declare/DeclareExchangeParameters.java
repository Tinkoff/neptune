package ru.tinkoff.qa.neptune.rabbit.mq.function.declare;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.valueOf;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Exchange '{exchange}'")
public final class DeclareExchangeParameters extends DeclareParameters<DeclareExchangeParameters> {

    @DescriptionFragment("exchange")
    private final String exchange;

    @StepParameter("type")
    private String type = "direct";

    @StepParameter("autoDelete")
    private boolean autoDelete;

    @StepParameter("internal")
    private boolean internal;

    @StepParameter(value = "passive")
    private Boolean passive;

    public DeclareExchangeParameters(String exchange) {
        checkArgument(isNotBlank(exchange), "Name of the exchange should be defined");
        this.exchange = exchange;
    }

    DeclareExchangeParameters setType(String type) {
        checkArgument(isNotBlank(type), "Name of the type of exchange should be defined");
        this.type = type;
        return this;
    }

    /**
     * Defines the type of exchange
     *
     * @param type is a type of exchange
     * @return self-reference
     */
    public DeclareExchangeParameters type(String type) {
        return setType(type);
    }

    /**
     * Defines the type of exchange
     *
     * @param type is a type of exchange
     * @return self-reference
     */
    public DeclareExchangeParameters type(BuiltinExchangeType type) {
        checkArgument(nonNull(type), "Type of exchange should be defined");
        return type(type.getType());
    }

    /**
     * Makes new exchange autoDelete
     *
     * @return self-reference
     */
    public DeclareExchangeParameters autoDelete() {
        this.autoDelete = true;
        return this;
    }

    /**
     * Makes new exchange autoDelete
     *
     * @return self-reference
     */
    public DeclareExchangeParameters internal() {
        this.internal = true;
        return this;
    }

    /**
     * Forces a new exchange to be declared passively.
     * It makes ignore values of fields which represent such properties as
     * type, autoDelete, internal, durable and additional arguments
     *
     * @return self-reference
     */
    public DeclareExchangeParameters passive() {
        this.passive = true;
        return this;
    }

    @Override
    public Map<String, String> getParameters() {
        var result = new LinkedHashMap<String, String>();
        if (passive != null) {
            result.put(translate(this.getClass().getDeclaredField("passive")), valueOf(true));
        } else {
            result.putAll(super);
        }
        return super.getParameters();
    }

    @Override
    public void declare(Channel channel) {

    }
}
