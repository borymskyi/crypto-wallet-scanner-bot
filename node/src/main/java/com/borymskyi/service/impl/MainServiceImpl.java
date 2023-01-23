package com.borymskyi.service.impl;

import com.borymskyi.dao.AppUserDAO;
import com.borymskyi.dao.RawDataDAO;
import com.borymskyi.entity.AppUser;
import com.borymskyi.entity.RawData;
import com.borymskyi.entity.enums.UserState;
import com.borymskyi.service.MainService;
import com.borymskyi.service.ProducerService;
import com.borymskyi.service.enums.ServiceCommands;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.borymskyi.entity.enums.UserState.BASIC_STATE;
import static com.borymskyi.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static com.borymskyi.service.enums.ServiceCommands.*;

/**
 * @author Dmitrii Borymskyi
 * @version 1.0
 */
@Log4j
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
        AppUser appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";

        if (CANCEL.equals(text)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO add process email;
        } else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }
        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRowData(update);
        AppUser appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        //TODO add saving document
        var answer = "Документ успешно загружен! Ссылка для скачивания: https://test.com/get-photo/777";
        sendAnswer(answer, chatId);
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getState();
        if (!appUser.getIsActive()) {
            var error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента.";
            sendAnswer(error, chatId);
            return false;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "Отмените текущую команду с помощью /cancel для отправки файлов.";
            return true;
        }
        return false;
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRowData(update);
        AppUser appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        //TODO add saving photo
        var answer = "Фото успешно загружено! Ссылка для скачивания: https://test.com/get-photo/777";
        sendAnswer(answer, chatId);
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        if (REGISTRATION.equals(cmd)) {
            //TODO add registration
            return "Временно недоступно.";
        } else if (HELP.equals(cmd)) {
            return help();
        } else if (START.equals(cmd)) {
            return "Приветствую! Чтобы посмотреть список доступных команд ведите /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
        }
    }

    private String help() {
        return "Список доступных команд: \n"
                + "/cancel - отмена выполнения текущей команды\n"
                + "/registration - регистрация пользователя";
    }

    //устанавливает текущему пользователю базовый статус и сохранять обновленные данные в бд.
    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена!";
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
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
