package okuki;

public abstract class GlobalListener implements Okuki.Listener<Place> {

    @Override
    public abstract void onPlace(Place place);

    @Override
    public void onError(Throwable error) {
        throw new RuntimeException("Exception in " + this, error);
    }

}
