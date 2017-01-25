package okuki.rx;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okuki.BranchListener;
import okuki.GlobalListener;
import okuki.Okuki;
import okuki.Place;
import okuki.PlaceListener;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

public class RxOkukiTest {

    @Mock
    Okuki okuki;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGlobalSubscription() throws Exception {
        TestSubscriber<Place> testObserver = TestSubscriber.create();
        RxOkuki.onAnyPlace(okuki).subscribe(testObserver);
        verify(okuki).addGlobalListener(isA(GlobalListener.class));
        testObserver.unsubscribe();
        verify(okuki).removeGlobalListener(isA(GlobalListener.class));
    }

    @Test
    public void testPlaceSubscription() throws Exception {
        TestSubscriber<Place> testObserver = TestSubscriber.create();
        RxOkuki.onPlace(okuki, Place.class).subscribe(testObserver);
        verify(okuki).addPlaceListener(isA(PlaceListener.class));
        testObserver.unsubscribe();
        verify(okuki).removePlaceListener(isA(PlaceListener.class));
    }

    @Test
    public void testBranchSubscription() throws Exception {
        TestSubscriber<Place> testObserver = TestSubscriber.create();
        RxOkuki.onBranch(okuki, Place.class).subscribe(testObserver);
        verify(okuki).addBranchListener(isA(BranchListener.class));
        testObserver.unsubscribe();
        verify(okuki).removeBranchListener(isA(BranchListener.class));
    }


}
