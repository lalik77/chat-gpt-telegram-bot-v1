
# Building a Telegram Bot with ChatGPT Integration Using Spring Boot 2.5.2


This repository features a concise tutorial on building a basic Telegram bot. Learn how to seamlessly integrate it 
with Spring Boot 2.5.2 and leverage the functionality of the [OpenAI-Java](https://github.com/TheoKanning/openai-java) 
library. Follow along step-by-step to create your own interactive bot capable of interacting intelligently with users.


At the end of this tutorial, you'll have a fully functional Telegram bot similar to the one described in the 
upcoming video.

[![Telegram Bot V1](img/run/youtube-thumb.png)](https://youtu.be/FobWCI5j1uM "Telegram Bot V1")

As you see , the answer received by the bot is not complete. I think it's due to the  OpenAI-Java library.
I will make another same repo with another implementation. 

As you can observe, the response received by the bot appears to be incomplete. I suspect this issue may be related to 
the OpenAI-Java library. Therefore, I plan to create a new repository with a different implementation.


### 1 - Create the project
In Idea create a new Maven project
![](img/create-project/create-maven-project-1.png)

![](img/create-project/create-maven-project-2.png)

We will have a pom.xml file which we will change.
![](img/create-project/create-maven-project-3.png)

Editing the pom.xml file
![](img/create-project/edit-maven-pom-file.png)

Create main class , the entry point of our application

![](img/create-project/create-main-class.png)



### 2 - BotFather
You have to find the BotFather in Telegram.
Pay attention to the official BotFather
![](img/bot-father/bot-father-1.png)

![](img/bot-father/create-new-bot.png)

![](img/bot-father/create-new-bot-2.png)


### 3 - Add Telegram Dependency
Edit pom.xml file
Add Telegram and lombok  dependencies

![](img/telegram-dependency/pom-xml.png)


### 4 - Creating our bot

Two ways to create bots (Polling and Webhooks)
I will touch the Polling.
Create new package `controller` and a new class `TelegramBot`that will
extend a class from Telegram `TelegramLongPollingBot` and override three methods

![](img/create-bot-1/telegram-bot-class.png)

After the app started 

![](img/create-bot-1/app-runing-1.png)


### 5 - Implementing The Telegram ChatGPT Bot Version 1

 1 - First, let's refactor and export token and username to `application.properties`
file. Let's create it in `resources` folder .

![](img/create-bot-2/application-properties.png)

 2 - Remove hardcoded data from `TelegramBot` class

![](img/create-bot-2/telegaram-bot-class-1.png)

 3 - Write implementation 

![](img/create-bot-2/on-update-received-1.png)

private methods

![](img/create-bot-2/private-methods.png)

running the app

![](img/create-bot-2/app-running-1.png)

Now let's write code using OpenAi

We have to generate an OpenAI API key.
If you don't have one, you can sign up for it on the OpenAI website.
![](img/create-bot-2/generating-openai-api-key-1.png)

I have found a library on git but it seems that his library does not work fine.
Add new dependency in `pom.xml` file.

![](img/create-bot-2/theokanning-lib.png)

Let's remove hardcoded tokens from `.properties` file and set it to env variables.
I will not show you how to do it you can google it yourself.

![](img/create-bot-2/env-variables.png)

Now we will add configuration to Openai API

![](img/create-bot-2/open-ai-config.png)

![](img/create-bot-2/open-ai-config-2.png)

Now let's inject `OpenAiService` bean in our `TelegramBot`

![](img/create-bot-2/telegaram-bot-class-2.png)

 Let's implement  `getResponseFromChatGPT()`

![](img/create-bot-2/get-response-from-gpt.png)

Before finish, let's make our bot more beautiful and functional.
Inside method `onUpdateReceived()` , we will write three commands that our 
bot will accept.

![](img/create-bot-2/on-update-received-2.png)

Static text
![](img/create-bot-2/static-text.png)
Method chatGptCommandReceived()
![](img/create-bot-2/chat-gpt-command-received.png)
Method getResponseFromChatGpt()
![](img/create-bot-2/get-response-from-chat-gpt.png)
Method sendTextMessage()
![](img/create-bot-2/send-text-message.png)
Method processPromptResponse()
![](img/create-bot-2/process-prompt-response.png)

Now we will add menu like this.

![](img/create-bot-2/create-menu-1.png)

For this we will export configs for Telegram Bot to a separate class and inject it into `TelegramBot` class.
Create a new `BotConfig.java`  class.
![](img/create-bot-2/bot-config.png)
Make some changes in `TelegramBot.java` class.
![](img/create-bot-2/telegram-bot-class-3.png)
![](img/create-bot-2/telegram-bot-class-4.png)

Let's run the App.
![](img/run/run-1.png)

[![Telegram Bot V1](img/run/youtube-thumb.png)](https://youtu.be/FobWCI5j1uM "Telegram Bot V1")