package org.example.javaaiassistant.dto;

import java.time.LocalDateTime;

public class ChatMessage {

    private String role; // USER or AI
    private String text;
    private LocalDateTime time;

    public ChatMessage() {}

    public ChatMessage(String role, String text) {
        this.role = role;
        this.text = text;
        this.time = LocalDateTime.now();
    }

    public String getRole() { return role; }
    public String getText() { return text; }
    public LocalDateTime getTime() { return time; }
}