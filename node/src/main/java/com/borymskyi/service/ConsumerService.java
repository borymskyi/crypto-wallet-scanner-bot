package com.borymskyi.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {

    void consumerTextMessageUpdates(Update update);
    void consumerDocumentMessageUpdates(Update update);
    void consumerPhotoMessageUpdates(Update update);
}
