package okuki.sample.chucknorris;

import javax.inject.Inject;

import okuki.sample.common.mvp.Presenter;
import okuki.sample.common.rx.Errors;

public class ChuckNorrisPresenter extends Presenter<ChuckNorrisPresenter.Vu> {

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
                                ignore -> setJoke(),
                                Errors.log()
                        )
        );
        if (dataManager.getNumResults() == 0) {
            reload();
        } else {
            setJoke();
        }
    }

    void reload() {
        dataManager.load();
    }

    void setJoke() {
        getVu().setJokeHtml(dataManager.getResult(0).text);
    }

    interface Vu {
        void setJokeHtml(String jokeHtml);

        void setLoading(boolean isLoading);
    }

}
