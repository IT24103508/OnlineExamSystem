package com.exam.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Question {
    private String questionId;
    private String examId;
    private String questionText;
    private String[] options;
    private String correctAnswer;

    public Question() {}

    public Question(String questionId, String examId, String questionText, String[] options, String correctAnswer) {
        this.questionId = questionId;
        this.examId = examId;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String[] getOptions() { return options; }
    public void setOptions(String[] options) { this.options = options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("questionId", questionId);
        json.put("examId", examId);
        json.put("questionText", questionText);
        json.put("options", new JSONArray(options));
        json.put("correctAnswer", correctAnswer);
        return json;
    }

    public static Question fromJSON(JSONObject json) {
        JSONArray optionsArray = json.getJSONArray("options");
        String[] options = new String[optionsArray.length()];
        for (int i = 0; i < optionsArray.length(); i++) {
            options[i] = optionsArray.getString(i);
        }
        return new Question(
            json.getString("questionId"),
            json.getString("examId"),
            json.getString("questionText"),
            options,
            json.getString("correctAnswer")
        );
    }
}