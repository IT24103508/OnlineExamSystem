package com.exam.servlet;

import com.exam.model.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/addStudent")
public class AddStudentServlet extends HttpServlet {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public void init() throws ServletException {
        StudentManager.loadStudents();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentId = request.getParameter("studentId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");
        String password = request.getParameter("password");

        // Server-side validation
        if (studentId == null || studentId.trim().isEmpty()) {
            request.setAttribute("error", "Student ID is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Name is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("error", "Invalid email format");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (course == null || course.trim().isEmpty()) {
            request.setAttribute("error", "Course is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        if (StudentManager.findStudentById(studentId) != null) {
            request.setAttribute("error", "Student ID already exists");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        Student student = new Student(studentId, name, email, course, password);
        StudentManager.addStudent(student);
        response.sendRedirect("adminLoader");
    }
}