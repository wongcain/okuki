package okuki;

import android.os.Parcel;

import okuki.Place;

public class IntegerPlace extends Place<Integer> {

    public IntegerPlace(Integer data) {
        super(data);
    }

    private IntegerPlace(Parcel in) {
        this(in.readInt());
    }

}
