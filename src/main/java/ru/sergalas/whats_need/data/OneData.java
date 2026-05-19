package ru.sergalas.whats_need.data;

import ru.sergalas.whats_need.enums.RoleEnum;

import java.util.List;

public record OneData(String title, List<EntryChatData> contents) {
}
