package okuki.android;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import okuki.Okuki;
import okuki.Place;

public class OkukiState implements Parcelable {

    private final WrappedPlace currentPlace;
    private final List<WrappedPlace> placeHistory = new ArrayList<>();

    OkukiState(Okuki okuki) {
        currentPlace = new WrappedPlace(okuki.getCurrentPlace());
        for (Place place : okuki.getHistory()) {
            placeHistory.add(new WrappedPlace(place));
        }
    }

    WrappedPlace getCurrentPlace() {
        return currentPlace;
    }

    List<WrappedPlace> getPlaceHistory() {
        return placeHistory;
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
    }

    public static final Creator<OkukiState> CREATOR = new Creator<OkukiState>() {
        @Override
        public OkukiState createFromParcel(Parcel parcel) {
            return new OkukiState(parcel);
        }

        @Override
        public OkukiState[] newArray(int size) {
            return new OkukiState[size];
        }
    };

}
