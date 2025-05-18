package com.exam.servlet;

import com.exam.util.ResultManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editResult")
public class EditResultServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ResultManager.loadResults();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentId = request.getParameter("studentId");
        String examId = request.getParameter("examId");
        String marksStr = request.getParameter("marks");
        String grade = request.getParameter("grade");

        // Server-side validation
        int marks;
        try {
            marks = Integer.parseInt(marksStr);
            if (marks < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Marks must be a non-negative integer");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (grade == null || grade.trim().isEmpty()) {
            request.setAttribute("error", "Grade is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        ResultManager.updateResult(studentId, examId, marks, grade);
        response.sendRedirect("adminLoader");
    }
}