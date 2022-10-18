package org.neoment.classes;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class TelegramHandler {
    TelegramBot bot;
    public TelegramHandler(TelegramBot bot) {
        this.bot = bot;
    }

    private InlineKeyboardMarkup creationMarkup() {
        //return new InlineKeyboardMarkup(new InlineKeyboardButton("Записаться").callbackData("join"),
        //        new InlineKeyboardButton("Отписаться").callbackData("leave"));
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[] {
                        new InlineKeyboardButton("Записаться").callbackData("join"),
                        new InlineKeyboardButton("Отписаться").callbackData("leave")
                },
                new InlineKeyboardButton[] {
                        new InlineKeyboardButton("Следующий").callbackData("next"),
                });

    }

    public Message replyQueueCreation(Queue q, Message creationMessage) {

        SendMessage request = new SendMessage(creationMessage.chat().id(), utils.queueCreationMessage(q))
                .parseMode(ParseMode.MarkdownV2)
                .replyToMessageId(creationMessage.messageId())
                .replyMarkup(this.creationMarkup());

        SendResponse sendResponse = this.bot.execute(request);
        return sendResponse.message();
    }

    public void updateQueueMessage(Queue q) {
        EditMessageText editMessageText = new EditMessageText(q.getChatId(), q.getMessageId(), utils.queueCreationMessage(q))
                .parseMode(ParseMode.MarkdownV2)
                .replyMarkup(this.creationMarkup());

        BaseResponse response = bot.execute(editMessageText);
    }
}
