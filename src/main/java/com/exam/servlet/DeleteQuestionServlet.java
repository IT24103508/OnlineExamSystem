package com.exam.servlet;

import com.exam.util.QuestionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteQuestion")
public class DeleteQuestionServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        QuestionManager.loadQuestions();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String questionId = request.getParameter("questionId");
        QuestionManager.deleteQuestion(questionId);
        response.sendRedirect("adminLoader");
    }
}