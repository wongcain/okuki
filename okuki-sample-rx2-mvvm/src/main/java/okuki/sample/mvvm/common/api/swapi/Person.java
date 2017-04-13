package okuki.sample.mvvm.common.api.swapi;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Person {

    public static JsonAdapter<Person> typeAdapter(final Moshi moshi) {
        return new AutoValue_Person.MoshiJsonAdapter(moshi);
    }

    abstract String name();


}
