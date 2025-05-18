<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <div class="bg-white p-6 rounded-lg shadow-lg">
        <h1 class="text-3xl font-bold mb-6 text-center">Student Dashboard</h1>

        <!-- Success/Error Messages -->
        <c:if test="${not empty message}">
            <p class="text-green-500 mb-4 text-center">${message}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="text-red-500 mb-4 text-center">${error}</p>
        </c:if>

        <!-- Profile Section -->
        <h2 class="text-2xl font-semibold mb-4">Your Profile</h2>
        <form action="updateProfile" method="post" class="mb-6">
            <div class="grid grid-cols-1 gap-4">
                <div>
                    <label class="block text-gray-700">Student ID</label>
                    <input type="text" name="studentId" value="${student.studentId}" disabled class="w-full border p-2 rounded">
                </div>
                <div>
                    <label class="block text-gray-700">Name</label>
                    <input type="text" name="name" value="${student.name}" required class="w-full border p-2 rounded">
                </div>
                <div>
                    <label class="block text-gray-700">Email</label>
                    <input type="email" name="email" value="${student.email}" required class="w-full border p-2 rounded">
                </div>
                <div>
                    <label class="block text-gray-700">Course</label>
                    <input type="text" name="course" value="${student.course}" required class="w-full border p-2 rounded">
                </div>
                <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Update Profile</button>
            </div>
        </form>

        <!-- Change Password Section -->
        <h2 class="text-2xl font-semibold mb-4">Change Password</h2>
        <form action="changePassword" method="post" class="mb-6">
            <div class="grid grid-cols-1 gap-4">
                <div>
                    <label class="block text-gray-700">Current Password</label>
                    <input type="password" name="currentPassword" required class="w-full border p-2 rounded">
                </div>
                <div>
                    <label class="block text-gray-700">New Password</label>
                    <input type="password" name="newPassword" required class="w-full border p-2 rounded">
                </div>
                <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Change Password</button>
            </div>
        </form>

        <!-- Available Exams Section -->
        <h2 class="text-2xl font-semibold mb-4">Available Exams</h2>
        <table class="w-full table-auto border-collapse mb-6">
            <thead>
            <tr class="bg-gray-200">
                <th class="border p-2">Exam ID</th>
                <th class="border p-2">Subject</th>
                <th class="border p-2">Date</th>
                <th class="border p-2">Duration (min)</th>
                <th class="border p-2">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="exam" items="${exams}">
                <tr>
                    <td class="border p-2">${exam.examId}</td>
                    <td class="border p-2">${exam.subject}</td>
                    <td class="border p-2">${exam.date}</td>
                    <td class="border p-2">${exam.duration}</td>
                    <td class="border p-2 text-center">
                        <c:if test="${not registeredExamIds.contains(exam.examId)}">
                            <form action="registerExam" method="post" class="inline">
                                <input type="hidden" name="examId" value="${exam.examId}">
                                <button type="submit" class="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600">Register</button>
                            </form>
                        </c:if>
                        <c:if test="${registeredExamIds.contains(exam.examId)}">
                            <form action="startAttempt" method="post" class="inline">
                                <input type="hidden" name="examId" value="${exam.examId}">
                                <button type="submit" class="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600">Start Attempt</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- Past Attempts Section -->
        <h2 class="text-2xl font-semibold mb-4">Past Attempts</h2>
        <table class="w-full table-auto border-collapse mb-6">
            <thead>
            <tr class="bg-gray-200">
                <th class="border p-2">Attempt ID</th>
                <th class="border p-2">Exam ID</th>
                <th class="border p-2">Marks</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="attempt" items="${attempts}">
                <tr>
                    <td class="border p-2">${attempt.attemptId}</td>
                    <td class="border p-2">${attempt.examId}</td>
                    <td class="border p-2">${attempt.marks}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- Results Section -->
        <h2 class="text-2xl font-semibold mb-4">Results</h2>
        <table class="w-full table-auto border-collapse mb-6">
            <thead>
            <tr class="bg-gray-200">
                <th class="border p-2">Exam ID</th>
                <th class="border p-2">Marks</th>
                <th class="border p-2">Grade</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="result" items="${results}">
                <tr>
                    <td class="border p-2">${result.examId}</td>
                    <td class="border p-2">${result.marks}</td>
                    <td class="border p-2">${result.grade}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- Navigation -->
        <div class="text-center">
            <a href="logout" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">Logout</a>
            <a href="homepage.jsp" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 ml-2">Back to Home</a>
        </div>
    </div>
</div>
</body>
</html>