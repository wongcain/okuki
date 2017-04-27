package okuki.sample.mvvm.common.network;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.jakewharton.rxrelay2.PublishRelay;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okuki.sample.mvvm.common.rx.Errors;


public abstract class DataManager<T> {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ObservableList<T> results = new ObservableArrayList<T>();
    private final AtomicBoolean loading = new AtomicBoolean(false);
    private final PublishRelay<Boolean> loadingStatus = PublishRelay.create();
    private int pageSize = DEFAULT_PAGE_SIZE;

    public Observable<Boolean> onLoadingStatus() {
        return loadingStatus;
    }

    public void load() {
        if (!loading.get()) {
            results.clear();
            loadMore();
        }
    }

    public void loadMore() {
        if (!loading.get()) {
            setIsLoading(true);
            doLoad(pageSize, results.size())
                    .subscribeOn( Schedulers.io())
                    .observeOn( AndroidSchedulers.mainThread())
                    .doOnError(error -> setIsLoading(false))
                    .subscribe(
                            list -> {
                                if (!list.isEmpty()) {
                                    results.addAll(list);
                                }
                                setIsLoading(false);
                            },
                            Errors.log());
        }
    }

    private void setIsLoading(boolean isLoading) {
        loading.set(isLoading);
        loadingStatus.accept(isLoading);
    }

    protected abstract Observable<List<T>> doLoad(int limit, int offset);

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ObservableList<T> getItems() {
        return results;
    }
}
