package com.exam.servlet;

import com.exam.model.Result;
import com.exam.util.ExamManager;
import com.exam.util.ResultManager;
import com.exam.util.StudentManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addResult")
public class AddResultServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ResultManager.loadResults();
        StudentManager.loadStudents();
        ExamManager.loadExams();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentId = request.getParameter("studentId");
        String examId = request.getParameter("examId");
        String marksStr = request.getParameter("marks");
        String grade = request.getParameter("grade");

        // Server-side validation
        if (studentId == null || studentId.trim().isEmpty()) {
            request.setAttribute("error", "Student ID is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (StudentManager.findStudentById(studentId) == null) {
            request.setAttribute("error", "Student does not exist");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (examId == null || examId.trim().isEmpty()) {
            request.setAttribute("error", "Exam ID is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (ExamManager.findExamById(examId) == null) {
            request.setAttribute("error", "Exam does not exist");
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
        if (grade == null || grade.trim().isEmpty()) {
            request.setAttribute("error", "Grade is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        if (ResultManager.findResult(studentId, examId) != null) {
            request.setAttribute("error", "Result already exists");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        Result result = new Result(studentId, examId, marks, grade);
        ResultManager.addResult(result);
        response.sendRedirect("adminLoader");
    }
}