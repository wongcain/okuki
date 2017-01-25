package okuki.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import okuki.BranchListener;
import okuki.Okuki;
import okuki.Place;
import okuki.sample.contacts.ContactsFragment;
import okuki.sample.contacts.ContactsPlace;
import okuki.sample.hello.HelloFragment;
import okuki.sample.hello.HelloPlace;

public class MainActivity extends AppCompatActivity {

    private final Okuki okuki = Okuki.getDefault();

    private FragmentManager fm;
    private BranchListener helloBranchListener;
    private BranchListener contactsBranchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        helloBranchListener = new BranchListener<HelloPlace>() {
            @Override
            public void onPlace(Place place) {
                gotoHello();
            }
        };
        okuki.addBranchListener(helloBranchListener);
        contactsBranchListener = new BranchListener<ContactsPlace>() {
            @Override
            public void onPlace(Place place) {
                gotoContacts();
            }
        };
        okuki.addBranchListener(contactsBranchListener);
        okuki.gotoPlace(new HelloPlace(getString(R.string.world)));
    }

    @Override
    protected void onStop() {
        okuki.removeBranchListener(helloBranchListener);
        okuki.removeBranchListener(contactsBranchListener);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (!okuki.goBack()) {
            super.onBackPressed();
        }
    }

    private void gotoHello() {
        Fragment f = fm.findFragmentByTag(HelloFragment.TAG);
        if (f == null) {
            f = HelloFragment.newInstance();
        }
        fm.beginTransaction().replace(R.id.main_container, f, HelloFragment.TAG).commit();
    }

    private void gotoContacts() {
        Fragment f = fm.findFragmentByTag(ContactsFragment.TAG);
        if (f == null) {
            f = ContactsFragment.newInstance();
        }
        fm.beginTransaction().replace(R.id.main_container, f, ContactsFragment.TAG).commit();
    }

}
