package okuki.toothpick;

import java.util.ArrayList;
import java.util.List;

import okuki.GlobalListener;
import okuki.Okuki;
import okuki.Place;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class PlaceScoper extends GlobalListener {

    private final Okuki okuki;
    private Place currentPlace;

    public PlaceScoper(Okuki okuki, Module... rootModules) {
        this.okuki = okuki;
        Toothpick.openScope(okuki).installModules(rootModules);
        okuki.addGlobalListener(this);
    }

    public void close() {
        okuki.removeGlobalListener(this);
        Toothpick.closeScope(okuki);
    }

    private Scope openPlaceScope(Class<? extends Place> placeClass, Module... modules) {
        List<Object> scopeKeys = new ArrayList<>();
        scopeKeys.add(okuki);
        scopeKeys.addAll(Place.getHierarchyForPlace(placeClass));
        Scope scope = Toothpick.openScopes(scopeKeys.toArray());
        if (modules != null) {
            scope.installModules(modules);
        }
        return scope;
    }

    private Scope openParentScope(Class<? extends Place> placeClass) {
        List<Object> scopeKeys = new ArrayList<>();
        scopeKeys.add(okuki);
        List<Class<? extends Place>> hierarchy = Place.getHierarchyForPlace(placeClass);
        if (hierarchy.size() > 1) {
            scopeKeys.addAll(hierarchy.subList(0, hierarchy.size() - 1));
        }
        return Toothpick.openScopes(scopeKeys.toArray());
    }

    public void inject(Object obj) {
        Scope scope = (currentPlace == null) ? Toothpick.openScope(okuki) : openPlaceScope(currentPlace.getClass());
        Toothpick.inject(obj, scope);
    }

    @Override
    public void onPlace(Place place) {

        // get current place hierarchy
        List<Class<? extends Place>> prevHier = (currentPlace == null) ? new ArrayList<>() : currentPlace.getHierarchy();

        // get new place hierarchy
        List<Class<? extends Place>> newHier = place.getHierarchy();

        // close previous place scope hierarchy starting at first place of difference
        List<Class<? extends Place>> keysToClose = new ArrayList<>(prevHier);
        keysToClose.removeAll(newHier);
        for (Class<? extends Place> keyToClose : keysToClose) {
            Toothpick.closeScope(keyToClose);
        }

        // open new scope hierarchy setting modules for newly opened scopes
        for (Class<? extends Place> keyToOpen : newHier) {
            List<Module> modules = new ArrayList<>();
            if (!prevHier.contains(keyToOpen)) {
                Scope parentScope = openParentScope(keyToOpen);
                List<Class<? extends Module>> moduleClasses = getModuleClassesForPlaceScope(keyToOpen);
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
            openPlaceScope(keyToOpen, modules.toArray(new Module[modules.size()]));
        }

        currentPlace = place;
    }


    private List<Class<? extends Module>> getModuleClassesForPlaceScope(Class<? extends Place> placeClass) {
        List<Class<? extends Module>> moduleClasses = new ArrayList<>();
        ScopeConfig config = placeClass.getAnnotation(ScopeConfig.class);
        if (config != null) {
            for (Class<? extends Module> moduleClass : config.modules()) {
                moduleClasses.add(moduleClass);
            }
        }
        return moduleClasses;
    }

}
