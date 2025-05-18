package com.exam.util;

import com.exam.model.Registration;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationManager {
    private static final Logger logger = Logger.getLogger(RegistrationManager.class);
    private static final String FILE_PATH = JsonUtil.getFilePath("registrations.json");
    private static List<Registration> registrations = new ArrayList<>();

    public static void loadRegistrations() {
        registrations = JsonUtil.readJsonFile(FILE_PATH, new TypeReference<List<Registration>>() {});
        logger.info("Loaded " + registrations.size() + " registrations from " + FILE_PATH);
    }

    public static void saveRegistrations() {
        JsonUtil.writeJsonFile(FILE_PATH, registrations);
    }

    public static List<Registration> getAllRegistrations() {
        return new ArrayList<>(registrations);
    }

    public static List<Registration> getRegistrationsByStudent(String studentId) {
        return registrations.stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public static Registration findRegistration(String studentId, String examId) {
        return registrations.stream()
                .filter(r -> r.getStudentId().equals(studentId) && r.getExamId().equals(examId))
                .findFirst()
                .orElse(null);
    }

    public static void addRegistration(Registration registration) {
        registrations.add(registration);
        saveRegistrations();
        logger.info("Added registration: student=" + registration.getStudentId() + ", exam=" + registration.getExamId());
    }

    public static void updateRegistration(String oldStudentId, String oldExamId, Registration updatedRegistration) {
        registrations.removeIf(r -> r.getStudentId().equals(oldStudentId) && r.getExamId().equals(oldExamId));
        registrations.add(updatedRegistration);
        saveRegistrations();
        logger.info("Updated registration: old=(" + oldStudentId + "," + oldExamId + "), new=(" +
                updatedRegistration.getStudentId() + "," + updatedRegistration.getExamId() + ")");
    }

    public static void deleteRegistration(String studentId, String examId) {
        registrations.removeIf(r -> r.getStudentId().equals(studentId) && r.getExamId().equals(examId));
        saveRegistrations();
        logger.info("Deleted registration: student=" + studentId + ", exam=" + examId);
    }
}