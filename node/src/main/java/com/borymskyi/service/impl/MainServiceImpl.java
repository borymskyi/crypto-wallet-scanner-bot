package com.borymskyi.service.impl;

import com.borymskyi.dao.AppUserDAO;
import com.borymskyi.dao.RawDataDAO;
import com.borymskyi.entity.AppUser;
import com.borymskyi.entity.RawData;
import com.borymskyi.entity.enums.UserState;
import com.borymskyi.service.MainService;
import com.borymskyi.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.borymskyi.entity.enums.UserState.BASIC_STATE;

/**
 * @author Dmitrii Borymskyi
 * @version 1.0
 */
@Service
public class MainServiceImpl implements MainService {
    private final RawDataDAO rowDataDAO;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;

    @Autowired
    public MainServiceImpl(RawDataDAO rowDataDAO, ProducerService producerService, AppUserDAO appUserDAO) {
        this.rowDataDAO = rowDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRowData(update);
        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
        var appUser = findOrSaveAppUser(telegramUser);

        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText("Node: Hello from node!");
        producerService.producerAnswer(sendMessage);
    }

    private AppUser findOrSaveAppUser(User telegramUser) {
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }

        return persistentAppUser;
    }

    private void saveRowData(Update update) {
        RawData rowData = RawData.builder()
                .event(update)
                .build();

        rowDataDAO.save(rowData);
    }
}
