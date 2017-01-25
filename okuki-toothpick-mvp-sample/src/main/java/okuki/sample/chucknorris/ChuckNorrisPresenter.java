package okuki.sample.chucknorris;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okuki.sample.chucknorris.icndb.IcndbDataManager;
import okuki.sample.common.mvp.PlacePresenter;
import okuki.sample.common.rx.Errors;
import toothpick.config.Module;

public class ChuckNorrisPresenter extends PlacePresenter<ChuckNorrisPlace, ChuckNorrisPresenter.Vu> {

    @Inject
    IcndbDataManager icndbDataManager;

    @Override
    protected void onVuAttached() {
        super.onVuAttached();
        addSubscriptions(
                icndbDataManager.getJoke()
                        .subscribe(joke -> getVu().setJokeHtml(joke),
                                Errors.log()
                        )
        );
    }

    @NonNull
    @Override
    protected List<Module> getModules() {
        return Collections.singletonList(new ChuckNorrisModule());
    }

    interface Vu {
        void setJokeHtml(String jokeHtml);
    }

}
