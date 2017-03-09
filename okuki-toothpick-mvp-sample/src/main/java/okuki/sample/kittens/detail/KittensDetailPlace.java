package okuki.sample.kittens.detail;

import okuki.Place;
import okuki.PlaceConfig;
import okuki.sample.kittens.KittensPlace;

@PlaceConfig(parent = KittensPlace.class)
public class KittensDetailPlace extends Place<Integer> {

    public KittensDetailPlace(Integer data) {
        super(data);
    }

    @Override
    public String toString() {
        return "KittensDetailPlace{" + getData() + "}";
    }
}
