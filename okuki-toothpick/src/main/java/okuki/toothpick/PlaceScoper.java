package okuki.toothpick;

import java.util.ArrayList;
import java.util.List;

import okuki.GlobalListener;
import okuki.Okuki;
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

    private static AutoScoper autoScoper;

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

    public static void enableAutoScoping(Okuki okuki) {
        if (autoScoper == null) {
            autoScoper = new AutoScoper();
        }
        okuki.addGlobalListener(autoScoper);
    }

    public static void disableAutoScoping(Okuki okuki) {
        if (autoScoper != null) {
            okuki.removeGlobalListener(autoScoper);
        }
    }

    private static class AutoScoper extends GlobalListener {

        private Place currentPlace;

        @Override
        public void onPlace(Place place) {
            // get current place hierarchy
            List<Class<? extends Place>> prevHier = new ArrayList<>();
            if (currentPlace != null) {
                prevHier.addAll(currentPlace.getHierarchy());
            }

            // get new place hierarchy
            List<Class<? extends Place>> newHier = new ArrayList<>(place.getHierarchy());

            // close previous place scope hierarchy starting at first place of difference
            List<Class<? extends Place>> keysToClose = new ArrayList<>(prevHier);
            keysToClose.removeAll(newHier);
            for (int i = keysToClose.size() - 1; i > 0; i--) {
                closePlaceScope(keysToClose.get(i));
            }

            // open new scope hierarchy setting modules for newly opened scopes
            for (Class<? extends Place> placeClass : newHier) {
                List<Module> modules = new ArrayList<>();
                if (!prevHier.contains(placeClass)) {
                    Scope parentScope = openParentScope(placeClass);
                    List<Class<? extends Module>> moduleClasses = getModuleClassesForPlaceScope(placeClass);
                    for (Class<? extends Module> moduleClass : moduleClasses) {
                        try {
                            modules.add(parentScope.getInstance(moduleClass));
                        } catch (Exception e) {
                            try {
                                modules.add(moduleClass.newInstance());
                            } catch (Exception e1) {
                                throw new RuntimeException("Unable to instatiate module: " + moduleClass, e1);
                            }
                        }
                    }
                }
                openPlaceScope(placeClass, modules.toArray(new Module[modules.size()]));
            }

            currentPlace = place;
        }


        private List<Class<? extends Module>> getModuleClassesForPlaceScope(Class<? extends Place> placeClass) {
            List<Class<? extends Module>> moduleClasses = new ArrayList<>();
            PlaceScope config = (PlaceScope) placeClass.getAnnotation(PlaceScope.class);
            if (config != null) {
                for (Class<? extends Module> moduleClass : config.modules()) {
                    moduleClasses.add(moduleClass);
                }
            }
            return moduleClasses;
        }

    }

}
