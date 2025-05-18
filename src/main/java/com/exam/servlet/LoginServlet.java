package com.exam.servlet;

import com.exam.model.Student;
import org.apache.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get form parameters
            String studentId = request.getParameter("studentId");
            String password = request.getParameter("password");

            // Validate inputs
            if (studentId == null || studentId.isEmpty() || password == null || password.isEmpty()) {
                logger.warn("Invalid login attempt: studentId=" + studentId);
                request.setAttribute("error", "Student ID and password are required.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // Find student and verify password
            Student student = StudentManager.findStudentById(studentId);
            if (student == null || !StudentManager.checkPassword(studentId, password)) {
                logger.warn("Failed login attempt for studentId=" + studentId);
                request.setAttribute("error", "Invalid student ID or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // Create session and store student
            HttpSession session = request.getSession(true);
            session.setAttribute("student", student);
            logger.info("Successful login for studentId=" + studentId);

            // Redirect to student dashboard
            response.sendRedirect("studentLoader");
        } catch (Exception e) {
            logger.error("Error processing login for studentId=" + request.getParameter("studentId") +
                    ": " + e.getMessage(), e);
            request.setAttribute("error", "Login failed: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
