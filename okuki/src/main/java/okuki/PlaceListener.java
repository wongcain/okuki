package okuki;

import java.lang.reflect.ParameterizedType;

public abstract class PlaceListener<P extends Place> implements Okuki.Listener<P> {

    private final Class<P> placeClass;

    public PlaceListener() {
        //noinspection ConstantConditions
        placeClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<P> getPlaceClass() {
        return placeClass;
    }

    @Override
    public abstract void onPlace(P place);

    @Override
    public void onError(Throwable error) {
        throw new RuntimeException("Exception in " + this, error);
    }

}
