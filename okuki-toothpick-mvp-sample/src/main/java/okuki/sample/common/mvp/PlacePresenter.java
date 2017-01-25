package okuki.sample.common.mvp;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import okuki.Place;
import okuki.toothpick.PlaceScoper;
import toothpick.config.Module;

public abstract class PlacePresenter<P extends Place, V> extends Presenter<V> {

    private final Class<P> placeClass;

    public PlacePresenter() {
        placeClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        List<Module> modules = getModules();
        setScope(PlaceScoper.openPlaceScope(placeClass, modules.toArray(new Module[modules.size()])));
    }

    @Override
    @CallSuper
    protected void onDetachVu() {
        PlaceScoper.closePlaceScope(placeClass);
    }

    @NonNull
    protected List<Module> getModules() {
        return Collections.emptyList();
    }

}
