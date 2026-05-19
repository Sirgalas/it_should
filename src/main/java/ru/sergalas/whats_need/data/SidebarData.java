package ru.sergalas.whats_need.data;

import java.util.UUID;

public record SidebarData(
        UUID id,
        String name,
        String url,
        Boolean active
) {

}
