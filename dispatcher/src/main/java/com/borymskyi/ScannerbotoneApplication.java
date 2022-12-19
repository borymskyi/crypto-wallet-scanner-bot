package com.borymskyi;

import com.borymskyi.controllers.FirstScannerBot;
import com.borymskyi.controllers.UpdateController;
import com.borymskyi.service.impl.UpdateProducerImpl;
import com.borymskyi.utils.MessageUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class ScannerbotoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScannerbotoneApplication.class, args);

		try {
			TelegramBotsApi botAPI = new TelegramBotsApi(DefaultBotSession.class);

			botAPI.registerBot(new FirstScannerBot(new UpdateController(new MessageUtils(), new UpdateProducerImpl())));

		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

	}
}
