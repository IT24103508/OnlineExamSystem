package com.exam.util;

import com.exam.model.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.LinkedList;

public class ResultManager {
    private static final String FILE_PATH = getFilePath("results.json");
    private static LinkedList<Result> results = new LinkedList<>();

    private static String getFilePath(String fileName) {
        return System.getProperty("user.dir") + "/src/main/webapp/" + fileName;
    }

    public static void loadResults() {
        results.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                saveResults();
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
                    results.add(Result.fromJSON(jsonArray.getJSONObject(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveResults() {
        JSONArray jsonArray = new JSONArray();
        for (Result result : results) {
            jsonArray.put(result.toJSON());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(jsonArray.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<Result> getResults() {
        return results;
    }

    public static LinkedList<Result> getResultsByStudentId(String studentId) {
        LinkedList<Result> studentResults = new LinkedList<>();
        for (Result result : results) {
            if (result.getStudentId().equals(studentId)) {
                studentResults.add(result);
            }
        }
        return studentResults;
    }

    public static Result findResult(String studentId, String examId) {
        for (Result result : results) {
            if (result.getStudentId().equals(studentId) && result.getExamId().equals(examId)) {
                return result;
            }
        }
        return null;
    }

    public static void addResult(Result result) {
        results.add(result);
        saveResults();
    }

    public static void updateResult(String studentId, String examId, int marks, String grade) {
        for (Result result : results) {
            if (result.getStudentId().equals(studentId) && result.getExamId().equals(examId)) {
                result.setMarks(marks);
                result.setGrade(grade);
                saveResults();
                break;
            }
        }
    }

    public static void deleteResult(String studentId, String examId) {
        results.removeIf(result -> result.getStudentId().equals(studentId) && result.getExamId().equals(examId));
        saveResults();
    }
}