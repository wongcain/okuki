package okuki.sample.common.okuki;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okuki.HistoryAction;
import okuki.Okuki;
import okuki.Place;
import okuki.SimplePlace;
import timber.log.Timber;

public class OkukiStateRestorer {

    private static final String KEY = OkukiState.class.getName();

    @Inject
    Gson gson;

    @Inject
    Okuki okuki;


    class SerializedPlace {
        String name;
        String data;

        SerializedPlace(String name, String data) {
            this.name = name;
            this.data = data;
        }
    }

    class OkukiState {
        SerializedPlace currentPlace;
        List<SerializedPlace> history = new ArrayList<>();

        OkukiState(SerializedPlace currentPlace, List<SerializedPlace> history) {
            this.currentPlace = currentPlace;
            this.history = history;
        }
    }

    public void onSave(@NonNull Bundle bundle) {
        try {
            if (okuki.getCurrentPlace() != null) {
                List<SerializedPlace> history = new ArrayList<>();
                for (Place place : okuki.getHistory()) {
                    history.add(serialize(place));
                }
                SerializedPlace currentPlace = serialize(okuki.getCurrentPlace());
                bundle.putString(KEY, gson.toJson(new OkukiState(currentPlace, history)));
                Timber.d("Saved Okuki state with %s Places in history", okuki.getHistory().size());
            } else {
                Timber.d("No Okuki state to save");
            }
        } catch (Exception e) {
            Timber.e(e, "Error saving Okuki state");
        }
    }

    public void onRestore(@Nullable Bundle bundle) {
        if ((okuki.getCurrentPlace() == null) && (bundle != null) && bundle.containsKey(KEY)) {
            try {
                OkukiState okukiState = gson.fromJson(bundle.getString(KEY), OkukiState.class);
                okuki.getHistory().clear();
                for (SerializedPlace serializedPlace : okukiState.history) {
                    okuki.getHistory().add(deserialize(serializedPlace));
                }
                Place currentPlace = deserialize(okukiState.currentPlace);
                okuki.gotoPlace(currentPlace, HistoryAction.NONE);
                Timber.d("Restored Okuki state with %s Places in history", okuki.getHistory().size());
            } catch (Exception e) {
                Timber.e(e, "Error restoring Okuki state");
            }
        }
    }

    private SerializedPlace serialize(Place place) {
        String name = place.getClass().getName();
        String data = gson.toJson(place);
        return new SerializedPlace(name, data);
    }

    private Place deserialize(SerializedPlace serializedPlace) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<? extends Place> placeClass = (Class<? extends Place>) Class.forName(serializedPlace.name);
        if (SimplePlace.class.isAssignableFrom(placeClass)) {
            return placeClass.newInstance();
        } else {
            return gson.fromJson(serializedPlace.data, placeClass);
        }
    }
}
