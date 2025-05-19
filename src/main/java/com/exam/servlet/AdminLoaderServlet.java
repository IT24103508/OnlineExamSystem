package com.exam.servlet;

import com.exam.model.Student;
import com.exam.model.Exam;
import com.exam.model.Question;
import com.exam.model.Registration;
import com.exam.model.Attempt;
import com.exam.model.Result;
import com.exam.util.StudentManager;
import com.exam.util.ExamManager;
import com.exam.util.QuestionManager;
import com.exam.util.RegistrationManager;
import com.exam.util.AttemptManager;
import com.exam.util.ResultManager;
import org.apache.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/adminLoader")
public class AdminLoaderServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AdminLoaderServlet.class);

    @Override
    public void init() throws ServletException {
        // Initialize data by loading JSON files
        StudentManager.loadStudents();
        ExamManager.loadExams();
        QuestionManager.loadQuestions();
        RegistrationManager.loadRegistrations();
        AttemptManager.loadAttempts();
        ResultManager.loadResults();
        logger.info("AdminLoaderServlet initialized, data loaded from JSON files.");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // TODO: Add admin authentication check here (e.g., check for admin session or role)
            // if (request.getSession().getAttribute("admin") == null) {
            //     response.sendRedirect("adminLogin.jsp");
            //     return;
            // }

            // Load all data from JSON files
            List<Student> students = StudentManager.getAllStudents();
            List<Exam> exams = ExamManager.getAllExams();
            List<Question> questions = QuestionManager.getAllQuestions();
            List<Registration> registrations = RegistrationManager.getAllRegistrations();
            List<Attempt> attempts = AttemptManager.getAllAttempts();
            List<Result> results = ResultManager.getAllResults();

            // Log data loading
            logger.info("Loaded from JSON: " + students.size() + " students, " + exams.size() + " exams, " +
                        questions.size() + " questions, " + registrations.size() + " registrations, " +
                        attempts.size() + " attempts, " + results.size() + " results.");

            // Set attributes for admin.jsp
            request.setAttribute("students", students);
            request.setAttribute("exams", exams);
            request.setAttribute("questions", questions);
            request.setAttribute("registrations", registrations);
            request.setAttribute("attempts", attempts);
            request.setAttribute("results", results);

            // Forward to admin.jsp
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error loading admin data from JSON: " + e.getMessage(), e);
            request.setAttribute("error", "Failed to load admin dashboard: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}