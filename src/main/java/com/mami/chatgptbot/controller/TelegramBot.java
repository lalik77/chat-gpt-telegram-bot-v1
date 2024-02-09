package com.mami.chatgptbot.controller;

import com.mami.chatgptbot.config.BotConfig;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private static final String HELP_TEXT = """
            This bot is designed to showcase Spring capabilities! You can interact with it using the following commands:
            /start: Displays a welcome message.
            /chatgpt: Initiates a prompt conversation with the bot.
            /help: Displays this message.
            """;
    private static final String PROMPT_MESSAGE = "Please type your prompt:";

    private final OpenAiService service;
    private final BotConfig botConfig;

    public TelegramBot(OpenAiService service, BotConfig botConfig) {
        this.service = service;
        this.botConfig = botConfig;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Get a welcome message."));
        listOfCommands.add(new BotCommand("/chatgpt", "Type your prompt."));
        listOfCommands.add(new BotCommand("/help", "Info how to use this bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("TelegramBot#TelegramBot(): {}", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        // Check if the update has a message
        if (update.hasMessage() && update.getMessage().hasText()) {

            // Handle  commands

            switch (messageText) {

                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/chatgpt":
                    chatGptCommandReceived(chatId);
                    break;
                case "/help":
                    sendTextMessage(chatId, HELP_TEXT);
                    break;
                default:
                    processPromptResponse(chatId, messageText);
            }
        }
    }

    private void startCommandReceived(long chatId, String firstName) {

        String answer = "Hi, " + firstName + ", nice to meet you!" +
                " I am an AI digital assistant created by the OpenAI team";
        sendTextMessage(chatId, answer);
    }

    private void chatGptCommandReceived(long chatId) {
        sendTextMessage(chatId, PROMPT_MESSAGE);
    }

    private String getResponseFromChatGPT(String message) {

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(message)
                .model("gpt-3.5-turbo-instruct")
                .echo(true)
                .build();
        final String result = service.createCompletion(completionRequest)
                .getChoices()
                .get(0)
                .getText()
                .strip();
        return result;
    }

    private void sendTextMessage(long chatId, String text) {
        try {
            execute(new SendMessage(String.format("%d", chatId), text));

        } catch (TelegramApiException e) {
            log.error("TelegramBot#sendTextMessage(): {}", e);
        }
    }

    private void processPromptResponse(long chatId, String question) {

        final String responseFromChatGPT = getResponseFromChatGPT(question);
        sendTextMessage(chatId, responseFromChatGPT);
    }
}
