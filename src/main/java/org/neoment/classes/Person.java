package org.neoment.classes;

public class Person {
    String teleId, teleNick, teleName;

    public Person(String teleId, String teleNick, String teleName) {
        this.teleId = teleId;
        this.teleNick = teleNick;
        this.teleName = teleName;
    }

    public String getId() {
        return this.teleId;
    }

    public String getNick() {
        return this.teleNick;
    }

    public String getName () {
        return this.teleName;
    }
}
