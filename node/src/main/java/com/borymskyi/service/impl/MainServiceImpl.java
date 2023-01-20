package com.borymskyi.service.impl;

import com.borymskyi.dao.RowDataDAO;
import com.borymskyi.service.MainService;
import com.borymskyi.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Dmitrii Borymskyi
 * @version 1.0
 */
@Service
public class MainServiceImpl implements MainService {
    private final RowDataDAO rowDataDAO;
    private final ProducerService producerService;

    @Autowired
    public MainServiceImpl(RowDataDAO rowDataDAO, ProducerService producerService) {
        this.rowDataDAO = rowDataDAO;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRowData(update);

        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from NODE");

        producerService.producerAnswer(sendMessage);
    }

    private void saveRowData(Update update) {
        com.borymskyi.entity.RowData rowData = com.borymskyi.entity.RowData.builder()
                .event(update)
                .build();

        rowDataDAO.save(rowData);
    }
}
