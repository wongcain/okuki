package okuki.sample.mvvm.common.api.swapi;

import com.squareup.moshi.Moshi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okuki.sample.mvvm.common.api.swapi.SwapiItem.Type;
import okuki.sample.mvvm.common.network.DataManager;
import okuki.sample.mvvm.common.network.NetworkModule.MyAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class SwapiDataManager extends DataManager<SwapiItem> {

    private final Swapi swapi;

    private SwapiItem.Type swapiItemType = Type.values()[0];

    @Inject
    public SwapiDataManager(OkHttpClient okHttpClient, Moshi moshi) {
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(Swapi.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create())
                .addConverterFactory( MoshiConverterFactory.create(moshi))
                .build();

        swapi = retrofit.create(Swapi.class);
    }

    @Override protected Observable<List<SwapiItem>> loadData( int limit, int offsetIgnored ) {
        return swapi.getItems(swapiItemType).map( Page::results );
    }

    public void setSwapiItemType( Type swapiItemType ) {
        this.swapiItemType = swapiItemType;
        load();
    }

}
