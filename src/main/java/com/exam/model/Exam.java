package com.exam.model;

import org.json.JSONObject;

public class Exam {
    private String examId;
    private String subject;
    private String date;
    private int duration;

    public Exam() {}

    public Exam(String examId, String subject, String date, int duration) {
        this.examId = examId;
        this.subject = subject;
        this.date = date;
        this.duration = duration;
    }

    // Getters and Setters
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("examId", examId);
        json.put("subject", subject);
        json.put("date", date);
        json.put("duration", duration);
        return json;
    }

    public static Exam fromJSON(JSONObject json) {
        return new Exam(
            json.getString("examId"),
            json.getString("subject"),
            json.getString("date"),
            json.getInt("duration")
        );
    }
}
