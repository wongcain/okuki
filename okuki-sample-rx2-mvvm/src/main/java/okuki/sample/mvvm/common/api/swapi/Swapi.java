package okuki.sample.mvvm.common.api.swapi;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Swapi {

    String BASE_URL = "http://swapi.co/api/";

    @GET("{type}")
    Observable<Page<SwapiItem>> getItems(@Path("type") SwapiItem.Type type);

}
