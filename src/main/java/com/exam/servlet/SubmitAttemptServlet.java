package com.exam.servlet;

import com.exam.model.Attempt;
import com.exam.model.Question;
import com.exam.util.AttemptManager;
import com.exam.util.QuestionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.json.JSONObject;

@WebServlet("/submitAttempt")
public class SubmitAttemptServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        QuestionManager.loadQuestions();
        AttemptManager.loadAttempts();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String attemptId = request.getParameter("attemptId");
        String examId = request.getParameter("examId");
        String studentId = ((com.exam.model.Student) session.getAttribute("student")).getStudentId();

        // Collect answers
        Map<String, String> answers = new HashMap<>();
        LinkedList<Question> questions = QuestionManager.getQuestionsByExamId(examId);
        int marks = 0;

        for (Question question : questions) {
            String answer = request.getParameter("answer_" + question.getQuestionId());
            if (answer != null && !answer.trim().isEmpty()) {
                answers.put(question.getQuestionId(), answer);
                if (answer.equals(question.getCorrectAnswer())) {
                    marks += 1; // 1 mark per correct answer
                }
            }
        }

        // Convert answers to JSON
        JSONObject answersJson = new JSONObject(answers);
        Attempt attempt = new Attempt(attemptId, studentId, examId, answersJson.toString(), marks);
        AttemptManager.addAttempt(attempt);

        // Clear session attributes
        session.removeAttribute("attemptId");
        session.removeAttribute("examId");

        response.sendRedirect("studentLoader");
    }
}