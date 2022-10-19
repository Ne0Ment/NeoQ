package org.neoment.classes;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;

import java.text.MessageFormat;
import java.util.List;

public class utils {
    public static String msgOrigin(Message msg) {
        return msg.messageId().toString()+msg.chat().id().toString();
    }

    public static Person personFromUser(User user) {
        return new Person(user.id().toString(), user.username(), (user.firstName()==null ? "" : user.firstName()) + " " + (user.lastName()==null? "" : user.lastName()));
    }

    public static Person personFromMessage(Message msg) {
        return personFromUser(msg.from());
    }
    public static String queueCreationMessage(Queue q) {
        StringBuilder qStr = new StringBuilder(MessageFormat.format("Очередь <{0}\\>:\n", q.getName()));
        List<Person> line = q.getLine();

        if (line.size()==0) {
            qStr.append("   ").append("Никто не пришел на фан встречу\\.\\.\\.").append("\n");
        }
        for (int i=0; i<line.size(); i++) {
            qStr.append("   ").append(i + 1).append(": ").append(MessageFormat.format("[{0}](tg://user?id={1})", line.get(i).getName(), line.get(i).getId())).append("\n");
        }
        return qStr.toString();
    }
}
