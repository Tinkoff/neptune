package ru.tinkoff.qa.neptune.retrofit2.properties;

import retrofit2.Retrofit;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitSupplier;

@PropertyDescription(description = {
        "Defines a class whose objects return prepared",
        "instances of retrofit2.Retrofit"}, section = "Retrofit")
@PropertyDefaultValue("ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitSupplier.DefaultRetrofitSupplier")
@PropertyName("DEFAULT_RETROFIT")
public final class DefaultRetrofitProperty implements ObjectPropertySupplier<Retrofit, RetrofitSupplier> {

    /**
     * This instance reads value of the property {@code 'DEFAULT_RETROFIT'} and returns an {@link Retrofit}
     */
    public static final DefaultRetrofitProperty DEFAULT_RETROFIT_PROPERTY = new DefaultRetrofitProperty();

    private DefaultRetrofitProperty() {
        super();
    }
}
