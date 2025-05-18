package com.exam.servlet;

import com.exam.model.Student;
import com.exam.model.Exam;
import com.exam.model.Attempt;
import com.exam.model.Result;
import com.exam.util.ExamManager;
import com.exam.util.RegistrationManager;
import com.exam.util.AttemptManager;
import com.exam.util.ResultManager;
import org.apache.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/studentLoader")
public class StudentLoaderServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(StudentLoaderServlet.class);

    @Override
    public void init() throws ServletException {
        // Removed calls to private load methods: StudentManager.loadStudents(),
        // ExamManager.loadExams(), RegistrationManager.loadRegistrations(),
        // AttemptManager.loadAttempts(), and ResultManager.loadResults().
        // Data is already loaded internally by the manager classes when needed.
        logger.info("StudentLoaderServlet initialized.");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("student") == null) {
                logger.warn("Unauthorized access to studentLoader, redirecting to login.");
                request.setAttribute("error", "Please log in to access the student dashboard.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            Student student = (Student) session.getAttribute("student");
            String studentId = student.getStudentId();

            // Load data from JSON files
            List<Exam> allExams = ExamManager.getAllExams();
            List<Exam> registeredExams = RegistrationManager.getRegistrationsByStudent(studentId)
                    .stream()
                    .map(reg -> ExamManager.findExamById(reg.getExamId()))
                    .filter(exam -> exam != null)
                    .collect(Collectors.toList());
            List<String> registeredExamIds = registeredExams.stream()
                    .map(Exam::getExamId)
                    .collect(Collectors.toList());
            List<Attempt> attempts = AttemptManager.getAttemptsByStudent(studentId);
            List<Result> results = ResultManager.getResultsByStudent(studentId);

            // Log data loading
            logger.info("Loaded from JSON for student " + studentId + ": " + allExams.size() + " exams, " +
                    registeredExams.size() + " registered exams, " + attempts.size() + " attempts, " +
                    results.size() + " results.");

            // Set attributes for student.jsp
            request.setAttribute("student", student);
            request.setAttribute("exams", allExams);
            request.setAttribute("registeredExams", registeredExams);
            request.setAttribute("registeredExamIds", registeredExamIds);
            request.setAttribute("attempts", attempts);
            request.setAttribute("results", results);

            // Forward to student.jsp
            request.getRequestDispatcher("student.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error loading student data from JSON for studentId=" +
                    (request.getSession(false) != null ?
                            ((Student)request.getSession(false).getAttribute("student")).getStudentId() : "unknown") +
                    ": " + e.getMessage(), e);
            request.setAttribute("error", "Failed to load student dashboard: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}