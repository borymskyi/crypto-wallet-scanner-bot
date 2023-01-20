package com.borymskyi.service;

import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {
    void produce(String rabbitQueue, Update update);
}
