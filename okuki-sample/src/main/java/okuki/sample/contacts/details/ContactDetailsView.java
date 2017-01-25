package okuki.sample.contacts.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import okuki.Okuki;
import okuki.PlaceListener;
import okuki.sample.R;
import okuki.sample.contacts.Contact;
import okuki.sample.contacts.ContactsDataManager;
import okuki.sample.contacts.edit.ContactEditPlace;

public class ContactDetailsView extends LinearLayout {

    private final Okuki okuki = Okuki.getDefault();

    private PlaceListener<ContactDetailsPlace> contactDetailsPlaceListener;
    private int contactId;

    public ContactDetailsView(Context context) {
        super(context);
        init(context);
    }

    public ContactDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContactDetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_contact_details, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        final TextView nameView = (TextView) findViewById(R.id.contact_details_name);
        final TextView emailView = (TextView) findViewById(R.id.contact_details_email);
        final Button editBtn = (Button) findViewById(R.id.contact_details_edit_btn);

        contactDetailsPlaceListener = new PlaceListener<ContactDetailsPlace>() {
            @Override
            public void onPlace(ContactDetailsPlace place) {
                Contact contact = ContactsDataManager.getContact(place.getData());
                nameView.setText(contact.getName());
                emailView.setText(contact.getEmail());
                contactId = contact.getId();
            }
        };
        okuki.addPlaceListener(contactDetailsPlaceListener);

        editBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                okuki.gotoPlace(new ContactEditPlace(contactId));
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        okuki.removePlaceListener(contactDetailsPlaceListener);
        super.onDetachedFromWindow();
    }

}
