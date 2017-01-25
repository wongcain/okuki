package okuki.sample.contacts.details;

import okuki.Place;
import okuki.PlaceConfig;
import okuki.sample.contacts.ContactsPlace;

@PlaceConfig(parent = ContactsPlace.class)
public class ContactDetailsPlace extends Place<Integer> {

    public ContactDetailsPlace(Integer data) {
        super(data);
    }

}
