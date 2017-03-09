package okuki.sample.welcome;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

public class WelcomePlace extends okuki.Place<Date> implements Parcelable {

    public WelcomePlace(@NonNull Date data) {
        super(data);
    }

    private WelcomePlace(Parcel parcel) {
        this(new Date(parcel.readLong()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(getData().getTime());
    }

    public static final Parcelable.Creator<WelcomePlace> CREATOR = new Parcelable.Creator<WelcomePlace>() {
        @Override
        public WelcomePlace createFromParcel(Parcel parcel) {
            return new WelcomePlace(parcel);
        }

        @Override
        public WelcomePlace[] newArray(int size) {
            return new WelcomePlace[size];
        }
    };

}
