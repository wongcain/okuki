# Okuki
A simple, hierarchical navigation bus and back-stack for Android and Java, with optional Rx bindings,
and Toothpick integration for brainless dependency scope management.

## Examples
2 sample projects are provided below. Although Okuki is written in Java with no Android dependencies,
it was written with Android in mind. As such, both examples are Android projects:
* [Simple Example](https://github.com/wongcain/okuki/tree/master/okuki-sample): Demonstrates basic
usage in a simple Android project, without use of the optional Rx and Toothpick integrations.
* [MVP Example](https://github.com/wongcain/okuki/tree/master/okuki-toothpick-mvp-sample): Demonstrates
Okuki's full capabilities using both Rx and Toothpick integrations, and implements the MVP
(Model-View-Presenter) design pattern.

## Setup
Gradle:
```
repositories {
    jcenter()
    maven {
        url 'https://dl.bintray.com/wongcain/okuki'
    }
}

...

dependencies {
    compile 'com.cainwong.okuki:okuki:0.1.0'

    // for RxBindings
    compile 'io.reactivex:rxjava:1.2.5'
    compile 'com.cainwong.okuki:okuki-rx:0.1.0'

    // for Toothpick integration
    compile 'com.github.stephanenicolas.toothpick:toothpick-runtime:1.0.2'
    compile 'com.github.stephanenicolas.toothpick:toothpick-compiler:1.0.2'
    compile 'com.cainwong.okuki:okuki-toothpick:0.1.0'
}
```


## What is Okuki?
Okuki's purpose is to communicate and remember application UI navigation requests in a consistent,
abstracted way across an application. This is done by the creation of `Place` classes that represent
unique UI states or destinations. Think of a Place as a _URL_ or _route_ that maps to a UI state.
Square's _Flow_ library is based on the same general concept. However, where _Flow_ provides an
Android-specific mechanism for implementing UI changes (as well Resource management and lifecycle),
Okuki takes a simpler, less restrictive approach, functioning more like an _EventBus_ for
communicating UI state change requests without making requirements on how the requests are handled.
This means that choices such as whether or not to use multiple Activities or a single-Activity
architecture, or whether or not to use Fragments vs Custom Views is completely up to you. Okuki plays
nicely with all of these approaches.

## Places
Each UI state is presented by an instance of an Object extended from the class `Place<T>`. Each
Place defines a type of payload `T` that will be carried by the instance. Most likely, the payload
will of the type `String`or `Integer` representing the unique identifier of a model or record. But
Okuki makes no restriction on what may be sent as a payload. So a Place may carry a more complex
Object as a payload as well. Additionally, a Place is not required to carry a payload at all if it
is not needed. For convenience sake, Okuki provides `SimplePlace` which can be extended to create
no-payload Places.


## Place Hierachy
Places are hierarchical. By default, each Place sits at the root-level of the hierarchy. However,
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

### Sticky Behavior
A very important aspect of Okuki's behavior is that the most recently requested Place is automatically
provided to a Listener immediately when the listener is added (subject to the scope of the listener
as descibed by the various listener definitions above). So in the previous example, `logListener`
would automatically receive the most recently requested Place on `okuki.addGlobalListener(logListener)`.
`contactsBranchListener` would also receive the most recent place at
`okuki.addBranchListener(contactsBranchListener)`, but only if the most recent place was of type
`ContactsPlace` or one of its descendants.


## Requesting Places
Issuing a place request is as simple as calling `okuki.gotoPlace(Place place)`. Okuki takes each
received `Place` and broadcasts it to all registered listeners capable of receiving Places of the
given type. Additionally, a method is provided for specifying a `HistoryAction` to perform when
issuing the request: `okuki.gotoPlace(Place place, HistoryAction historyAction)`. More about
`HistoryAction` and the history backstack follows.

## Place History Backstack
In addition for providing a mechanism for requesting an receiving places, Okuki provides a history
backstack of the places requested. This backstack may be used to easily navigate to back to previously
requested Places. To go back to the previous Place, simply call `okuki.goBack()`. When doing so, the
most recent Place is popped off the back-stack and broadcast to configured listeners.
Okuki also provides the method `okuki.getHistory()` that provides direct access to the history. This
allows you to rewrite any portion of the backstack as needed without triggering any Place requests.

### History Actions
By default each Place is added to the top of the history backstack. But Okuki supports several
options for how a Place request affects the history.  Use the following `HistoryAction` values as
follows via the method `okuki.gotoPlace(Place place, HistoryAction historyAction)`:
 * `ADD`: The default behavior. Adds the requested Place to the top of the history backstack.
 * `REPLACE_TOP`: This will remove the most recent Place from the top of the history backstack, and
 then place the provided Place at the top of the stack. If the stack is already empty, behaves the
 same as `ADD`.
 * `TRY_BACK_TO_SAME`: Searches backwards in the stack to find an equivalent Place
 (`Object.equals(Object o)`). If found, pops the stack back to the found Place (including popping the
 found Place), and then adds the requested Place to the backstack. If not found, behaves the same as
 `ADD`.
 * `TRY_BACK_TO_SAME_TYPE`: Searches backwards in the stack to find a Place of the same type (instance
 of the same Class). If found, pops the stack back to the found Place (including popping the found
 Place), and then adds the requested Place to the backstack. If not found, behaves the same as `ADD`.
 * `NONE`: Broadcasts the requested place to the Listeners without altering the history in any way,
 including adding the requested Place to the backstack.

## Thread Safety
Okuki does not make use of concurrency, i.e. is _NOT_ thread-safe. The reason for this is that it is
designed for communicating UI changes and is intended to be run on a single thread (the Android UI
Thread).

## Rx Bindings
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

## Toothpick Dependency Injection Integration
Toothpick integration is provided via the optional package `okuki.toothpick`. This integration allows
you to use Places and their hierarchy to define dependency scopes. Using the staic methods provided
by `PlaceScoper` you can open and close scope whose hierachy matches the hierarcy of a given Place.
As you open each scope for a place, you can optionally provide any number of `Module` instances that
will be loaded into the scope. The dependencies loaded into the scope will be available to all scopes
opened for descendant Places until the scope is closed. Using this integration unifies the concepts
of "where are you" in an application with "what resources are available", and allows you to clean up
unused resouces (close scopes) more confidently knowing that you are not impacting the resources
depended on by other areas of your application.

### PlaceScoper Methods
* `PlaceScoper.openRootScope(Module... modules)`
   Open the root scope (application-level scope) and install provided modules.

* `PlaceScoper.closenRootScope()`
   Close root scope.

* `PlaceScoper.openPlaceScope(Class<? extends Place> placeClass, Module... modules)`
   Open scope for a Place (hierachical, extending from root scope) and install provided modules.

* `PlaceScoper.closenPlaceScope(Class<? extends Place> placeClass)`
   Close the scope for a Place.

* `PlaceScoper.openParentScope(Class<? extends Place> placeClass)`
   Open the scope of a Place's parent.

### PlaceModules
In addition to `PlaceScoper`, the class `PlaceModule` is also included. This class extends from
Toothpick's standard `Module` class, but adds the functionality of _injecting itself_ with dependencies
from the specified Place's _parent scope_. What this means is that a `PlaceModule` can reference other
injected dependencies specified in higher-level scopes in order to instantiate other resources that
it will be providing without needing to provide additional code for passing these dependencies into
the `Module` constructor, etc.
For example, the module below provies a Retrofit API implementation that is scoped to the class
`KittensPlace`. As you can see the `OkHttpClient` and `Gson` instance are injected, as they are bound
at a scope higher in the hierarchy:
```
public class KittensModule extends PlaceModule<KittensPlace> {

    @Inject
    OkHttpClient client;

    @Inject
    Gson gson;

    public KittensModule() {
        bind(GiphyDataManager.class).toInstance(new GiphyDataManager(client, gson));
        bind(KittensResultsList.class).singletonInScope();
    }
}
```
To best understand how Okuki and Toothpick work together, see the
[MVP Example](https://github.com/wongcain/okuki/tree/master/okuki-toothpick-mvp-sample).

For more information about using the wonderful Toothpick library, please its
[Github repository](https://github.com/stephanenicolas/toothpick) and the thorough documentation on
the wiki there.
