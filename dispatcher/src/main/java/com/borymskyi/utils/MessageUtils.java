package com.borymskyi.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUtils {
    public SendMessage generateSendMessageWithText(Update update, String text) {
        var sendMessage = new SendMessage();

        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(text);

        return sendMessage;
    }
}
