package okuki.sample.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import okuki.BranchListener;
import okuki.HistoryAction;
import okuki.Okuki;
import okuki.Place;
import okuki.PlaceListener;
import okuki.sample.R;
import okuki.sample.contacts.details.ContactDetailsPlace;
import okuki.sample.contacts.details.ContactDetailsView;
import okuki.sample.contacts.edit.ContactEditPlace;
import okuki.sample.contacts.edit.ContactEditView;

public class ContactsFragment extends Fragment {

    public static final String TAG = ContactsFragment.class.getSimpleName();

    private final Okuki okuki = Okuki.getDefault();

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    private ContactsAdapter contactsAdapter;
    private PlaceListener<ContactsPlace> contactsPlaceListener;
    private BranchListener<ContactDetailsPlace> contactDetailsBranchListener;
    private BranchListener<ContactEditPlace> contactEditBranchListener;
    private ContactsDataManager.UpdateListener contactsUpdateListener;

    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = (Spinner) view.findViewById(R.id.contacts_spinner);
        contactsAdapter = new ContactsAdapter(view.getContext());
        spinner.setAdapter(contactsAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position > 0) {
                    HistoryAction historyAction = okuki.getCurrentPlace() instanceof ContactsPlace
                            ? HistoryAction.REPLACE_TOP : HistoryAction.TRY_BACK_TO_SAME_TYPE;
                    okuki.gotoPlace(new ContactDetailsPlace(position), historyAction);
                    spinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        contactsPlaceListener = new PlaceListener<ContactsPlace>() {
            @Override
            public void onPlace(ContactsPlace place) {
                setSpinnerVisible(true);
            }
        };
        okuki.addPlaceListener(contactsPlaceListener);
        contactDetailsBranchListener = new BranchListener<ContactDetailsPlace>() {
            @Override
            public void onPlace(Place place) {
                setSpinnerVisible(true);
                gotoContactDetails();
            }
        };
        okuki.addBranchListener(contactDetailsBranchListener);
        contactEditBranchListener = new BranchListener<ContactEditPlace>() {
            @Override
            public void onPlace(Place place) {
                setSpinnerVisible(false);
                gotoContactEdit();
            }
        };
        okuki.addBranchListener(contactEditBranchListener);
        contactsUpdateListener = new ContactsDataManager.UpdateListener() {
            @Override
            public void onUpdated() {
                contactsAdapter.notifyDataSetChanged();
            }
        };
        ContactsDataManager.addUpdateListener(contactsUpdateListener);
    }

    @Override
    public void onStop() {
        okuki.removeBranchListener(contactDetailsBranchListener);
        okuki.removeBranchListener(contactEditBranchListener);
        ContactsDataManager.removeUpdateListener(contactsUpdateListener);
        super.onStop();
    }

    private void gotoContactDetails() {
        ViewGroup container = (ViewGroup) getView().findViewById(R.id.contacts_container);
        container.removeAllViews();
        container.addView(new ContactDetailsView(getContext()));
    }

    private void gotoContactEdit() {
        ViewGroup container = (ViewGroup) getView().findViewById(R.id.contacts_container);
        container.removeAllViews();
        container.addView(new ContactEditView(getContext()));
    }

    private void setSpinnerVisible(boolean visible) {
        spinner.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private class ContactsAdapter extends ArrayAdapter<Contact> {

        public ContactsAdapter(Context context) {
            super(context, android.R.layout.simple_spinner_dropdown_item);
            add(new Contact(0, getString(R.string.label_select), ""));
            addAll(ContactsDataManager.getContacts());
        }
    }

}
