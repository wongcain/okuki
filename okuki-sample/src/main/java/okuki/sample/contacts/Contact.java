package okuki.sample.contacts;

public class Contact implements Comparable<Contact>, Cloneable {

    private int id;
    private String name;
    private String email;

    public Contact() {
    }

    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Contact contact) {
        return id - contact.id;
    }

    public Contact clone() {
        return new Contact(id, name, email);
    }

}
