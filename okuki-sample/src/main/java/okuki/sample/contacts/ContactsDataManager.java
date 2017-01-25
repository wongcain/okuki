package okuki.sample.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsDataManager {

    private static final Map<Integer, Contact> CONTACTS = new HashMap<>();

    static {
        CONTACTS.put(1, new Contact(1, "David Bowie", "david@davidbowie.com"));
        CONTACTS.put(2, new Contact(2, "Leonard Cohen", "leonard@leonardcohn.com"));
        CONTACTS.put(3, new Contact(3, "Sharon Jones", "sharon@sharonjones.com"));
    }

    public static List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>(CONTACTS.values());
        Collections.sort(contacts);
        return contacts;
    }

    public static Contact getContact(int id) {
        return CONTACTS.get(id);
    }

    public static void saveContact(Contact contact) {
        CONTACTS.put(contact.getId(), contact);
    }
}
