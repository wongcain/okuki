package okuki.sample.mvvm.common.mvvm;

import android.support.annotation.CallSuper;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okuki.sample.mvvm.App;

public abstract class BaseViewModel implements ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @CallSuper
    @Override
    public void onAttach() {
        App.inject(this);
    }

    @CallSuper
    @Override
    public void onDetach() {
        compositeDisposable.clear();
    }

    protected void addToAutoDispose(Disposable... disposables) {
        compositeDisposable.addAll(disposables);
    }

}
