package okuki.sample.common.api.giphy;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GiphyApi {

    String BASE_URL = "http://api.giphy.com";
    String PUBLIC_API_KEY = "dc6zaTOxFJmzC";

    @GET("/v1/gifs/search?api_key=" + PUBLIC_API_KEY)
    Observable<Result<SearchResult>> search(@Query("q") String query, @Query("limit") int limit, @Query("offset") int offset);

}
