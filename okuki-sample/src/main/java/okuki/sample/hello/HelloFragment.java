package okuki.sample.hello;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import okuki.Okuki;
import okuki.PlaceListener;
import okuki.sample.R;
import okuki.sample.contacts.ContactsPlace;
import okuki.sample.contacts.details.ContactDetailsPlace;


public class HelloFragment extends Fragment {

    public static final String TAG = HelloFragment.class.getSimpleName();

    private final Okuki okuki = Okuki.getDefault();

    private PlaceListener<HelloPlace> helloPlaceListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hello, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.hello_goto_contacts_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okuki.gotoPlace(new ContactsPlace());
            }
        });
        view.findViewById(R.id.hello_goto_third_contact_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okuki.gotoPlace(new ContactDetailsPlace(3));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        helloPlaceListener = new PlaceListener<HelloPlace>() {
            @Override
            public void onPlace(HelloPlace place) {
                setMessage(place.getData());
            }
        };
        okuki.addPlaceListener(helloPlaceListener);
    }

    @Override
    public void onStop() {
        okuki.removePlaceListener(helloPlaceListener);
        super.onStop();
    }

    private void setMessage(String name) {
        TextView tv = (TextView) getView().findViewById(R.id.hello_text);
        tv.setText(getString(R.string.hello, name));
    }

    public static HelloFragment newInstance() {
        return new HelloFragment();
    }

}
