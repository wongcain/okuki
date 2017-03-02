package okuki.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okuki.HistoryAction;
import okuki.Okuki;
import okuki.Place;
import okuki.SimplePlace;

public class OkukiState implements Parcelable {

    private final WrappedPlace currentPlace;
    private final List<WrappedPlace> placeHistory = new ArrayList<>();

    private OkukiState(Okuki okuki) {
        currentPlace = new WrappedPlace(okuki.getCurrentPlace());
        for (Place place : okuki.getHistory()) {
            placeHistory.add(new WrappedPlace(place));
        }
    }

    private OkukiState(Parcel parcel) {
        currentPlace = parcel.readParcelable(WrappedPlace.class.getClassLoader());
        parcel.readTypedList(placeHistory, WrappedPlace.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(currentPlace, flags);
        parcel.writeTypedList(placeHistory);
        Log.d("OkukiState", "Saved history size: " + placeHistory.size());
    }

    public static final Parcelable.Creator<OkukiState> CREATOR = new Parcelable.Creator<OkukiState>() {
        @Override
        public OkukiState createFromParcel(Parcel parcel) {
            return new OkukiState(parcel);
        }

        @Override
        public OkukiState[] newArray(int size) {
            return new OkukiState[size];
        }
    };

    public static OkukiState extractFromOkuki(Okuki okuki) {
        return okuki.getCurrentPlace() == null ? null : new OkukiState(okuki);
    }

    public void applyToOkuki(Okuki okuki) {
        okuki.getHistory().clear();
        for (WrappedPlace wrappedPlace : placeHistory) {
            okuki.getHistory().add(wrappedPlace.getPlace());
            Log.d("OkukiState", "Adding place to history: " + wrappedPlace.placeClass + ": " + wrappedPlace.getPlace());
        }
        Log.d("OkukiState", "Setting current place: " + currentPlace.placeClass + ": " + currentPlace.getPlace());
        okuki.gotoPlace(currentPlace.getPlace(), HistoryAction.NONE);
        Log.d("OkukiState", "Restored history size: " + placeHistory.size());
    }


    private static class WrappedPlace<T extends Place> implements Parcelable {
        private final Class<T> placeClass;
        private final Class dataClass;
        private final Object data;

        private WrappedPlace(T place) {
            placeClass = (Class<T>) place.getClass();
            data = place.getData();
            if (data == null) {
                dataClass = Void.class;
            } else {
                dataClass = data.getClass();
            }
        }

        private WrappedPlace(Parcel parcel) {
            placeClass = (Class<T>) parcel.readSerializable();
            dataClass = (Class) parcel.readSerializable();
            if (Void.class.equals(dataClass)) {
                data = parcel.readValue(String.class.getClassLoader());
            } else if (Parcelable.class.isAssignableFrom(dataClass)) {
                data = parcel.readParcelable(dataClass.getClassLoader());
            } else if (Serializable.class.isAssignableFrom(dataClass)) {
                data = parcel.readSerializable();
            } else {
                throw new RuntimeException(String.format("Error restoring place %s", placeClass));
            }
        }

        private T getPlace() {
            try {
                if (SimplePlace.class.isAssignableFrom(placeClass)) {
                    return (T) placeClass.newInstance();
                } else {
                    return (T) placeClass.getConstructor(new Class[]{dataClass}).newInstance(data);
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("Error getting place %s", placeClass));
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeSerializable(placeClass);
            parcel.writeSerializable(dataClass);
            if (Void.class.equals(dataClass)) {
                parcel.writeValue(null);
            } else if (Parcelable.class.isAssignableFrom(dataClass)) {
                parcel.writeParcelable((Parcelable) data, 0);
            } else if (Serializable.class.isAssignableFrom(dataClass)) {
                parcel.writeSerializable((Serializable) data);
            } else {
                throw new RuntimeException(String.format("Error restoring place %s", placeClass));
            }
        }

        public static final Parcelable.Creator<WrappedPlace> CREATOR = new Parcelable.Creator<WrappedPlace>() {
            @Override
            public WrappedPlace createFromParcel(Parcel parcel) {
                Log.d("OkukiState", "WrappedPlace.CREATOR.createFromParcel");
                return new WrappedPlace(parcel);
            }

            @Override
            public WrappedPlace[] newArray(int size) {
                return new WrappedPlace[size];
            }
        };

    }
}
