package com.exam.servlet;

import com.exam.util.ExamManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editExam")
public class EditExamServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ExamManager.loadExams();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String examId = request.getParameter("examId");
        String subject = request.getParameter("subject");
        String date = request.getParameter("date");
        String durationStr = request.getParameter("duration");

        // Server-side validation
        if (subject == null || subject.trim().isEmpty()) {
            request.setAttribute("error", "Subject is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (date == null || date.trim().isEmpty()) {
            request.setAttribute("error", "Date is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        int duration;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Duration must be a positive integer");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        ExamManager.updateExam(examId, subject, date, duration);
        response.sendRedirect("adminLoader");
    }
}