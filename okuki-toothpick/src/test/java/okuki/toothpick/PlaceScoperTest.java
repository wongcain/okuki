package okuki.toothpick;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okuki.Okuki;
import okuki.Place;
import okuki.PlaceConfig;
import okuki.SimplePlace;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Toothpick.class)
public class PlaceScoperTest {

    PlaceScoper placeScoper;

    @Mock
    Okuki okuki;

    @Mock
    Scope rootScope;

    @Mock
    Scope scope1;

    @Mock
    Scope scope2;

    @Mock
    Scope scope3;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockStatic(Toothpick.class);
        when(Toothpick.openScope(okuki)).thenReturn(rootScope);
        placeScoper = new PlaceScoper(okuki, new RootModule());
    }

    @After
    public void destroy() throws Exception {
        placeScoper.close();
    }

    @Test
    public void testSetupAndDestroy() {
        verifyStatic();
        Toothpick.openScope(okuki);
        verify(rootScope).installModules(isA(RootModule.class));
        verify(okuki).addGlobalListener(placeScoper);
        placeScoper.close();
        verifyStatic();
        Toothpick.closeScope(okuki);
        verify(okuki).removeGlobalListener(placeScoper);
    }

    @Test
    public void testPlaceScoping() {
        when(Toothpick.openScopes(okuki)).thenReturn(rootScope);
        when(Toothpick.openScopes(okuki, Place1.class)).thenReturn(scope1);
        when(Toothpick.openScopes(okuki, Place1.class, Place2.class)).thenReturn(scope2);
        when(Toothpick.openScopes(okuki, Place1.class, Place3.class)).thenReturn(scope3);
        when(rootScope.getInstance(Module1.class)).thenReturn(new Module1());
        when(scope1.getInstance(Module2.class)).thenReturn(new Module2());
        when(scope1.getInstance(Module2A.class)).thenThrow(Exception.class);
        when(scope1.getInstance(Module3.class)).thenReturn(new Module3());

        placeScoper.onPlace(new Place1());
        verifyStatic();
        Toothpick.openScopes(okuki, Place1.class);
        verify(rootScope).getInstance(Module1.class);
        verify(scope1).installModules(isA(Module1.class));

        placeScoper.onPlace(new Place2("test"));
        verifyStatic();
        Toothpick.openScopes(okuki, Place1.class, Place2.class);
        verify(scope1).getInstance(Module2.class);
        verify(scope1).getInstance(Module2A.class);
        verify(scope2).installModules(isA(Module2.class), isA(Module2A.class));

        when(Toothpick.openScopes(okuki, Place1.class, Place3.class)).thenReturn(scope3);
        placeScoper.onPlace(new Place3());
        verifyStatic();
        Toothpick.closeScope(Place2.class);
        verifyStatic();
        Toothpick.openScopes(okuki, Place1.class, Place3.class);
        verify(scope3).installModules(isA(Module3.class));
    }


    static class RootModule extends Module {
    }

    static class Module1 extends Module {
    }

    static class Module2 extends Module {
    }

    static class Module2A extends Module {
    }

    static class Module3 extends Module {
    }


    @ScopeConfig(modules = Module1.class)
    static class Place1 extends SimplePlace {
    }

    @ScopeConfig(modules = {Module2.class, Module2A.class})
    @PlaceConfig(parent = Place1.class)
    static class Place2 extends Place<String> {
        public Place2(String mData) {
            super(mData);
        }
    }

    @ScopeConfig(modules = Module3.class)
    @PlaceConfig(parent = Place1.class)
    static class Place3 extends SimplePlace {
    }


}
