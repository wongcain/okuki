package okuki.android;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import okuki.Okuki;
import okuki.Place;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class OkukiParcelerTest {

    @Test
    public void test() throws Exception {
        Okuki okuki = new Okuki();
        Bundle bundle = new Bundle();
        bundle.putString("key", "value");
        BundlePlace bundlePlace = new BundlePlace(bundle);
        StringPlace stringPlace = new StringPlace("string");
        IntegerPlace integerPlace = new IntegerPlace(1);
        LongPlace longPlace = new LongPlace(2l);
        FloatPlace floatPlace = new FloatPlace(3.1f);
        DoublePlace doublePlace = new DoublePlace(4.2d);

        OkukiState state = OkukiParceler.extract(okuki);
        assertNull(state);

        okuki.gotoPlace(bundlePlace);
        okuki.gotoPlace(stringPlace);
        okuki.gotoPlace(integerPlace);
        okuki.gotoPlace(longPlace);
        okuki.gotoPlace(floatPlace);
        okuki.gotoPlace(doublePlace);

        state = OkukiParceler.extract(okuki);

        okuki = new Okuki();
        OkukiParceler.apply(okuki, state);

        assertEquals(doublePlace, okuki.getCurrentPlace());
        List<Place> historyList = new ArrayList<>(okuki.getHistory());
        assertEquals(6, historyList.size());
        assertEquals(bundlePlace, historyList.get(5));
        assertEquals(stringPlace, historyList.get(4));
        assertEquals(integerPlace, historyList.get(3));
        assertEquals(longPlace, historyList.get(2));
        assertEquals(floatPlace, historyList.get(1));
        assertEquals(doublePlace, historyList.get(0));
    }


}
