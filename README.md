# Okuki
A simple, hierarchical navigation bus and back stack for Android, with optional Rx bindings,
and Toothpick integration for automatic dependency-scope management.

## Examples
2 sample projects are provided below. Although Okuki is written in Java with no Android dependencies,
it was written with Android in mind. As such, both examples are Android projects:
* [Simple Example](https://github.com/wongcain/okuki/tree/master/okuki-sample): Demonstrates basic
usage in a simple Android project, without use of the optional Rx and Toothpick integrations.
* [MVP Example](https://github.com/wongcain/okuki/tree/master/okuki-toothpick-mvp-sample): Demonstrates
Okuki's full capabilities using Rx, Toothpick, and Parcelable-State save/restore. It also implements 
an Okuki-centric approach to the MVP (Model-View-Presenter) design pattern.

## Setup
Gradle:
```
repositories {
    jcenter()
}

...

dependencies {
    compile 'com.cainwong.okuki:okuki:0.2.0'
     
    // for RxBindings
    compile 'io.reactivex:rxjava:1.2.5'
    compile 'com.cainwong.okuki:okuki-rx:0.2.0'
 
    // for Android Parcelable State Save/Restore
    compile 'com.cainwong.okuki:okuki-android:0.2.0'
 
    // for Toothpick integration
    compile 'com.github.stephanenicolas.toothpick:toothpick-runtime:1.0.5'
    compile 'com.github.stephanenicolas.toothpick:toothpick-compiler:1.0.5'
    compile 'com.cainwong.okuki:okuki-toothpick:0.2.0'
}
```


## What is Okuki?
Okuki's purpose is to communicate and remember hierarchical application UI state changes in a 
consistent, abstracted way across an application. This is done by the creation of `Place` classes 
that represent unique UI states or destinations. Think of a Place as a _URL_ or _route_ that maps to 
a UI state. Square's _Flow_ library is based on a similar concept. However, where _Flow_ 
defines a specific mechanism for implementing UI changes (as well Resource management and 
lifecycle), Okuki makes no requirements on how UI state changes are implemented. For example Okuki 
can support single- or multi-Activity architectures, with or without using Fragments and/or custom 
views. 

## Places
Each UI state is presented by an instance of an Object extended from the class `Place<T>`. Each
Place defines a type of payload `T` that will be carried by the instance. Okuki places no 
restriction on the Type of of payload that a Place may carry. However, if you wish to make use of 
`OkukiParceler` in the `okuki-android` extension (see _Saving and Restoring State_ below), you will 
need to limit your payloads to `Parcelable` and/or `Serializable` Types. If you require no payload, 
you can use Type `Void` or extend `SimplePlace` which Okuki includes for convenience.


## Place Hierachy
All Places are hierarchical. By default, each Place sits at the root-level of the hierarchy. However,
Places can be nested in a hierarchy by annotating them with `@PlaceConfig` and setting the
`parent` parameter.

```
@PlaceConfig(parent = ContactsPlace.class)
public class ContactDetailsPlace extends Place<Integer> {

    public ContactDetailsPlace(Integer data) {
        super(data);
    }

}
```
In the example above, `ContactDetailsPlace` is an immediate _descendant_ of `ContactsPlace`. If
another class is created that identifies `ConactDetailsPlace` as _its_ parent, both this new class
and `ContactDetailsPlace` would be _descendants_ of `ContactsPlace`.

## Listening for Place Requests
Okuki provies 3 types of Listeners. Each Listener registers to receive `Place` instances as they are
requested. The difference between the listeners is what type of requested Places they receive.

### PlaceListener
PlaceListeners receive only Places only of a specified type.  The type is specified via the generic
attribute `P` in `PlaceListener<P extends Place>`.

The following listener would receive only instances of `ContactDetailsPlace`:
```
PlaceListener<ContactDetailsPlace> contactDetailsPlaceListener = new PlaceListener<ContactDetailsPlace>{
    @Override
    public void onPlace(ContactDetailsPlace place) {
        int id = place.getData();
        // GET CONTACT DATA UPDATE UI, ETC.
    }
};
okuki.addPlaceListener(contactDetailsPlaceListener);
```

### BranchListener
BranchListeners also receive Places of a specified type, but also receive any Place that is a
_descendant_ of the specified type. So given the classes defined in the _Place Hierarchy_ section
above, the following code would listen for Place requests for `ContactsPlace` and `ContactDetailsPlace`,
as well as any other _descendents_ of these classes:
```
BranchListener contactsBranchListener = new BranchListener<ContactsPlace>() {
   @Override
   public void onPlace(Place place) {
       // NAVIGATE TO "CONTACTS" SECTION OF APPLICATION, ETC.
   }
};
okuki.addBranchListener(contactsBranchListener);
```

### GlobalListener
A registered `GlobalListener` will receive all Places that are requested, regardless of type or
hierarchy. One simple use-case for such a listener would be a logging mechanism that reports all
requested Places for the purpose of debugging.
```
GlobalListener logListener = new GlobalListener(){
   @Override
   public void onPlace(Place place) {
       log("Place requested: " + place);
   }
};
okuki.addGlobalListener(logListener);
```

### Removing Listeners
Okuki keeps strong references to registered Listeners, so be sure the unregister them as appropriate
for your application lifecyle.
```
okuki.removePlaceListener(contactDetailsPlaceListener);
...
okuki.removeBranchListener(contactsBranchListener);
...
okuki.removeGlobalListener(logListener);

```

### Error Handling
In addition to `onPlace(Place)` each of the Listeners also implements an `onError(Exception e)` method.
The default behavior of this method is to throw a Runtime exception. It is recommended that you
override this behavior in your Listener implementations. If using `RxOkuki`, exceptions are delegated
through the standard Rx error propagation.

### Sticky Behavior
A very important aspect of Okuki's behavior is that the most recently requested Place is automatically
provided to a Listener immediately when the listener is added (subject to the scope of the listener
as described by the various listener definitions above). So in the previous example, `logListener`
would automatically receive the most recently requested Place on `okuki.addGlobalListener(logListener)`.
`contactsBranchListener` would also receive the most recent place at
`okuki.addBranchListener(contactsBranchListener)`, but only if the most recent place was of type
`ContactsPlace` or one of its descendants.


## Requesting Places
Issuing a place request is as simple as calling `okuki.gotoPlace(Place place)`. Okuki takes each
received `Place` and broadcasts it to all registered listeners capable of receiving Places of the
given type. Additionally, a method is provided for specifying a `HistoryAction` to perform when
issuing the request: `okuki.gotoPlace(Place place, HistoryAction historyAction)`. More about
`HistoryAction` and the history back stack follows.

## Place History Back Stack
In addition for providing a mechanism for requesting an receiving places, Okuki provides a history
back stack of the places requested. This back stack may be used to easily navigate to back to previously
requested Places. To go back to the previous Place, simply call `okuki.goBack()`. When doing so, the
most recent Place is popped off the back stack and broadcast to configured listeners.
Okuki also provides the method `okuki.getHistory()` that provides direct access to the history. This
allows you to rewrite any portion of the back stack as needed without triggering any Place requests.

### History Actions
By default each Place is added to the top of the history back stack. But Okuki supports several
options for how a Place request affects the history.  Use the following `HistoryAction` values as
follows via the method `okuki.gotoPlace(Place place, HistoryAction historyAction)`:
 * `ADD`: The default behavior. Adds the requested Place to the top of the history back stack.
 * `REPLACE_TOP`: This will remove the most recent Place from the top of the history back stack, and
 then place the provided Place at the top of the stack. If the stack is already empty, behaves the
 same as `ADD`.
 * `TRY_BACK_TO_SAME`: Searches backwards in the stack to find an equivalent Place
 (`Object.equals(Object o)`). If found, pops the stack back to the found Place (including popping the
 found Place), and then adds the requested Place to the back stack. If not found, behaves the same as
 `ADD`.
 * `TRY_BACK_TO_SAME_TYPE`: Searches backwards in the stack to find a Place of the same type (instance
 of the same Class). If found, pops the stack back to the found Place (including popping the found
 Place), and then adds the requested Place to the back stack. If not found, behaves the same as `ADD`.
 * `NONE`: Broadcasts the requested place to the Listeners without altering the history in any way,
 including adding the requested Place to the back stack.

## Thread Safety
Okuki is single-threaded (i.e. _NOT_ thread-safe). The reason for this is that it is designed for 
communicating UI changes and is intended to be run on a single thread (the Android Main/UI Thread).

## Rx Bindings (RxJava 1.x)
RxBindings are provided in the optional package `okuki.rx`. Using these bindings simplifies use of
Okuki as you no longer need to manage instances of the various `Listener` classes. Simply subscribe
and unsubscribe like this:
```
Subscription globalSub = RxOkuki.onAnyPlace(okuki).subscribe(place -> logPlace(place));
Subscription branchSub = RxOkuki.onBranch(okuki, ContactsPlace.class).subscribe(place -> gotoContacts());
Subscription placeSub = RxOkuki.onPlace(okuki, ContactsPlace.class).subscribe(place -> dislayContact(place.getData()));
...
subscription.unsubscribe(globalSub);
subscription.unsubscribe(branchSub);
subscription.unsubscribe(placeSub);
```
#### _RxJava 2.0 coming soon..._

## Saving and Restoring State (_Android Only_)
The optional Okuki-Android package provides a mechanism for saving and restoring the state of an 
Okuki instance as a Parcelable. With it you can maintain Okuki's state (current Place and Place 
History Back Stack) across Android configuration changes and process death.

### Usage
Do the following to enable Okuki State save/restore:
1. Add the dependency for Okuki-Android. (See _Setup_ above.)
2. Ensure that all of your `Place` classes either have `Void` payload types (which includes 
`SimplePlace`), or payload types that implement `Parcelable` or `Serializable`.
3. Call `OkukiParceler.extract(Okuki okuki)` to get a Parcelable `OkukiState` object that can 
be written into a `Bundle`. (_Note: the OkukiState may be null if no PlaceRequest has yet been made._)
4. Call `OkukiParceler.apply(Okuki okuki, OkukiState okukiState)` to restore the saved state to your
Okuki instance.

## Toothpick Dependency Injection Integration
Toothpick integration is provided via the optional package `okuki.toothpick`. This integration allows
you to use Places and their hierarchy as your Toothpick Scope hierarchy, and to define modules to load 
at each level of the hierarchy. Once configured, a `PlaceScoper` listens for Place requests and 
automatically opens a Toothpick scope that reflects the respective Place hierarchy. Additionally, any 
scopes that is not part of the newly requested Place hierachy are automatically closed, freeing up 
resources not needed in the new Place hierarchy. 

### Usage
Do the following to enable the Toothpick integration:
1. Add the dependencies for both Toothpick and Okuki-Toothpick. (See _Setup_ above.)
2. Create a `PlaceScoper` instance for your Okuki instance using its `Builder`, also specifying any 
Modules that you like to configure dependencies that should be available globally:
```
placeScoper = new PlaceScoper.Builder().okuki(okuki).modules(new AppModule(), new NetworkModule()).build();
```
3. Use the `@ScopeConfig` annotation on `Place` classes to define additional Modules that should 
apply only to the respective portion of the hierachy defined by the Place. (These modules will 
themselves automatically receive any injections available from their parent scope.):
```
@ScopeConfig(modules = KittensModule.class)
public class KittensPlace extends SimplePlace {
    public KittensPlace() {
    }
}
```
4. Use your instance of `PlaceScoper` to perform all of your injections across your application. 
(Do do so you'll need provide some kind of static accessor to your `PlaceScoper` instance.):
```
APP_INSTANCE.placeScoper.inject(obj);
```


To best understand how Okuki and Toothpick work together, see the
[MVP Example](https://github.com/wongcain/okuki/tree/master/okuki-toothpick-mvp-sample).

For more information about using the wonderful Toothpick library, please its
[Github repository](https://github.com/stephanenicolas/toothpick) and the thorough documentation on
the wiki there.
