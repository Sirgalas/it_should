package ru.sergalas.whats_need.enums;

import javax.swing.plaf.PanelUI;
import java.util.Arrays;

public enum RoleEnum {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");
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

}
