import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;


public class Bot extends TelegramLongPollingBot {
    private static final Random random = new Random();
    private long chat_id;
    private final Jokes jokes = new Jokes();

    public Bot() throws IOException, ParserConfigurationException, SAXException {
    }

    @Override
    public String getBotUsername() {
        return "chattyaddaibot";
    }
    @Override
    public String getBotToken() {
        return "2028072573:AAGvHvZju0TQ_k402x2mm9q2-lGHmpEBrN0";
    }
    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText().trim().toLowerCase(Locale.ROOT);
            chat_id = update.getMessage().getChatId();

            try{
                switch (message_text){
                    case "/start":
                        sendMsg("Hi, I'm Chatty! \uD83E\uDD16\nI can tell jokes and exchange rates \uD83D\uDCB1\n" +
                                    "You will find out the available commands by typing /help \uD83E\uDD13\n" +
                                    "Also, you can get a link to the project by entering /source " +
                                "\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB\n" +
                                    "How can I help you? \uD83D\uDC40");
                        break;
                    case "/help":
                        TimeUnit.SECONDS.sleep(random.nextInt(2) + 1);
                        sendMsg("Examples of what I understand:\nCan you tell me a joke?\n" +
                                        "Tell me a joke\nConvert 42 US Dollar to Euro\nWomen" +
                                "\nHow much is 69 Hungarian Forint in S.African Rand?");
                        break;
                    case "/source":
                        sendMsg("https://github.com/ShishkinPavel/chattybot");
                        break;
                    case "women":
                        sendMsg("Lol no, i don't. I'm a bot, not a clairvoyant \uD83E\uDDD9\uD83C\uDFFB\u200D♂️");
                        break;
                    default:
                            sendMsg(getValue(message_text.toLowerCase(Locale.ROOT)));
                            break;
                        }
                    } catch (IOException | ParserConfigurationException | ParseException |
                    InterruptedException | SAXException e) {
                e.printStackTrace();
            }

        }
    }


    private void sendMsg(String msg) throws InterruptedException {

        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText(msg);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getValue(String s) throws IOException,
            ParserConfigurationException, SAXException, ParseException {

        // part for joke
        if (s.contains("tell me a joke")){
            return jokes.getJoke();
        }
        // no more jokes

        Currency currency = new Currency();
        HashMap<String, String> hashMap = currency.getHashMap();

        // replacing the full name with ISO. for example Czech Koruna -> CZK
        for (String key:
                hashMap.keySet()) {
            if (s.contains(key)){
                s = s.replace(key, hashMap.get(key));
            }
        }
        // Also, I need to replace my "base currency" and delete all [,.!?] from the end of the line
        s = s.replace("russian ruble", "RUB").replaceFirst("^[^a-zA-Z]+", "").
                replaceAll("\\s*\\p{Punct}+\\s*$", "");
        // kinda cringe, sorry
        Pattern pattern = Pattern.compile
                ("convert\\s+[+-]?([0-9]*[.|,])?[0-9]+\\s+[A-Za-z]{3}\\s+to\\s+[A-Za-z]{3}|"+
                        "how\\s+much\\s+is\\s+[+-]?([0-9]*[.|,])?[0-9]+\\s+[A-Za-z]{3}\\s+in\\s+[A-Za-z]{3}");

        Matcher matcher = pattern.matcher(s);
        if (matcher.find()){
            return generateCurrency(s, currency);
        }
        return "I don't understand you:( \nTry to send me /help if you have any problems";
    }

    private String generateCurrency(String s, Currency currency) throws ParseException {

        // I just break into words and reverse the list,
        // because you always need to work with the end of the line
        List<String> list = Arrays.asList(s.split("\\s+"));
        Collections.reverse(list);
        currency.setCurrency(list.get(0));
        double cur = currency.getCurrency();
        double amount = NumberFormat.getNumberInstance(Locale.FRANCE).parse(list.get(3)).doubleValue();
        if (Objects.equals(list.get(2).toUpperCase(Locale.ROOT), "RUB")){
            return String.format("%.2f",amount / cur) + " " + list.get(0);
        } else {
            double first = amount / cur;
            currency.setCurrency(list.get(2));
            double second = amount / currency.getCurrency();
            return String.format("%.2f",(amount * first/second)) + " " + list.get(0).toUpperCase(Locale.ROOT);
        }
    }

}
