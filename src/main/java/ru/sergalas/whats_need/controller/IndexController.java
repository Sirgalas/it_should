package ru.sergalas.whats_need.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sergalas.whats_need.data.SidebarData;
import ru.sergalas.whats_need.services.ChatService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class IndexController {
    private final ChatService service;

    @ModelAttribute("sidebar")
    public List<SidebarData> getSideBar(){
        return service.getSideBar();
    }

    @GetMapping()
    public String index() {
        return "chat/index";
    }
}
