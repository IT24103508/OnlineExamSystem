package com.exam.util;

import com.exam.model.Attempt;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.LinkedList;

public class AttemptManager {
    private static final String FILE_PATH = getFilePath("attempts.json");
    private static LinkedList<Attempt> attempts = new LinkedList<>();

    private static String getFilePath(String fileName) {
        return System.getProperty("user.dir") + "/src/main/webapp/" + fileName;
    }

    public static void loadAttempts() {
        attempts.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                saveAttempts();
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
                    attempts.add(Attempt.fromJSON(jsonArray.getJSONObject(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAttempts() {
        JSONArray jsonArray = new JSONArray();
        for (Attempt attempt : attempts) {
            jsonArray.put(attempt.toJSON());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(jsonArray.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<Attempt> getAttempts() {
        return attempts;
    }

    public static LinkedList<Attempt> getAttemptsByStudentId(String studentId) {
        LinkedList<Attempt> studentAttempts = new LinkedList<>();
        for (Attempt attempt : attempts) {
            if (attempt.getStudentId().equals(studentId)) {
                studentAttempts.add(attempt);
            }
        }
        return studentAttempts;
    }

    public static Attempt findAttemptById(String attemptId) {
        for (Attempt attempt : attempts) {
            if (attempt.getAttemptId().equals(attemptId)) {
                return attempt;
            }
        }
        return null;
    }

    public static void addAttempt(Attempt attempt) {
        attempts.add(attempt);
        saveAttempts();
    }

    public static void updateAttempt(String attemptId, String answers, int marks) {
        for (Attempt attempt : attempts) {
            if (attempt.getAttemptId().equals(attemptId)) {
                attempt.setAnswers(answers);
                attempt.setMarks(marks);
                saveAttempts();
                break;
            }
        }
    }

    public static void deleteAttempt(String attemptId) {
        attempts.removeIf(attempt -> attempt.getAttemptId().equals(attemptId));
        saveAttempts();
    }
}