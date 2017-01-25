package okuki.sample.contacts.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private Contact contact;

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

        final TextView nameView = (TextView) findViewById(R.id.contact_edit_name);
        final TextView emailView = (TextView) findViewById(R.id.contact_edit_email);
        final Button saveBtn = (Button) findViewById(R.id.contact_edit_save_btn);
        final Button cancelBtn = (Button) findViewById(R.id.contact_edit_cancel_btn);

        contactEditPlaceListener = new PlaceListener<ContactEditPlace>() {
            @Override
            public void onPlace(ContactEditPlace place) {
                contact = ContactsDataManager.getContact(place.getData());
                nameView.setText(contact.getName());
                emailView.setText(contact.getEmail());
            }
        };
        okuki.addPlaceListener(contactEditPlaceListener);

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                contact.setName((nameView.getText().toString()));
                contact.setEmail(emailView.getText().toString());
                ContactsDataManager.saveContact(contact);
                okuki.gotoPlace(new ContactDetailsPlace(contact.getId()), HistoryAction.TRY_BACK_TO_SAME);
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                okuki.goBack();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        okuki.removePlaceListener(contactEditPlaceListener);
        super.onDetachedFromWindow();
    }

}
