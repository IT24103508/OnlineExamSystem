package com.exam.servlet;

import com.exam.model.Student;
import com.exam.util.ExamManager;
import com.exam.util.QuestionManager;
import com.exam.util.RegistrationManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/startAttempt")
public class StartAttemptServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ExamManager.loadExams();
        RegistrationManager.loadRegistrations();
        QuestionManager.loadQuestions();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Student student = (Student) session.getAttribute("student");

        if (student == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String examId = request.getParameter("examId");

        if (examId == null || examId.trim().isEmpty()) {
            request.setAttribute("error", "Exam ID is required");
            request.getRequestDispatcher("studentLoader").forward(request, response);
            return;
        }

        if (ExamManager.findExamById(examId) == null) {
            request.setAttribute("error", "Exam not found");
            request.getRequestDispatcher("studentLoader").forward(request, response);
            return;
        }

        if (RegistrationManager.findRegistration(student.getStudentId(), examId) == null) {
            request.setAttribute("error", "Not registered for this exam");
            request.getRequestDispatcher("studentLoader").forward(request, response);
            return;
        }

        if (QuestionManager.getQuestionsByExamId(examId).isEmpty()) {
            request.setAttribute("error", "No questions available for this exam");
            request.getRequestDispatcher("studentLoader").forward(request, response);
            return;
        }

        String attemptId = UUID.randomUUID().toString();
        session.setAttribute("attemptId", attemptId);
        session.setAttribute("examId", examId);
        response.sendRedirect("attempt.jsp");
    }
}