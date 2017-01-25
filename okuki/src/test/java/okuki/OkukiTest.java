package okuki;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class OkukiTest {

    Place place1 = new Place1();
    Place place2 = new Place2("test");
    Place place3 = new Place3();
    Okuki okuki;

    @Mock
    GlobalListener globalListener;

    @Mock
    PlaceListener placeListener;

    @Mock
    BranchListener branchListener;

    @Before
    public void setup() {
        okuki = new Okuki();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPlaceHierarchy() throws Exception {
        assertThat(place3.getHierarchy().size()).isEqualTo(3);
        assertThat(place3.getHierarchy().get(0)).isEqualTo(Place1.class);
        assertThat(place3.getHierarchy().get(1)).isEqualTo(Place2.class);
        assertThat(place3.getHierarchy().get(2)).isEqualTo(Place3.class);
    }

    @Test
    public void testGlobalSubscription() throws Exception {
        okuki.addGlobalListener(globalListener);
        gotoAllPlaces();
        verify(globalListener).onPlace(place1);
        verify(globalListener).onPlace(place2);
        verify(globalListener).onPlace(place3);
        okuki.removeGlobalListener(globalListener);
        verify(globalListener, times(3)).onPlace(any(Place.class));
        gotoAllPlaces();
        verify(globalListener, times(3)).onPlace(any(Place.class));
        reset(globalListener);
        okuki.addGlobalListener(globalListener);
        verify(globalListener).onPlace(place3);
        verify(globalListener, times(1)).onPlace(any(Place.class));
        verifyNoMoreInteractions(globalListener);

    }

    @Test
    public void testPlaceSubscription() throws Exception {
        when(placeListener.getPlaceClass()).thenReturn(Place3.class);
        okuki.addPlaceListener(placeListener);
        gotoAllPlaces();
        verify(placeListener, times(1)).onPlace(place3);
        okuki.removePlaceListener(placeListener);
        gotoAllPlaces();
        verify(placeListener, times(1)).onPlace(any(Place.class));
        reset(placeListener);
        when(placeListener.getPlaceClass()).thenReturn(Place3.class);
        okuki.addPlaceListener(placeListener);
        okuki.removePlaceListener(placeListener);
        verify(placeListener, times(1)).onPlace(place3);
        reset(placeListener);
        when(placeListener.getPlaceClass()).thenReturn(Place2.class);
        okuki.addPlaceListener(placeListener);
        okuki.removePlaceListener(placeListener);
        verify(placeListener, times(0)).onPlace(any(Place.class));
    }

    @Test
    public void testBranchSubscription() throws Exception {
        when(branchListener.getBranchClass()).thenReturn(Place2.class);
        okuki.addBranchListener(branchListener);
        gotoAllPlaces();
        verify(branchListener).onPlace(place2);
        verify(branchListener).onPlace(place3);
        okuki.removeBranchListener(branchListener);
        gotoAllPlaces();
        verify(branchListener, times(2)).onPlace(any(Place.class));
        reset(branchListener);
        when(branchListener.getBranchClass()).thenReturn(Place2.class);
        okuki.addBranchListener(branchListener);
        okuki.removeBranchListener(branchListener);
        verify(branchListener).onPlace(place3);
        verify(branchListener, times(1)).onPlace(any(Place.class));
    }

    @Test
    public void testGoBack() throws Exception {
        okuki.addGlobalListener(globalListener);
        gotoAllPlaces();
        assertThat(okuki.goBack()).isTrue();
        assertThat(okuki.goBack()).isTrue();
        assertThat(okuki.goBack()).isFalse();
        verify(globalListener, times(2)).onPlace(place1);
        verify(globalListener, times(2)).onPlace(place2);
        verify(globalListener).onPlace(place3);
        verifyNoMoreInteractions(globalListener);

    }

    @Test
    public void testHistoryActionAdd() throws Exception {
        okuki.gotoPlace(place1);
        gotoAllPlaces();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place3);
        assertThat(okuki.goBack()).isTrue();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place2);
        assertThat(okuki.goBack()).isTrue();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place1);
        assertThat(okuki.goBack()).isTrue();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place1);
        assertThat(okuki.goBack()).isFalse();
    }

    @Test
    public void testHistoryActionReplaceTop() throws Exception {
        okuki.gotoPlace(place1, HistoryAction.REPLACE_TOP);
        gotoAllPlaces(HistoryAction.REPLACE_TOP);
        assertThat(okuki.getCurrentPlace()).isEqualTo(place3);
        assertThat(okuki.goBack()).isFalse();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place3);
    }

    @Test
    public void testHistoryActionTryBackToSameType() throws Exception {
        gotoAllPlaces(HistoryAction.TRY_BACK_TO_SAME_TYPE);
        assertThat(okuki.getCurrentPlace()).isEqualTo(place3);
        assertThat(okuki.goBack()).isTrue();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place2);
        okuki.gotoPlace(place1, HistoryAction.TRY_BACK_TO_SAME_TYPE);
        assertThat(okuki.goBack()).isFalse();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place1);
    }

    @Test
    public void testHistoryActionTryBackToSame() throws Exception {
        Place place2a = new Place2("Another place");
        gotoAllPlaces(HistoryAction.TRY_BACK_TO_SAME);
        okuki.gotoPlace(place2a, HistoryAction.TRY_BACK_TO_SAME);
        assertThat(okuki.getCurrentPlace()).isEqualTo(place2a);
        assertThat(okuki.goBack()).isTrue();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place3);
        okuki.gotoPlace(place2, HistoryAction.TRY_BACK_TO_SAME);
        assertThat(okuki.getCurrentPlace()).isEqualTo(place2);
        assertThat(okuki.goBack()).isTrue();
        assertThat(okuki.getCurrentPlace()).isEqualTo(place1);
    }

    private void gotoAllPlaces() {
        okuki.gotoPlace(place1);
        okuki.gotoPlace(place2);
        okuki.gotoPlace(place3);
    }

    private void gotoAllPlaces(HistoryAction historyAction) {
        okuki.gotoPlace(place1, historyAction);
        okuki.gotoPlace(place2, historyAction);
        okuki.gotoPlace(place3, historyAction);
    }

    class Place1 extends SimplePlace {
    }

    @PlaceConfig(parent = Place1.class)
    class Place2 extends Place<String> {
        public Place2(String mData) {
            super(mData);
        }
    }

    @PlaceConfig(parent = Place2.class)
    class Place3 extends SimplePlace {
    }

}
