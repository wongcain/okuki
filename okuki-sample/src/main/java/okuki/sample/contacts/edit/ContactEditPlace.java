package okuki.sample.contacts.edit;

import okuki.Place;
import okuki.PlaceConfig;
import okuki.sample.contacts.ContactsPlace;

@PlaceConfig(parent = ContactsPlace.class)
public class ContactEditPlace extends Place<Integer> {

    public ContactEditPlace(Integer data) {
        super(data);
    }

}
