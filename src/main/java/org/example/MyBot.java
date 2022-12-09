package org.example;

import org.example.utils.DifficultyLvl;
import org.example.utils.ListOfQuestions;
import org.example.utils.SettingsReader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

/**
 * @author Dmitry
 */
public class MyBot extends TelegramLongPollingBot {

    private InlineKeyboardMarkup keyboardM1;
    private InlineKeyboardMarkup keyboardM2;
    private InlineKeyboardMarkup keyboardM3;
    private InlineKeyboardMarkup keyboardM4;

    private List<CardQuestion> questions;

    private final String botUsername;
    private final String botToken;
    private int currentQuestionIndex;
    private String difficultyLvl;
    private int firstQuestionIndex;



    public MyBot() {
        botUsername = SettingsReader.getBotUsername();
        botToken = SettingsReader.getBotToken();


        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text(DifficultyLvl.LVL1.getText())
                .callbackData("1")
                .build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                .text(DifficultyLvl.LVL2.getText())
                .callbackData("2")
                .build();
        InlineKeyboardButton button3 = InlineKeyboardButton.builder()
                .text(DifficultyLvl.LVL3.getText())
                .callbackData("3")
                .build();
        keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(button1, button2, button3))
                .build();

        InlineKeyboardButton buttonTrue = InlineKeyboardButton.builder()
                .text("Правда!")
                .callbackData("truth")
                .build();
        InlineKeyboardButton buttonAction = InlineKeyboardButton.builder()
                .text("Действие!")
                .callbackData("action")
                .build();
        keyboardM2 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(buttonTrue, buttonAction))
                .build();

        InlineKeyboardButton buttonNextQuestion = InlineKeyboardButton.builder()
                .text("Следующий вопрос")
                .callbackData("next_question")
                .build();
        keyboardM3 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(buttonNextQuestion))
                .build();

        InlineKeyboardButton buttonYes = InlineKeyboardButton.builder()
                .text("Да")
                .callbackData("yes")
                .build();
        InlineKeyboardButton buttonNo = InlineKeyboardButton.builder()
                .text("Нет")
                .callbackData("no")
                .build();
        keyboardM4 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(buttonYes, buttonNo))
                .build();
    }

    private void initQuestions(DifficultyLvl lvl) {
        questions = ListOfQuestions.getQuestionList(lvl);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        SendMessage sm = new SendMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            sm.setChatId(update.getMessage().getChatId());
            String text = update.getMessage().getText();
            if (text.equals("/start")) {
                sm.setText("Выберите уровень сложности:");
                sm.setReplyMarkup(keyboardM1);
            } else if (text.equals("/help")) {
                System.out.println(getHelper());
            } else if (text.equals("/restart")) {
                sm.setText("Выберите уровень сложности:");
                sm.setReplyMarkup(keyboardM1);
            } else if (text.equals("/dif")) {
                sm.setText("Ваша сложность: " + difficultyLvl);
               // sm.setReplyMarkup(keyboardM2); //TODO
            } else if (text.equals("/change_dif")) {
                sm.setText("Выберите уровень сложности:");
                sm.setReplyMarkup(keyboardM1);
            } else {
                sm.setText("На всякие глупости не отвечаю!\nВыбирай уровень, либо ухади!");
            }
            //
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            try {
                String queryId = update.getCallbackQuery().getId();
                Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                buttonTap(chat_id, queryId, call_data, messageId);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void restart(SendMessage message) {
        //TODO
        message.setText("Выберите уровень сложности:");
        message.setReplyMarkup(keyboardM1);
    }

    private String getHelper() {
        //TODO вывести все команды и их краткие описания
        return "";
    }

    private int getNextQuestionIndex() {
        if (currentQuestionIndex == questions.size()-1) {
            currentQuestionIndex = 0;
            return currentQuestionIndex;
        }
        return ++currentQuestionIndex;
    }

    private String getRandomQuestion() {
        int randomIndex = new Random().nextInt(questions.size());
        currentQuestionIndex = randomIndex;
        firstQuestionIndex = randomIndex;
        return questions.get(randomIndex).getQuestion();
    }

    private void buttonTap(Long id, String queryId, String data, int msgId) throws TelegramApiException {

        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(id.toString()).messageId(msgId).build();

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        switch (data) {
            case "1" -> {
                difficultyLvl = DifficultyLvl.LVL1.getText();
                initQuestions(DifficultyLvl.LVL1);
                String text = "Вы выбрали сложность: " + difficultyLvl + '\n';
                text += "Вот ваш первый вопрос:\n\n" + getRandomQuestion();
                newTxt.setText(text);
                newKb.setReplyMarkup(keyboardM2);
            }
            case "2" -> {
                difficultyLvl = DifficultyLvl.LVL2.getText();
                initQuestions(DifficultyLvl.LVL2);
                String text = "Вы выбрали сложность: " + difficultyLvl + '\n';
                text += "Вот ваш первый вопрос:\n\n" + getRandomQuestion();
                newTxt.setText(text);
                newKb.setReplyMarkup(keyboardM2);
            }
            case "3" -> {
                difficultyLvl = DifficultyLvl.LVL3.getText();
                initQuestions(DifficultyLvl.LVL3);
                String text = "Вы выбрали сложность: " + difficultyLvl + '\n';
                text += "Вот ваш первый вопрос:\n\n" + getRandomQuestion();
                newTxt.setText(text);
                newKb.setReplyMarkup(keyboardM2);
            }
            case "truth" -> {
                SendMessage sendMessage = new SendMessage(id.toString(), "Отвечайте честно! В этом смысл игры!");
                sendMessage.setReplyMarkup(keyboardM3);
                execute(sendMessage);
                execute(close);
                return;
            }
            case "action" -> {
                SendMessage sendMessage = new SendMessage(id.toString(),
                        questions.get(currentQuestionIndex).getAction());
                sendMessage.setReplyMarkup(keyboardM3);
                execute(sendMessage);
                execute(close);
                return;
            }
            case "next_question" -> {
                int nextQuestionIndex = getNextQuestionIndex();
                if (nextQuestionIndex == firstQuestionIndex) {
                    SendMessage sendMessage = new SendMessage(id.toString(),
                            "Вы прошли все вопросы на данной сложности. Хотите изменить сложность?");
                    sendMessage.setReplyMarkup(keyboardM4);
                    execute(sendMessage);
                    execute(close);
                } else {
                    CardQuestion question = questions.get(nextQuestionIndex);
                    SendMessage sendMessage = new SendMessage(id.toString(), question.getQuestion());
                    sendMessage.setReplyMarkup(keyboardM2);
                    execute(sendMessage);
                    execute(close);
                    return;
                }
            }

            case "yes" -> {
                SendMessage sendMessage = new SendMessage(id.toString(),
                        "Выберите уровень сложности: ");
                sendMessage.setReplyMarkup(keyboardM1);
                execute(sendMessage);
                execute(close);
            }
            case "no" -> {
                SendMessage sendMessage = new SendMessage(id.toString(),
                        "Надеюсь, вы хорошо провели время. Всего доброго!");
                execute(sendMessage);
                execute(close);
            }
            default -> {
            }
        }
        execute(close);
        execute(newTxt);
        execute(newKb);
    }
}
