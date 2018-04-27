import org.telegram.telegrambots.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
   private static Conf conf = new Conf();
   private static String BotToken = conf.getToken();
   private static String BotUserName = conf.getNamebot();

   private class LastLocation {
       private String [] locations;
       private int length;

       LastLocation(int n) {
           length = n;
           locations = new String[n];
           for(int i = 0; i < n; i++) {
               locations[i] = "last use";
           }
       }

       String getLocation(int i) {
           if(i >= 0 && i < length) {
               return locations[i];
           }
           return null;
       }
       void addLocation(String newLocation) {
           int i;
           for(i = 0; i < length - 1; i++) {
               locations[i] = locations[i + 1];
           }
           locations[i] = newLocation;
       }
   }

    @Override
    public String getBotToken() {
        return BotToken;
    }
    @Override
    public String getBotUsername() {
        return BotUserName;
    }

    private LastLocation lastLocation = new LastLocation(4);

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotApi = new TelegramBotsApi();

        try {
            telegramBotApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Model model = new Model();

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendMsg(message, "What location ?");
                    break;
                case "/help":
                    sendMsg(message, "You need help ?");
                    break;
                case "/settings":
                    sendMsg(message, "Setting done");
                    break;
                default:
                    try {
                        lastLocation.addLocation(message.getText());
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (URISyntaxException e) {
                        sendMsg(message, "URL Syntax Exception.");
                    } catch (IOException e) {
                        sendMsg(message, "NU am gasit localitatea : " + message.getText());
                    }
            }
        }

    }


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {

            setButtoms(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    public void setButtoms(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
//        keyboardFirstRow.add(new KeyboardButton("/help"));
//        keyboardFirstRow.add(new KeyboardButton("/settings"));
        for(int i = 0; i < lastLocation.length; i++) {
            keyboardFirstRow.add(new KeyboardButton(lastLocation.getLocation(i)));
        }
        keyboardRowList.add(keyboardFirstRow);

        // complet row buttom from /settings list
//        KeyboardRow keyboardSecondRow = new KeyboardRow();
//        keyboardSecondRow.add(new KeyboardButton("Chisinau"));
//        keyboardRowList.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }
}
