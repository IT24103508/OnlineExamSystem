package com.exam.servlet;

import com.exam.util.ExamManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteExam")
public class DeleteExamServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ExamManager.loadExams();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String examId = request.getParameter("examId");
        ExamManager.deleteExam(examId);
        response.sendRedirect("adminLoader");
    }
}