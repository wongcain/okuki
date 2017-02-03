package okuki.sample.common.network;

import android.util.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import okuki.sample.common.rx.Errors;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public abstract class DataManager<T> {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private final List<T> results = new ArrayList<>();
    private final AtomicBoolean loading = new AtomicBoolean(false);
    private final PublishSubject<Boolean> loadingStatus = PublishSubject.create();
    private final PublishSubject<Range<Integer>> rangeInserted = PublishSubject.create();
    private final PublishSubject<Void> listUpdated = PublishSubject.create();
    private int pageSize = DEFAULT_PAGE_SIZE;

    public Observable<Boolean> onLoadingStatus() {
        return loadingStatus;
    }

    public Observable<Range<Integer>> onRangeInserted() {
        return rangeInserted;
    }

    public Observable<Void> onListUpdated() {
        return listUpdated;
    }

    public void load() {
        if (!loading.get()) {
            results.clear();
            loadMore();
        }
    }

    public void loadMore() {
        if (!loading.get()) {
            setLoading(true);
            loadData(pageSize, results.size())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(error -> setLoading(false))
                    .subscribe(
                            list -> {
                                if (!list.isEmpty()) {
                                    int start = results.size();
                                    int end = start + list.size();
                                    results.addAll(list);
                                    if (start > 0) {
                                        rangeInserted.onNext(new Range<>(start, end));
                                    } else {
                                        listUpdated.onNext(null);
                                    }
                                }
                                setLoading(false);
                            },
                            Errors.log());
        }
    }

    private void setLoading(boolean isLoading) {
        loading.set(isLoading);
        loadingStatus.onNext(isLoading);
    }

    protected abstract Observable<List<T>> loadData(int limit, int offset);

    public int getNumResults() {
        return results.size();
    }

    public T getResult(int index) {
        return results.get(index);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }
}
