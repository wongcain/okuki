package okuki.sample.chucknorris.icndb;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import rx.Observable;

public interface IcndbApi {

    String BASE_URL = "http://api.icndb.com";

    @GET("/jokes/random")
    Observable<Result<JokeResult>> getRandomJoke();

}
