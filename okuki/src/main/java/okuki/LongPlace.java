package okuki;

import android.os.Parcel;

import okuki.Place;

public class LongPlace extends Place<Long> {

    public LongPlace(Long data) {
        super(data);
    }

    private LongPlace(Parcel in) {
        this(in.readLong());
    }

}
