package com.exam.util;

import com.exam.model.Question;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.LinkedList;

public class QuestionManager {
    private static final String FILE_PATH = getFilePath("questions.json");
    private static LinkedList<Question> questions = new LinkedList<>();

    private static String getFilePath(String fileName) {
        return System.getProperty("user.dir") + "/src/main/webapp/" + fileName;
    }

    public static void loadQuestions() {
        questions.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                saveQuestions();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            if (jsonString.length() > 0) {
                JSONArray jsonArray = new JSONArray(jsonString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    questions.add(Question.fromJSON(jsonArray.getJSONObject(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveQuestions() {
        JSONArray jsonArray = new JSONArray();
        for (Question question : questions) {
            jsonArray.put(question.toJSON());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(jsonArray.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<Question> getQuestions() {
        return questions;
    }

    public static LinkedList<Question> getQuestionsByExamId(String examId) {
        LinkedList<Question> examQuestions = new LinkedList<>();
        for (Question question : questions) {
            if (question.getExamId().equals(examId)) {
                examQuestions.add(question);
            }
        }
        return examQuestions;
    }

    public static Question findQuestionById(String questionId) {
        for (Question question : questions) {
            if (question.getQuestionId().equals(questionId)) {
                return question;
            }
        }
        return null;
    }

    public static void addQuestion(Question question) {
        questions.add(question);
        saveQuestions();
    }

    public static void updateQuestion(String questionId, String questionText, String[] options, String correctAnswer) {
        for (Question question : questions) {
            if (question.getQuestionId().equals(questionId)) {
                question.setQuestionText(questionText);
                question.setOptions(options);
                question.setCorrectAnswer(correctAnswer);
                saveQuestions();
                break;
            }
        }
    }

    public static void deleteQuestion(String questionId) {
        questions.removeIf(question -> question.getQuestionId().equals(questionId));
        saveQuestions();
    }
}