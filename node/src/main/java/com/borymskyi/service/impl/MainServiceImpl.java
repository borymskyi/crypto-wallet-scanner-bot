package com.borymskyi.service.impl;

import com.borymskyi.dao.RawDataDAO;
import com.borymskyi.entity.RawData;
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
    private final RawDataDAO rowDataDAO;
    private final ProducerService producerService;

    @Autowired
    public MainServiceImpl(RawDataDAO rowDataDAO, ProducerService producerService) {
        this.rowDataDAO = rowDataDAO;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRowData(update);

        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText("Node: Hello from node!");
        producerService.producerAnswer(sendMessage);
    }

    private void saveRowData(Update update) {
        RawData rowData = RawData.builder()
                .event(update)
                .build();

        rowDataDAO.save(rowData);
    }
}
