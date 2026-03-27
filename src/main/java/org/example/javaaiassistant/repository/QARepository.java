package org.example.javaaiassistant.repository;

import org.example.javaaiassistant.model.QA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QARepository extends JpaRepository<QA, Long> {
    // Search by question containing a keyword (case-insensitive)
    List<QA> findByQuestionContainingIgnoreCase(String question);
}