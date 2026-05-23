package ru.sergalas.whats_need.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sergalas.whats_need.data.CreateChatData;
import ru.sergalas.whats_need.data.EditChatData;
import ru.sergalas.whats_need.data.PromptData;
import ru.sergalas.whats_need.data.SidebarData;
import ru.sergalas.whats_need.services.ChatService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("chat")
public class ChatController {

    private final ChatService service;

    @ModelAttribute("sidebar")
    public List<SidebarData> getSideBar(){
        return service.getSideBar();
    }

    @GetMapping("{id}")
    public String one(@PathVariable UUID id, Model model) {
        model.addAttribute("chat", service.getOneData(id));
        model.addAttribute("promtData", new PromptData(null));
        model.addAttribute("id", id);
        return "chat/one";
    }

    @PostMapping("new")
    public String newChat(@ModelAttribute @Valid CreateChatData data)
    {
        return "redirect:/chat/%s".formatted(service.create(data).toString());
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        model.addAttribute("id",id);
        model.addAttribute("chat", service.getEditChat(id));
        return  "chat/edit";
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable UUID id, @ModelAttribute EditChatData data) {
        service.editChat(id,data);
        return "redirect:/chat/%s".formatted(id);
    }

    @PostMapping("delete/{id}")
    public  String delete(@PathVariable UUID id) {
        service.deleteChat(id);
        return "redirect:/";
    }

    @PostMapping("{id}/entry")
    public String setTalk(@ModelAttribute PromptData data, @PathVariable UUID id) {
        service.sentQuestion(data, id);
        return "redirect:/chat/%s".formatted(id);
    }

}
