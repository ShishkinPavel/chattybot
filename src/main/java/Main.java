import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class Main {
    public static void main(String[] args){

        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register bot
        try {
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException | IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


}