package com.exam.model;

import org.json.JSONObject;

public class Result {
    private String studentId;
    private String examId;
    private int marks;
    private String grade;

    public Result() {}

    public Result(String studentId, String examId, int marks, String grade) {
        this.studentId = studentId;
        this.examId = examId;
        this.marks = marks;
        this.grade = grade;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("studentId", studentId);
        json.put("examId", examId);
        json.put("marks", marks);
        json.put("grade", grade);
        return json;
    }

    public static Result fromJSON(JSONObject json) {
        return new Result(
            json.getString("studentId"),
            json.getString("examId"),
            json.getInt("marks"),
            json.getString("grade")
        );
    }
}