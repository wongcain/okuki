package okuki.sample.contacts.edit;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import okuki.HistoryAction;
import okuki.Okuki;
import okuki.PlaceListener;
import okuki.sample.R;
import okuki.sample.contacts.Contact;
import okuki.sample.contacts.ContactsDataManager;
import okuki.sample.contacts.details.ContactDetailsPlace;

public class ContactEditView extends LinearLayout {

    private final Okuki okuki = Okuki.getDefault();

    private PlaceListener<ContactEditPlace> contactEditPlaceListener;

    private TextView nameView;
    private TextView emailView;
    private Button saveBtn;
    private Button cancelBtn;

    private static Contact editContact;

    public ContactEditView(Context context) {
        super(context);
        init(context);
    }

    public ContactEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContactEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_contact_edit, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        nameView = (TextView) findViewById(R.id.contact_edit_name);
        emailView = (TextView) findViewById(R.id.contact_edit_email);
        saveBtn = (Button) findViewById(R.id.contact_edit_save_btn);
        cancelBtn = (Button) findViewById(R.id.contact_edit_cancel_btn);

        contactEditPlaceListener = new PlaceListener<ContactEditPlace>() {
            @Override
            public void onPlace(ContactEditPlace place) {
                load(place.getData());
            }
        };
        okuki.addPlaceListener(contactEditPlaceListener);

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    private void load(int contactId) {
        Contact contact = ContactsDataManager.getContact(contactId);
        if (contact != null) {
            if (editContact == null || editContact.getId() != contact.getId()) {
                editContact = contact.clone();
            }
            nameView.setText(editContact.getName());
            emailView.setText(editContact.getEmail());
        } else {
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            goBack();
        }
    }

    private void save() {
        if (editContact != null) {
            updateEditCopy();
            ContactDetailsPlace detailsPlace = new ContactDetailsPlace(editContact.getId());
            ContactsDataManager.saveContact(editContact);
            editContact = null;
            okuki.gotoPlace(detailsPlace, HistoryAction.TRY_BACK_TO_SAME);
        } else {
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            goBack();
        }
    }

    private void goBack() {
        editContact = null;
        okuki.goBack();
    }

    private void updateEditCopy() {
        if (editContact != null) {
            editContact.setName((nameView.getText().toString()));
            editContact.setEmail(emailView.getText().toString());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        updateEditCopy();
        okuki.removePlaceListener(contactEditPlaceListener);
        super.onDetachedFromWindow();
    }

}
