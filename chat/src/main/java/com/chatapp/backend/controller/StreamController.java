package com.chatapp.backend.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

@Controller
public class StreamController {
    @MessageMapping("/hello")
    @SendTo("/stream/topic/greetings")
    public String greet(String message) throws InterruptedException {
        Thread.sleep(2000);
        return "Hello, " +
                message;
    }
}
