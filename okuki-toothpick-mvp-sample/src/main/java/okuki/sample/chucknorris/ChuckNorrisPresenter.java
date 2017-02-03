package okuki.sample.chucknorris;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okuki.sample.common.mvp.PlacePresenter;
import okuki.sample.common.rx.Errors;
import toothpick.config.Module;

public class ChuckNorrisPresenter extends PlacePresenter<ChuckNorrisPlace, ChuckNorrisPresenter.Vu> {

    @Inject
    ChuckNorrisDataManager dataManager;

    @Override
    protected void onVuAttached() {
        super.onVuAttached();
        addSubscriptions(
                dataManager.onLoadingStatus()
                        .subscribe(
                                isLoading -> getVu().setLoading(isLoading),
                                Errors.log()
                        ),
                dataManager.onListUpdated()
                        .filter(aVoid -> dataManager.getNumResults() > 0)
                        .subscribe(
                                ignore -> getVu().setJokeHtml(dataManager.getResult(0).text),
                                Errors.log()
                        )
        );
        dataManager.load();
    }

    void reload(){
        dataManager.load();
    }

    @NonNull
    @Override
    protected List<Module> getModules() {
        return Collections.singletonList(new ChuckNorrisModule());
    }

    interface Vu {
        void setJokeHtml(String jokeHtml);

        void setLoading(boolean isLoading);
    }

}
