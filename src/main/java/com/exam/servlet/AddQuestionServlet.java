package com.exam.servlet;

import com.exam.model.Question;
import com.exam.util.ExamManager;
import com.exam.util.QuestionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addQuestion")
public class AddQuestionServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        QuestionManager.loadQuestions();
        ExamManager.loadExams();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String questionId = request.getParameter("questionId");
        String examId = request.getParameter("examId");
        String questionText = request.getParameter("questionText");
        String[] options = request.getParameterValues("options");
        String correctAnswer = request.getParameter("correctAnswer");

        // Server-side validation
        if (questionId == null || questionId.trim().isEmpty()) {
            request.setAttribute("error", "Question ID is required");
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
        if (questionText == null || questionText.trim().isEmpty()) {
            request.setAttribute("error", "Question text is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (options == null || options.length != 4) {
            request.setAttribute("error", "Exactly 4 options are required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                request.setAttribute("error", "All options must be filled");
                request.getRequestDispatcher("admin.jsp").forward(request, response);
                return;
            }
        }
        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            request.setAttribute("error", "Correct answer is required");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        boolean validAnswer = false;
        for (String option : options) {
            if (option.equals(correctAnswer)) {
                validAnswer = true;
                break;
            }
        }
        if (!validAnswer) {
            request.setAttribute("error", "Correct answer must match one of the options");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        if (QuestionManager.findQuestionById(questionId) != null) {
            request.setAttribute("error", "Question ID already exists");
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }

        Question question = new Question(questionId, examId, questionText, options, correctAnswer);
        QuestionManager.addQuestion(question);
        response.sendRedirect("adminLoader");
    }
}