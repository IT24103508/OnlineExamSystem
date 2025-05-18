package com.exam.servlet;

import com.exam.util.AttemptManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteAttempt")
public class DeleteAttemptServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        AttemptManager.loadAttempts();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String attemptId = request.getParameter("attemptId");
        AttemptManager.deleteAttempt(attemptId);
        response.sendRedirect("adminLoader");
    }
}