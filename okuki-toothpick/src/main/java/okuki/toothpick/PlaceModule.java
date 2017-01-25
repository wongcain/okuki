package okuki.toothpick;

import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;

import okuki.Place;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class PlaceModule<P extends Place> extends Module {

    @Inject
    public PlaceModule() {
        final Class<P> placeClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Scope scope = PlaceScoper.openParentScope(placeClass);
        Toothpick.inject(this, scope);
    }

}
