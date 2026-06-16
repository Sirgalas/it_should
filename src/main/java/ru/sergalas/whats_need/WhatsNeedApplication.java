package ru.sergalas.whats_need;

import org.springframework.ai.model.chat.memory.repository.jdbc.autoconfigure.JdbcChatMemoryRepositoryAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { JdbcChatMemoryRepositoryAutoConfiguration.class })
public class WhatsNeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsNeedApplication.class, args);
    }


}
