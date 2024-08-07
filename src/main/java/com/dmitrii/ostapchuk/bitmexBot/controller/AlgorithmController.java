package com.dmitrii.ostapchuk.bitmexBot.controller;

import com.dmitrii.ostapchuk.bitmexBot.model.InputParameters;
import com.dmitrii.ostapchuk.bitmexBot.model.entity.User;
import com.dmitrii.ostapchuk.bitmexBot.service.FibonacciAlgorithmService;
import com.dmitrii.ostapchuk.bitmexBot.service.RestOrderService;
import com.dmitrii.ostapchuk.bitmexBot.service.UserService;
import com.dmitrii.ostapchuk.bitmexBot.service.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@Slf4j
@RequestMapping("/api")
@AllArgsConstructor
public class AlgorithmController {
    private WebSocketService webSocketService;
    private RestOrderService restOrderService;
    private FibonacciAlgorithmService fibonacciAlgorithmService;
    private UserService userService;

    @GetMapping
    public String getStartAlgorithmPage (Model model, Principal principal) {
        String name = principal.getName();
        User user = userService.findByUsername(name);
        String apiSecretKey = user.getSecretKey();
        String apiKey = user.getKey();
        restOrderService.setApiKey(apiKey);
        restOrderService.setApiSecretKey(apiSecretKey);
        model.addAttribute("inputParameters", new InputParameters());
        return "algorithm";
    }

    @GetMapping("/initialization")
    public String initializeAndSubscribe(@ModelAttribute InputParameters inputParameters) {
        restOrderService.setSymbol(inputParameters.getSymbol());
        restOrderService.setBaseUrl(inputParameters.getBaseUrl());
        fibonacciAlgorithmService.setDemo(inputParameters.getIsDemo());
        fibonacciAlgorithmService.setInitialQty(inputParameters.getInitialQty());
        fibonacciAlgorithmService.setStep(inputParameters.getStep());
        fibonacciAlgorithmService.setNumberOfLevels(inputParameters.getNumberOfLevels());
        fibonacciAlgorithmService.setNumberOfCycles(inputParameters.getNumberOfCycles());
        webSocketService.setFibonacciAlgorithmService(fibonacciAlgorithmService);
        webSocketService.connect("order");
        return "start";
    }

    @GetMapping("/start")
    public String createConnection () {
        fibonacciAlgorithmService.startNewCycle();
        return "waiting";
    }
}
