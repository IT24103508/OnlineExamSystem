package com.exam.servlet;

import com.exam.util.AttemptManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editAttempt")
public class EditAttemptServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        AttemptManager.loadAttempts();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String attemptId = request.getParameter("attemptId");
        String answers = request.getParameter("answers");
        String marksStr = request.getParameter("marks");

        // Server-side validation
        if (answers == null || answers.trim().isEmpty()) {
            request.setAttribute("error", "Answers are required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        int marks;
        try {
            marks = Integer.parseInt(marksStr);
            if (marks < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Marks must be a non-negative integer");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        AttemptManager.updateAttempt(attemptId, answers, marks);
        response.sendRedirect("adminLoader");
    }
}