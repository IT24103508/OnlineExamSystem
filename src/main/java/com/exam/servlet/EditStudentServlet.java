package com.exam.servlet;

import com.exam.model.Student;
import org.apache.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editStudent")
public class EditStudentServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(EditStudentServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve form parameters
            String studentId = request.getParameter("studentId");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String course = request.getParameter("course");

            // Validate inputs
            if (studentId == null || name == null || email == null || course == null ||
                    studentId.isEmpty() || name.isEmpty() || email.isEmpty() || course.isEmpty()) {
                logger.warn("Invalid student update parameters: studentId=" + studentId +
                        ", name=" + name + ", email=" + email + ", course=" + course);
                request.setAttribute("error", "All fields are required.");
                request.getRequestDispatcher("admin.jsp").forward(request, response);
                return;
            }

            // Check if student exists
            Student existingStudent = StudentManager.findStudentById(studentId);
            if (existingStudent == null) {
                logger.warn("Student not found: " + studentId);
                request.setAttribute("error", "Student not found.");
                request.getRequestDispatcher("admin.jsp").forward(request, response);
                return;
            }

            // Create updated Student object using constructor
            Student updatedStudent = new Student(studentId, name, email, course, existingStudent.getPasswordHash());

            // Update student in JSON file
            StudentManager.updateStudent(updatedStudent);
            logger.info("Updated student: " + studentId);

            // Set success message and redirect to admin dashboard
            request.setAttribute("message", "Student updated successfully.");
            request.getRequestDispatcher("adminLoader").forward(request, response);
        } catch (Exception e) {
            logger.error("Error updating student: " + e.getMessage(), e);
            request.setAttribute("error", "Failed to update student: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}