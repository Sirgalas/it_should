package ru.sergalas.whats_need;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WhatsNeedApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WhatsNeedApplication.class, args);
        //ChatClient chatClient = context.getBean(ChatClient.class);
        //System.out.println(chatClient.prompt().user("дай текст гимна Amazing Grace на русском языке ").call().content());
    }


}
