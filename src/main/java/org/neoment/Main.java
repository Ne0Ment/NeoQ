package org.neoment;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.response.BaseResponse;
import org.neoment.classes.*;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String botToken = System.getenv("neoqToken");
        System.out.println(botToken);
        QueueHandler qHandler = new QueueHandler();
        TelegramBot bot = new TelegramBot(botToken);
        TelegramHandler teleHandler = new TelegramHandler(bot);

        String godId = "867065390";

        bot.setUpdatesListener(updates -> {
            for (Update upd : updates) {
                if (upd.message()!=null) {
                    if (upd.message().text().equals("")) continue;
                    if (!upd.message().text().startsWith("/")) continue;
                    List<String> argsList = Arrays.asList(upd.message().text().trim().split("\\s+"));
                    Person sender = utils.personFromMessage(upd.message());

                    if (upd.message()!=null) {
                        switch (argsList.get(0)) {
                            case "/create" -> {
                                String qName = argsList.size()==1 ? "default" : String.join(" ", argsList.subList(1, argsList.size()));
                                Queue q = new Queue(qName, sender.getNick());

                                Message sent = teleHandler.replyQueueCreation(q, upd.message());

                                q.setOrigin(utils.msgOrigin(sent));
                                q.setChatId(sent.chat().id());
                                q.setMessageId(sent.messageId());
                                qHandler.addQueue(q);
                            }
                            case "/switch", "/remove", "/ban", "/unban", "/owner", "/rename" -> {
                                if (upd.message().replyToMessage()==null) break;

                                Queue q = qHandler.getQueueByHome(utils.msgOrigin(upd.message().replyToMessage()));
                                if (q==null) break;

                                if (q.checkOwner(sender.getNick()) || sender.getId().equals(godId)) {
                                    switch (argsList.get(0)) {
                                        case "/switch" -> {
                                            if (argsList.size()<3) break;

                                            if (argsList.get(1).startsWith("@")) {
                                                q.switchPeople(argsList.get(1).substring(1), argsList.get(2).substring(1));
                                            } else {
                                                q.switchPeople(Integer.parseInt(argsList.get(1))-1, Integer.parseInt(argsList.get(2))-1);
                                            }

                                        }
                                        case "/remove" -> {
                                            if (argsList.size()==1) break;
                                            if (argsList.get(1).startsWith("@")) {
                                                q.deletePerson(argsList.get(1).substring(1));
                                            } else {
                                                q.deletePerson(Integer.parseInt(argsList.get(1))-1);
                                            }
                                        }
                                        case "/ban" -> {
                                            if (argsList.size()==1) break;
                                            if (argsList.get(1).startsWith("@")) {
                                                q.banPerson(argsList.get(1).substring(1));
                                            } else {
                                                q.banPerson(Integer.parseInt(argsList.get(1))-1);
                                            }
                                        }
                                        case "/unban" -> {
                                            if (argsList.size()==1) break;
                                            q.unbanPerson(argsList.get(1).substring(1));
                                        }
                                        case "/owner" -> {
                                            if (argsList.size()==1) break;
                                            q.setOwner(argsList.get(1).substring(1));
                                        }
                                        case "/rename" -> {
                                            String qName = argsList.size()==1 ? "default" : String.join(" ", argsList.subList(1, argsList.size()));
                                            q.setName(qName);
                                        }
                                    }
                                }
                                teleHandler.updateQueueMessage(q);
                            }
                            default -> {}
                        }
                    }
                } else if (upd.callbackQuery()!=null) {
                    Queue q = qHandler.getQueueByHome(utils.msgOrigin(upd.callbackQuery().message()));
                    Person sender = utils.personFromUser(upd.callbackQuery().from());
                    if (q==null) break;
                    switch (upd.callbackQuery().data()) {
                        case "join" -> {
                            q.addPerson(utils.personFromUser(upd.callbackQuery().from()));
                        }
                        case "leave" -> {
                            q.deletePerson(upd.callbackQuery().from().username());
                        }
                        case "next" -> {
                            if (q.checkOwner(sender.getNick()) || sender.getId().equals(godId)) {
                                Person firstPerson = q.getLine().get(0);
                                q.deletePerson(0);
                                q.addPerson(firstPerson);
                            }
                        }
                        case "previous" -> {
                            if (q.checkOwner(sender.getNick()) || sender.getId().equals(godId)) {
                                Person lastPerson = q.getLine().get(q.getLine().size()-1);
                                q.deletePerson(q.getLine().size()-1);
                                q.addFirstPerson(lastPerson);
                            }
                        }
                        default -> {}
                    }
                    teleHandler.updateQueueMessage(q);
                    BaseResponse response = bot.execute(new AnswerCallbackQuery(upd.callbackQuery().id()));
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}

//TODO: юзеры могут меняться друг с другом
//TODO: переделать /next на inline кнопку