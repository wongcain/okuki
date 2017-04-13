package okuki.sample.mvvm.common.api.swapi;

import com.squareup.moshi.Moshi;

import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class SwapiFactory {

    public static Swapi getSwapi(){
        final Moshi moshi = new Moshi.Builder().add(MyAdapterFactory.create()).build();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(Swapi.BASE_URL)
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create())
                .addConverterFactory( MoshiConverterFactory.create(moshi))
                .build();

        return retrofit.create(Swapi.class);
    }

    public static void main(String[] args){
        Swapi swapi = getSwapi();
        swapi.getPeople(1).subscribe( new Consumer<Page<Person>>() {
            @Override public void accept( Page<Person> people ) throws Exception {
                for(Person person: people.results()){
                    System.out.println("Got person: " + person.name());
                }
            }
        } );

    }
}
