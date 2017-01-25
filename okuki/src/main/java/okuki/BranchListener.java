package okuki;

import java.lang.reflect.ParameterizedType;

public abstract class BranchListener<P extends Place> implements Okuki.Listener<Place> {

    private final Class<P> branchClass;

    public BranchListener() {
        //noinspection ConstantConditions
        branchClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<P> getBranchClass() {
        return branchClass;
    }

    @Override
    public abstract void onPlace(Place place);

    @Override
    public void onError(Throwable error) {
        throw new RuntimeException("Exception in " + this, error);
    }

}
