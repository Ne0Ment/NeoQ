package org.neoment.classes;

import java.util.*;

public class Queue {
    String name; // name of the q
    String owner; // telegram id of the owner
    String home; // id of the telegram message from which it was created
    Long chatId; // id of the chat in which it was created
    Integer messageId; // id of message that created the q
    List<Person> line; // a list of ppl in the q
    List<String> bannedNicks; // a list of banned nicks

    public Queue(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.line = new ArrayList<Person>();
        this.bannedNicks = new ArrayList<String>();
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public void setOwner(String newOwner) {
        this.owner = newOwner;
    }

    public Integer getIdByNick(String personNick) {
        int personId = -1;
        for (int i=0; i<this.line.size(); i++) { if (this.line.get(i).getNick().equals(personNick)) { personId = i; break; } }
        return personId;
    }

    public void deletePerson(int personId) {
        if (personId>=0 && personId<this.line.size()) this.line.remove(personId);
    }

    public void deletePerson(String personNick) {
        int personId = this.getIdByNick(personNick);
        if (personId != -1) this.deletePerson(personId);
    }

    public void banPerson(int personId) {
        if (this.line.size()>personId && personId>-1) {
            this.bannedNicks.add(this.line.get(personId).getNick());
            this.deletePerson(personId);
        }
    }

    public void banPerson(String personNick) {
        this.bannedNicks.add(personNick);
        int personId = this.getIdByNick(personNick);
        this.banPerson(personId);
    }

    public void unbanPerson(String personNick) {
        this.bannedNicks.remove(personNick);
    }

    public void switchPeople(int i1, int i2) {
        if (i1>=0 && i1<this.line.size() && i2>=0 && i2<this.line.size()) {
            Collections.swap(this.line, i1, i2);
        }
    }

    public void switchPeople(String nick1, String nick2) {
        int id1 = this.getIdByNick(nick1);
        int id2 = this.getIdByNick(nick2);
        if (id1!=-1 && id2!=-1) this.switchPeople(id1, id2);
    }

    public void addPerson(Person person) {
        boolean personExists = this.line.stream().anyMatch(o -> Objects.equals(o.getId(), person.getId()));
        boolean personBanned = this.bannedNicks.stream().anyMatch(o -> Objects.equals(o, person.getNick()));
        if (!personExists && !personBanned) this.line.add(person);
    }

    public void setOrigin(String origin) {
        this.home = origin;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public Boolean checkOwner(String personNick) {
        return Objects.equals(this.owner, personNick);
    }

    public Boolean checkHome(String check)  {
        return this.home.equals(check);
    }

    // GET FUNCTIONS
    public List<Person> getLine() {
        return this.line;
    }

    public String getName() {
        return this.name;
    }

    public Long getChatId() { return this.chatId; }

    public Integer getMessageId() { return this.messageId; }
}