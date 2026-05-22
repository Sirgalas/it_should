package ru.sergalas.whats_need.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.sergalas.whats_need.data.SidebarData;
import ru.sergalas.whats_need.services.ChatService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ChatService service;

    @ModelAttribute("sidebar")
    public List<SidebarData> getSideBar(){
        return service.getSideBar();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundError(NoHandlerFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("errorMessage", "Запрашиваемая страница не найдена: " + ex.getRequestURL());
        return "chat/error";
    }

    // Обработка ошибок валидации (BindingResult) при использовании @Valid в контроллере
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationErrors(MethodArgumentNotValidException ex, Model model) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        model.addAttribute("status", 400);
        model.addAttribute("errorMessage", "Ошибка при заполнении формы");
        model.addAttribute("validationErrors", errors);
        return "chat/error";
    }

    // Альтернативный обработчик валидации (иногда Spring кидает BindException)
    @ExceptionHandler(BindException.class)
    public String handleBindErrors(BindException ex, Model model) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        model.addAttribute("status", 400);
        model.addAttribute("errorMessage", "Ошибка при привязке данных");
        model.addAttribute("validationErrors", errors);
        return "chat/error";
    }

    // Обработка всех остальных непредвиденных ошибок (500)
    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("sidebar",  service.getSideBar());
        model.addAttribute("errorMessage", "Внутренняя ошибка сервера: " + ex.getMessage());
        return "chat/error";
    }
}
