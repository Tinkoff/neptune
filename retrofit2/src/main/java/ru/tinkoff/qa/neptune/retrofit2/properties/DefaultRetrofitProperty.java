package ru.tinkoff.qa.neptune.retrofit2.properties;

import retrofit2.Retrofit;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier;

@PropertyDescription(description = {
        "Defines a class whose objects return ",
        "instances of ru.tinkoff.qa.neptune.retrofit2.service.setup.DefaultRetrofitBuilderSupplier"}, section = "Retrofit")
@PropertyDefaultValue("ru.tinkoff.qa.neptune.retrofit2.service.setup.DefaultRetrofitBuilderSupplier")
@PropertyName("DEFAULT_RETROFIT")
public final class DefaultRetrofitProperty implements ObjectByClassPropertySupplier<RetrofitBuilderSupplier> {

    /**
     * This instance reads value of the property {@code 'DEFAULT_RETROFIT'} and returns an {@link Retrofit}
     */
    public static final DefaultRetrofitProperty DEFAULT_RETROFIT_PROPERTY = new DefaultRetrofitProperty();

    private DefaultRetrofitProperty() {
        super();
    }
}
