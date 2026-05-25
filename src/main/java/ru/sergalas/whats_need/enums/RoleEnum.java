package ru.sergalas.whats_need.enums;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import javax.swing.plaf.PanelUI;
import java.util.Arrays;

public enum RoleEnum {
    USER("user") {
        @Override
        public Message getMessage(String prompt) {
            return new UserMessage(prompt);
        }
    },
    ASSISTANT("assistant") {
        @Override
        public Message getMessage(String prompt) {
            return new AssistantMessage(prompt);
        }
    },
    SYSTEM("system") {
        @Override
        public Message getMessage(String prompt) {
            return new SystemMessage(prompt);
        }
    };
    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public static RoleEnum getRoleByName(String roleName) {
        return Arrays.stream(RoleEnum.values()).filter(role -> role.role.equals(roleName)).findFirst().orElseThrow();
    }

    public String getRole() {
        return role;
    }

    public abstract Message getMessage(String prompt);
}
