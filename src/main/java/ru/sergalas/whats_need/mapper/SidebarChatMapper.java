package ru.sergalas.whats_need.mapper;

import org.mapstruct.Mapper;
import ru.sergalas.whats_need.data.SidebarData;
import ru.sergalas.whats_need.entity.Chat;

@Mapper(componentModel = "spring")
public interface SidebarChatMapper {
    SidebarData toData(Chat chat);
}
