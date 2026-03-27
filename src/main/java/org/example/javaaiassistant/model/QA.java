package org.example.javaaiassistant.model;

import jakarta.persistence.*;

@Entity
@Table(name = "qa")  // Table name in the database
public class QA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    private String question; // Column for the question
    private String answer;   // Column for the answer

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}