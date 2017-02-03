package okuki.sample.common.api.icndb;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface IcndbApi {

    String BASE_URL = "http://api.icndb.com";

    @GET("/jokes/random/{num}")
    Observable<Result<IcndbResult>> getRandomJokes(@Path("num") int numJokes);

}
