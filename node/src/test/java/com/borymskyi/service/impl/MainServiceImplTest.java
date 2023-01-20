package com.borymskyi.service.impl;

import com.borymskyi.dao.RawDataDAO;
import com.borymskyi.entity.RawData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dmitrii Borymskyi
 * @version 1.0
 */

@SpringBootTest
class MainServiceImplTest {
    @Autowired
    private RawDataDAO rawDataDAO;

    @Test
    public void testSaveRawData() {
        Update update = new Update();
        Message msg = new Message();

        msg.setText("awd");
        update.setMessage(msg);

        RawData rawData = RawData.builder()
                .event(update)
                .build();
        Set<RawData> testDataSet = new HashSet<>();

        testDataSet.add(rawData);
        rawDataDAO.save(rawData);

        Assert.isTrue(testDataSet.contains(rawData), "Entity not found in the set");
    }
}