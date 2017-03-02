package okuki.sample.kittens.detail;

import okuki.PlaceConfig;
import okuki.IntegerPlace;
import okuki.sample.kittens.KittensPlace;

@PlaceConfig(parent = KittensPlace.class)
public class KittensDetailPlace extends IntegerPlace {

    public KittensDetailPlace(Integer data) {
        super(data);
    }

    @Override
    public String toString() {
        return "KittensDetailPlace{" + getData() + "}";
    }
}
