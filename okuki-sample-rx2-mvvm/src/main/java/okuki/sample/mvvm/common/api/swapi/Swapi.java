package okuki.sample.mvvm.common.api.swapi;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Swapi {

    String BASE_URL = "http://swapi.co/api/";

    @GET("people/")
    Observable<Page<Person>> getPeople( @Query("page") int page);

    @GET("people/{id}")
    Observable<Person> getPerson( @Path("id") int id);

}
