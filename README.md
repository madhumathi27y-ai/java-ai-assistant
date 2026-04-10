# 🤖 AI-Powered Java Assistant (Spring Boot)

## 📌 Overview

This project is a backend application built using **Java and Spring Boot** that answers user queries using a smart multi-source approach.

It improves performance and reliability by checking multiple data sources before relying on external AI APIs.

---

## ❗ Problem

AI-based systems can be:

* Slow for repeated queries
* Expensive due to API usage
* Dependent on external services

---

## 💡 Solution

This application uses a **fallback strategy**:

1. Check if the answer exists in the database
2. If not, call the AI API (e.g., OpenAI)
3. If the API fails or is unavailable, fetch data from a CSV file

This ensures:

* Faster responses
* Reduced API cost
* Better reliability

---

## 🔄 System Flow

```
User Question
   ↓
Check Database
   ↓ (not found)
Call AI API
   ↓ (fallback)
Read from CSV
   ↓
Return Answer
```

---

## 🚀 Features

* Multi-source query handling (DB + API + CSV)
* Reduced API dependency
* Faster response for repeated queries
* Reliable fallback mechanism
* Clean REST API design

---

## 🛠️ Tech Stack

* Java
* Spring Boot
* REST APIs
* OpenAI API (or similar)
* CSV data processing
* Maven

---

## ▶️ How to Run

### Prerequisites

* Java 17+
* Maven
* API key for AI service

### Steps

```bash
git clone https://github.com/your-username/your-repo.git
cd your-repo
mvn spring-boot:run
```

---

## 📡 API Endpoint

**POST /ask**

Request:

```json
{
  "question": "Your question here"
}
```

Response:

```json
{
  "answer": "Response from DB / API / CSV"
}
```

---

## 🧠 Architecture Note

This project demonstrates a **fallback-based backend design pattern**, improving system efficiency and reducing dependency on external services.

---
