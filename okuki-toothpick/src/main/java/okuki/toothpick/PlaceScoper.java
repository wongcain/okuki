package okuki.toothpick;

import java.util.ArrayList;
import java.util.List;

import okuki.Place;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class PlaceScoper {

    private static final Object ROOT_SCOPE_KEY = new Object() {
        @Override
        public String toString() {
            return PlaceScoper.class.getName() + ".ROOT_SCOPE_KEY";
        }
    };

    public static Scope openRootScope(Module... modules) {
        Scope scope = Toothpick.openScope(ROOT_SCOPE_KEY);
        if (modules != null) {
            scope.installModules(modules);
        }
        return scope;
    }

    public static void closeRootScope() {
        Toothpick.closeScope(ROOT_SCOPE_KEY);
    }

    public static Scope openPlaceScope(Class<? extends Place> placeClass, Module... modules) {
        List<Object> scopeKeys = new ArrayList<>();
        scopeKeys.add(ROOT_SCOPE_KEY);
        scopeKeys.addAll(Place.getHierarchyForPlace(placeClass));
        Scope scope = Toothpick.openScopes(scopeKeys.toArray());
        if (modules != null) {
            scope.installModules(modules);
        }
        return scope;
    }

    public static void closePlaceScope(Class<? extends Place> placeClass) {
        Toothpick.closeScope(placeClass);
    }

    public static Scope openParentScope(Class<? extends Place> placeClass) {
        List<Object> scopeKeys = new ArrayList<>();
        scopeKeys.add(ROOT_SCOPE_KEY);
        List<Class<? extends Place>> hierarchy = Place.getHierarchyForPlace(placeClass);
        if (hierarchy.size() > 1) {
            scopeKeys.addAll(hierarchy.subList(0, hierarchy.size() - 1));
        }
        return Toothpick.openScopes(scopeKeys.toArray());
    }

}
