package com.exam.model;

import org.json.JSONObject;

public class Attempt {
    private String attemptId;
    private String studentId;
    private String examId;
    private String answers; // JSON string of questionId:answer pairs
    private int marks;

    public Attempt() {}

    public Attempt(String attemptId, String studentId, String examId, String answers, int marks) {
        this.attemptId = attemptId;
        this.studentId = studentId;
        this.examId = examId;
        this.answers = answers;
        this.marks = marks;
    }

    // Getters and Setters
    public String getAttemptId() { return attemptId; }
    public void setAttemptId(String attemptId) { this.attemptId = attemptId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public String getAnswers() { return answers; }
    public void setAnswers(String answers) { this.answers = answers; }
    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("attemptId", attemptId);
        json.put("studentId", studentId);
        json.put("examId", examId);
        json.put("answers", answers);
        json.put("marks", marks);
        return json;
    }

    public static Attempt fromJSON(JSONObject json) {
        return new Attempt(
            json.getString("attemptId"),
            json.getString("studentId"),
            json.getString("examId"),
            json.getString("answers"),
            json.getInt("marks")
        );
    }
}