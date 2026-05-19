package ru.sergalas.whats_need.mapper;

import org.mapstruct.Mapper;
import ru.sergalas.whats_need.data.OneData;
import ru.sergalas.whats_need.entity.Chat;

@Mapper(componentModel = "spring")
public interface OneChatMapper {

    OneData toData(Chat chat);
}
