package okuki.sample.mvvm.common.api.google.search;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CustomSearch {

    String BASE_URL = "https://www.googleapis.com/customsearch/";
    String CX = "002537579926136297275:rmahtn_mmwu";
    String API_KEY = "AIzaSyAeOtHY6xIZ4iYJYAmpryKvOHQjLyCsl8E";

    enum SearchType {
        image
    }

    @GET("v1")
    Observable<SearchResult> search(@Query("cx") String cx, @Query("key") String apiKey,
                                    @Query("searchType") SearchType type, @Query("q") String query);

}
