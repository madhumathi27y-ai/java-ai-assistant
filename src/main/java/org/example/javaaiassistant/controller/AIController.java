package org.example.javaaiassistant.controller;
import jakarta.servlet.http.HttpSession;
import org.example.javaaiassistant.dto.AnswerDTO;
import org.example.javaaiassistant.dto.ChatMessage;
import org.example.javaaiassistant.service.AIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AIController {
    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestParam String question, Model model, HttpSession session) {

        List<AnswerDTO> answers = aiService.getAnswers(question);

        List<ChatMessage> chatHistory =
                (List<ChatMessage>) session.getAttribute("chatHistory");

        if (chatHistory == null) {
            chatHistory = new ArrayList<>();
        }

        // Add USER message
        chatHistory.add(new ChatMessage("USER", question));

        // Add AI messages
        for (AnswerDTO ans : answers) {
            chatHistory.add(new ChatMessage("AI", ans.getText()));
        }

        session.setAttribute("chatHistory", chatHistory);
        model.addAttribute("chatHistory", chatHistory);

        return "index";
    }
}

