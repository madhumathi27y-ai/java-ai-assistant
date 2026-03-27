package org.example.javaaiassistant.dto;

public class AnswerDTO {
    private String text;
    private String source; // DB, OPENAI, CSV, FALLBACK

    public AnswerDTO() {}

    public AnswerDTO(String text, String source) {
        this.text = text;
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}