package com.exam.servlet;

import com.exam.util.ResultManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteResult")
public class DeleteResultServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ResultManager.loadResults();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentId = request.getParameter("studentId");
        String examId = request.getParameter("examId");
        ResultManager.deleteResult(studentId, examId);
        response.sendRedirect("adminLoader");
    }
}