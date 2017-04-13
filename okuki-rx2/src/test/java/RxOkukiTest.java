import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.observers.TestObserver;
import okuki.BranchListener;
import okuki.GlobalListener;
import okuki.Okuki;
import okuki.Place;
import okuki.PlaceListener;
import okuki.SimplePlace;
import okuki.rx2.RxOkuki;

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
        TestObserver<Place> testObserver = TestObserver.create();
        RxOkuki.onAnyPlace(okuki).subscribe(testObserver);
        verify(okuki).addGlobalListener(isA(GlobalListener.class));
        testObserver.dispose();
        verify(okuki).removeGlobalListener(isA(GlobalListener.class));
    }

    @Test
    public void testPlaceSubscription() throws Exception {
        TestObserver<Place> testObserver = TestObserver.create();
        RxOkuki.onPlace(okuki, TestPlace.class).subscribe(testObserver);
        verify(okuki).addPlaceListener(isA(PlaceListener.class));
        testObserver.dispose();
        verify(okuki).removePlaceListener(isA(PlaceListener.class));
    }

    @Test
    public void testBranchSubscription() throws Exception {
        TestObserver<Place> testObserver = TestObserver.create();
        RxOkuki.onBranch(okuki, TestPlace.class).subscribe(testObserver);
        verify(okuki).addBranchListener(isA(BranchListener.class));
        testObserver.dispose();
        verify(okuki).removeBranchListener(isA(BranchListener.class));
    }


    static class TestPlace extends SimplePlace {
    }

}
