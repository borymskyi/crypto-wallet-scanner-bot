package com.borymskyi.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Dmitrii Borymskyi
 * @version 1.0
 */
public interface MainService {
    void processTextMessage(Update update);
}
