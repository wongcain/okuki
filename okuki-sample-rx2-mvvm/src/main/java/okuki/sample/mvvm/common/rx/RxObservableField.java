package okuki.sample.mvvm.common.rx;

import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import io.reactivex.Observable;

public class RxObservableField {

    private RxObservableField() {
        throw new AssertionError("No instances.");
    }

    public static <T> Observable<T> toObservable(@NonNull final ObservableField<T> field) {

        return Observable.create(e -> {
            T initialValue = field.get();
            if (initialValue != null) {
                e.onNext(initialValue);
            }
            final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    e.onNext(field.get());
                }
            };
            field.addOnPropertyChangedCallback(callback);
            e.setCancellable(() -> field.removeOnPropertyChangedCallback(callback));
        });
    }
}
