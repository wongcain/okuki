package okuki;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Okuki {

    interface Listener<P extends Place> {

        void onPlace(P place);

        void onError(Throwable error);

    }

    private static Okuki defaultInstance;
    private Deque<Place> history = new ArrayDeque<>();
    private Place currentPlace;
    private List<Listener> globalListeners = new ArrayList<>();
    private Map<Class<? extends Place>, List<Listener>> placeListeners = new HashMap<>();
    private Map<Class<? extends Place>, List<Listener>> branchListeners = new HashMap<>();

    public Okuki() {
    }

    public static Okuki getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new Okuki();
        }
        return defaultInstance;
    }

    /**
     * Use this method to request navigation to a Place, with default history action ADD.
     *
     * @param place
     */
    public void gotoPlace(Place place) {
        gotoPlace(place, HistoryAction.ADD);
    }

    /**
     * Use this method to request navigation to a Place and perform the specified {@link HistoryAction}
     *
     * @param place
     * @param historyAction
     */
    public void gotoPlace(Place place, HistoryAction historyAction) {
        switch (historyAction) {
            case ADD:
                history.push(place);
                callPlace(place);
                break;
            case REPLACE_TOP:
                if (!history.isEmpty()) {
                    history.pop();
                }
                history.push(place);
                callPlace(place);
                break;
            case TRY_BACK_TO_SAME:
                if (!goBackToPlaceEqual(place)) {
                    history.push(place);
                    callPlace(place);
                }
                break;
            case TRY_BACK_TO_SAME_TYPE:
                if (!goBackToPlaceByType(place)) {
                    history.push(place);
                    callPlace(place);
                }
                break;
            case NONE:
                callPlace(place);
                break;
        }

    }

    /**
     * Attempts to go back to one previous Place in the history. If successful, returns true.
     * If not (i.e. history is empty) returns false.
     */
    public boolean goBack() {
        boolean success = false;
        if (history.size() > 1) {
            history.pop();
            callPlace(history.peek());
            success = true;
        }
        return success;
    }

    /**
     * Returns the current Place (top of the history stack).  If the stack is empty, returns null.
     */
    public Place getCurrentPlace() {
        return currentPlace;
    }

    /**
     * Registers a {@link GlobalListener} that will receive all {@link Place} events. Upon
     * registration, the listener will receive the current Place (unless it is null).
     *
     * @param listener
     */
    public void addGlobalListener(GlobalListener listener) {
        globalListeners.add(listener);
        if (currentPlace != null) {
            notifyListener(listener, currentPlace);
        }
    }

    /**
     * Unregisters a {@link GlobalListener}
     *
     * @param listener
     */
    public void removeGlobalListener(GlobalListener listener) {
        globalListeners.remove(listener);
    }

    /**
     * Registers a {@link PlaceListener} that will receive all events for a given {@link Place} by
     * type/class. Upon registration, the listener will receive the current Place if it is of the
     * configured type.
     *
     * @param listener
     */
    public void addPlaceListener(PlaceListener listener) {
        Class<? extends Place> placeClass = listener.getPlaceClass();
        if (!placeListeners.containsKey(placeClass)) {
            placeListeners.put(placeClass, new ArrayList<Listener>());
        }
        placeListeners.get(placeClass).add(listener);
        if (currentPlace != null && currentPlace.getClass() == placeClass) {
            notifyListener(listener, currentPlace);
        }
    }

    /**
     * Unregisters a {@link PlaceListener}
     *
     * @param listener
     */
    public void removePlaceListener(PlaceListener listener) {
        Class<? extends Place> placeClass = listener.getPlaceClass();
        if (placeListeners.containsKey(placeClass)) {
            placeListeners.get(placeClass).remove(listener);
        }
    }

    /**
     * Registers a {@link BranchListener} that will receive all events for a given {@link Place} by
     * type/class, as well as descendant Place classes as defined via {@link PlaceConfig} annotations.
     * Upon registration, the listener will receive the current Place if it is in configured hierarchy.
     *
     * @param listener
     */
    public void addBranchListener(final BranchListener listener) {
        Class<? extends Place> branchClass = listener.getBranchClass();
        if (!branchListeners.containsKey(branchClass)) {
            branchListeners.put(branchClass, new ArrayList<Listener>());
        }
        branchListeners.get(branchClass).add(listener);
        if (currentPlace != null && currentPlace.getHierarchy().contains(branchClass)) {
            notifyListener(listener, currentPlace);
        }
    }

    /**
     * Unregisters a {@link BranchListener}
     *
     * @param listener
     */
    public void removeBranchListener(BranchListener listener) {
        Class<? extends Place> branchClass = listener.getBranchClass();
        if (branchListeners.containsKey(branchClass)) {
            branchListeners.get(branchClass).remove(listener);
        }
    }

    /**
     * Provides direct access to Place history stack
     */
    public Deque<Place> getHistory() {
        return history;
    }

    private boolean goBackToPlaceEqual(Place place) {
        boolean success = false;
        int i = 0;
        for (Place p : history) {
            if (p.equals(place)) {
                for (int j = 0; j < i + 1; j++) {
                    history.pop();
                }
                history.push(place);
                callPlace(place);
                success = true;
                break;
            }
            i++;
        }
        return success;
    }

    private boolean goBackToPlaceByType(Place place) {
        boolean success = false;
        int i = 0;
        for (Place p : history) {
            if (p.getClass().equals(place.getClass())) {
                for (int j = 0; j < i + 1; j++) {
                    history.pop();
                }
                history.push(place);
                callPlace(place);
                success = true;
                break;
            }
            i++;
        }
        return success;
    }

    private void callPlace(Place place) {
        currentPlace = place;
        notifyListeners(globalListeners, place);
        List<Class<? extends Place>> hierarchy = place.getHierarchy();
        for (Class<? extends Place> hierClass : hierarchy) {
            notifyListeners(branchListeners.get(hierClass), place);
        }
        notifyListeners(placeListeners.get(place.getClass()), place);
    }

    private void notifyListeners(List<Listener> listeners, Place place) {
        if (listeners != null) {
            for (Listener listener : listeners) {
                notifyListener(listener, place);
            }
        }
    }

    private void notifyListener(Listener listener, Place place) {
        try {
            listener.onPlace(place);
        } catch (Throwable error) {
            listener.onError(error);
        }
    }

}
