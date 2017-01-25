package okuki.sample.welcome;

import android.content.res.Resources;

import java.util.Date;

import javax.inject.Inject;

import okuki.sample.R;
import okuki.sample.common.mvp.PlacePresenter;

public class WelcomePresenter extends PlacePresenter<WelcomePlace, WelcomePresenter.Vu> {

    @Inject
    Resources resources;

    @Override
    protected void onVuAttached() {
        getVu().setWelcomeMessage(resources.getString(R.string.welcome_msg, new Date()));
    }

    interface Vu {
        void setWelcomeMessage(String msg);
    }

}
