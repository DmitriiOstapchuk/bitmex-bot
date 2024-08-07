package com.dmitrii.ostapchuk.bitmexBot.controller;

import com.dmitrii.ostapchuk.bitmexBot.model.dto.UserTO;
import com.dmitrii.ostapchuk.bitmexBot.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {
    private UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("userTO", new UserTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute("userTO") UserTO userTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (!userTO.getPassword().equals(userTO.getRepeatPassword())) {
            bindingResult.rejectValue("password", "", "You entered two different passwords. Please try again.");
            return "register";
        }
        userService.create(userTO);
        return "redirect:/login";
    }
}
