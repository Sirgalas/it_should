package ru.sergalas.whats_need.mapper;

import org.mapstruct.Mapper;
import ru.sergalas.whats_need.data.CreateChatData;
import ru.sergalas.whats_need.data.OneData;
import ru.sergalas.whats_need.data.SidebarData;
import ru.sergalas.whats_need.entity.Chat;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    SidebarData toSidebarData(Chat chat);
    OneData toOneData(Chat chat);
    Chat toEntityCreate(CreateChatData data);
}
