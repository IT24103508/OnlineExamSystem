package com.exam.util;

import com.exam.model.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private static final Logger logger = Logger.getLogger(StudentManager.class);
    private static final String FILE_PATH = JsonUtil.getFilePath("students.json");
    private static List<Student> students = new ArrayList<>();

    public static void loadStudents() {
        students = JsonUtil.readJsonFile(FILE_PATH, new TypeReference<List<Student>>() {});
        logger.info("Loaded " + students.size() + " students from " + FILE_PATH);
    }

    public static void saveStudents() {
        JsonUtil.writeJsonFile(FILE_PATH, students);
    }

    public static List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public static Student findStudentById(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    public static void addStudent(Student student) {
        students.add(student);
        saveStudents();
        logger.info("Added student: " + student.getStudentId());
    }

    public static void updateStudent(Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(updatedStudent.getStudentId())) {
                students.set(i, updatedStudent);
                saveStudents();
                logger.info("Updated student: " + updatedStudent.getStudentId());
                return;
            }
        }
    }

    public static void deleteStudent(String studentId) {
        students.removeIf(s -> s.getStudentId().equals(studentId));
        saveStudents();
        logger.info("Deleted student: " + studentId);
    }

    public static boolean checkPassword(String studentId, String password) {
        Student student = findStudentById(studentId);
        if (student != null) {
            return student.checkPassword(password);
        }
        return false;
    }
}
