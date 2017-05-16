package okuki.sample.mvvm.common.mvvm;

import android.view.View;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okuki.sample.mvvm.App;

public abstract class BaseViewModel implements ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseViewModel() {
        App.inject(this);
    }

    @Override
    public void onAttach() {
    }

    @Override
    public void onDetach() {
    }

    public final void onViewAttachedToWindow(View view) {
        onAttach();
    }

    public final void onViewDetachedFromWindow(View view) {
        compositeDisposable.clear();
        onDetach();
    }

    protected void addToAutoDispose(Disposable... disposables) {
        compositeDisposable.addAll(disposables);
    }

}
