package org.example.javaaiassistant.service;

import okhttp3.*;
import org.example.javaaiassistant.dto.AnswerDTO;
import org.example.javaaiassistant.model.QA;
import org.example.javaaiassistant.repository.QARepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AIService {

    private final QARepository qaRepository;
    private final OkHttpClient client = new OkHttpClient();

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public AIService(QARepository qaRepository) {
        this.qaRepository = qaRepository;
    }

    public List<AnswerDTO> getAnswers(String question) {

        System.out.println("💡 Question: " + question);

        // 1️⃣ DB FIRST
        List<AnswerDTO> db = getFromDB(question);
        if (!db.isEmpty()) return db;

        // 2️⃣ OpenAI
        List<AnswerDTO> openAI = getFromOpenAI(question);
        if (!openAI.isEmpty()) return openAI;

        // 3️⃣ CSV fallback
        List<AnswerDTO> csv = getFromCSV(question);
        if (!csv.isEmpty()) return csv;

        // 4️⃣ Final fallback
        return List.of(new AnswerDTO(
                "Sorry, I could not find an answer.",
                "FALLBACK"
        ));
    }

    // ================= DB =================

    private List<AnswerDTO> getFromDB(String question) {

        List<QA> results = qaRepository.findByQuestionContainingIgnoreCase(question);

        if (!results.isEmpty()) {
            System.out.println("✅ Answer from DB");
        } else {
            System.out.println("❌ No DB match");
        }

        List<AnswerDTO> list = new ArrayList<>();
        for (QA q : results) {
            list.add(new AnswerDTO(q.getAnswer(), "DB"));
        }

        return list;
    }

    // ================= OPENAI =================

    private List<AnswerDTO> getFromOpenAI(String question) {

        if (openaiApiKey == null || openaiApiKey.isEmpty()) {
            System.out.println("⚠️ No OpenAI key");
            return Collections.emptyList();
        }

        System.out.println("🔹 Calling OpenAI...");

        try {
            JSONObject json = new JSONObject();
            json.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();
            JSONObject msg = new JSONObject();
            msg.put("role", "user");
            msg.put("content", question);
            messages.put(msg);

            json.put("messages", messages);

            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .post(body)
                    .addHeader("Authorization", "Bearer " + openaiApiKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {

                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("❌ OpenAI failed");
                    return Collections.emptyList();
                }

                String res = response.body().string();
                JSONObject obj = new JSONObject(res);

                String answer = obj.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

                System.out.println("✅ OpenAI Answer: " + answer);

                // 💾 Save to DB
                saveToDB(question, answer);

                return List.of(new AnswerDTO(answer, "OPENAI"));
            }

        } catch (Exception e) {
            System.out.println("❌ OpenAI Error: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    // ================= CSV =================

    private List<AnswerDTO> getFromCSV(String question) {

        System.out.println("📂 Trying CSV...");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("knowledge.csv").getInputStream(),
                StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (!line.contains(",")) continue;

                String[] parts = line.split(",", 2);
                String key = parts[0].toLowerCase();
                String value = parts[1];

                if (question.toLowerCase().contains(key)) {
                    System.out.println("✅ CSV Match");
                    return List.of(new AnswerDTO(value, "CSV"));
                }
            }

        } catch (Exception e) {
            System.out.println("❌ CSV Error: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    // ================= SAVE =================

    private void saveToDB(String question, String answer) {
        try {
            QA qa = new QA();
            qa.setQuestion(question);
            qa.setAnswer(answer);
            qaRepository.save(qa);

            System.out.println("💾 Saved to DB");

        } catch (Exception e) {
            System.out.println("❌ DB Save Failed");
        }
    }
}