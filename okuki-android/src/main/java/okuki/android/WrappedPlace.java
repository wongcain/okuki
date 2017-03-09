package okuki.android;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import okuki.Place;
import okuki.SimplePlace;

class WrappedPlace<T extends Place> implements Parcelable {
    private final Class<T> placeClass;
    private final Class dataClass;
    private final Object data;

    WrappedPlace(T place) {
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

    T getPlace() {
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

    public static final Creator<WrappedPlace> CREATOR = new Creator<WrappedPlace>() {
        @Override
        public WrappedPlace createFromParcel(Parcel parcel) {
            return new WrappedPlace(parcel);
        }

        @Override
        public WrappedPlace[] newArray(int size) {
            return new WrappedPlace[size];
        }
    };

}
