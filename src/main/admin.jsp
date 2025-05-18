<script type="text/javascript">
        var gk_isXlsx = false;
        var gk_xlsxFileLookup = {};
        var gk_fileData = {};
        function filledCell(cell) {
          return cell !== '' && cell != null;
        }
        function loadFileData(filename) {
        if (gk_isXlsx && gk_xlsxFileLookup[filename]) {
            try {
                var workbook = XLSX.read(gk_fileData[filename], { type: 'base64' });
                var firstSheetName = workbook.SheetNames[0];
                var worksheet = workbook.Sheets[firstSheetName];

                // Convert sheet to JSON to filter blank rows
                var jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1, blankrows: false, defval: '' });
                // Filter out blank rows (rows where all cells are empty, null, or undefined)
                var filteredData = jsonData.filter(row => row.some(filledCell));

                // Heuristic to find the header row by ignoring rows with fewer filled cells than the next row
                var headerRowIndex = filteredData.findIndex((row, index) =>
                  row.filter(filledCell).length >= filteredData[index + 1]?.filter(filledCell).length
                );
                // Fallback
                if (headerRowIndex === -1 || headerRowIndex > 25) {
                  headerRowIndex = 0;
                }

                // Convert filtered JSON back to CSV
                var csv = XLSX.utils.aoa_to_sheet(filteredData.slice(headerRowIndex)); // Create a new sheet from filtered array of arrays
                csv = XLSX.utils.sheet_to_csv(csv, { header: 1 });
                return csv;
            } catch (e) {
                console.error(e);
                return "";
            }
        }
        return gk_fileData[filename] || "";
        }
        </script><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
    <header class="bg-blue-600 text-white shadow-md">
        <div class="container mx-auto p-4 flex justify-between items-center">
            <h1 class="text-2xl font-bold">Admin Dashboard</h1>
            <form action="logout" method="post">
                <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">Logout</button>
            </form>
        </div>
    </header>
    <main class="container mx-auto p-6">
        <c:if test="${not empty error}">
            <p class="text-red-500 mb-4 text-center">${error}</p>
        </c:if>

        <!-- Students Management -->
        <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h2 class="text-xl font-semibold mb-4">Manage Students</h2>
            <form id="addStudentForm" action="addStudent" method="post" class="mb-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Student ID</label>
                        <input type="text" name="studentId" id="addStudentId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Name</label>
                        <input type="text" name="name" id="addStudentName" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Email</label>
                        <input type="email" name="email" id="addStudentEmail" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Course</label>
                        <input type="text" name="course" id="addStudentCourse" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Password</label>
                        <input type="password" name="password" id="addStudentPassword" class="border p-2 w-full rounded" required>
                    </div>
                </div>
                <div class="mt-4">
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Add Student</button>
                </div>
            </form>
            <table class="w-full border-collapse border">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="border p-2">Student ID</th>
                        <th class="border p-2">Name</th>
                        <th class="border p-2">Email</th>
                        <th class="border p-2">Course</th>
                        <th class="border p-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td class="border p-2">${student.studentId}</td>
                            <td class="border p-2">${student.name}</td>
                            <td class="border p-2">${student.email}</td>
                            <td class="border p-2">${student.course}</td>
                            <td class="border p-2">
                                <form action="editStudent" method="post" class="inline">
                                    <input type="hidden" name="studentId" value="${student.studentId}">
                                    <input type="text" name="name" value="${student.name}" class="border p-1 rounded" required>
                                    <input type="email" name="email" value="${student.email}" class="border p-1 rounded" required>
                                    <input type="text" name="course" value="${student.course}" class="border p-1 rounded" required>
                                    <button type="submit" class="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-600">Update</button>
                                </form>
                                <form action="deleteStudent" method="post" class="inline">
                                    <input type="hidden" name="studentId" value="${student.studentId}">
                                    <button type="submit" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600" onclick="return confirm('Are you sure?')">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <c:if test="${not empty message}">
            <p class="text-green-500 mb-4 text-center">${message}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="text-red-500 mb-4 text-center">${error}</p>
        </c:if>
        <table class="w-full table-auto border-collapse mb-6">
            <thead>
            <tr class="bg-gray-200">
                <th class="border p-2">Student ID</th>
                <th class="border p-2">Name</th>
                <th class="border p-2">Email</th>
                <th class="border p-2">Course</th>
                <th class="border p-2">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="student" items="${students}">
                <tr>
                    <td class="border p-2">${student.studentId}</td>
                    <td class="border p-2">${student.name}</td>
                    <td class="border p-2">${student.email}</td>
                    <td class="border p-2">${student.course}</td>
                    <td class="border p-2">
                        <form action="editStudent" method="post" class="inline">
                            <input type="hidden" name="studentId" value="${student.studentId}">
                            <input type="text" name="name" value="${student.name}" required class="border p-1 mb-1 w-24">
                            <input type="email" name="email" value="${student.email}" required class="border p-1 mb-1 w-24">
                            <input type="text" name="course" value="${student.course}" required class="border p-1 mb-1 w-24">
                            <button type="submit" class="bg-blue-500 text-white px-2 py-1 rounded">Update</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- Exams Management -->
        <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h2 class="text-xl font-semibold mb-4">Manage Exams</h2>
            <form id="addExamForm" action="addExam" method="post" class="mb-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Exam ID</label>
                        <input type="text" name="examId" id="addExamId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Subject</label>
                        <input type="text" name="subject" id="addExamSubject" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Date</label>
                        <input type="date" name="date" id="addExamDate" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Duration (minutes)</label>
                        <input type="number" name="duration" id="addExamDuration" class="border p-2 w-full rounded" required>
                    </div>
                </div>
                <div class="mt-4">
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Add Exam</button>
                </div>
            </form>
            <table class="w-full border-collapse border">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="border p-2">Exam ID</th>
                        <th class="border p-2">Subject</th>
                        <th class="border p-2">Date</th>
                        <th class="border p-2">Duration</th>
                        <th class="border p-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="exam" items="${exams}">
                        <tr>
                            <td class="border p-2">${exam.examId}</td>
                            <td class="border p-2">${exam.subject}</td>
                            <td class="border p-2">${exam.date}</td>
                            <td class="border p-2">${exam.duration}</td>
                            <td class="border p-2">
                                <form action="editExam" method="post" class="inline">
                                    <input type="hidden" name="examId" value="${exam.examId}">
                                    <input type="text" name="subject" value="${exam.subject}" class="border p-1 rounded" required>
                                    <input type="date" name="date" value="${exam.date}" class="border p-1 rounded" required>
                                    <input type="number" name="duration" value="${exam.duration}" class="border p-1 rounded" required>
                                    <button type="submit" class="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-600">Update</button>
                                </form>
                                <form action="deleteExam" method="post" class="inline">
                                    <input type="hidden" name="examId" value="${exam.examId}">
                                    <button type="submit" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600" onclick="return confirm('Are you sure?')">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Questions Management -->
        <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h2 class="text-xl font-semibold mb-4">Manage Questions</h2>
            <form id="addQuestionForm" action="addQuestion" method="post" class="mb-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Question ID</label>
                        <input type="text" name="questionId" id="addQuestionId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Exam ID</label>
                        <input type="text" name="examId" id="addQuestionExamId" class="border p-2 w-full rounded" required>
                    </div>
                    <div class="col-span-2">
                        <label class="block text-sm font-medium text-gray-700">Question Text</label>
                        <input type="text" name="questionText" id="addQuestionText" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Option 1</label>
                        <input type="text" name="options" id="addOption1" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Option 2</label>
                        <input type="text" name="options" id="addOption2" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Option 3</label>
                        <input type="text" name="options" id="addOption3" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Option 4</label>
                        <input type="text" name="options" id="addOption4" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Correct Answer</label>
                        <input type="text" name="correctAnswer" id="addCorrectAnswer" class="border p-2 w-full rounded" required>
                    </div>
                </div>
                <div class="mt-4">
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Add Question</button>
                </div>
            </form>
            <table class="w-full border-collapse border">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="border p-2">Question ID</th>
                        <th class="border p-2">Exam ID</th>
                        <th class="border p-2">Question Text</th>
                        <th class="border p-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="question" items="${questions}">
                        <tr>
                            <td class="border p-2">${question.questionId}</td>
                            <td class="border p-2">${question.examId}</td>
                            <td class="border p-2">${question.questionText}</td>
                            <td class="border p-2">
                                <form action="editQuestion" method="post" class="inline">
                                    <input type="hidden" name="questionId" value="${question.questionId}">
                                    <input type="text" name="questionText" value="${question.questionText}" class="border p-1 rounded" required>
                                    <c:forEach var="option" items="${question.options}" varStatus="optStatus">
                                        <input type="text" name="options" value="${option}" class="border p-1 rounded" required>
                                    </c:forEach>
                                    <input type="text" name="correctAnswer" value="${question.correctAnswer}" class="border p-1 rounded" required>
                                    <button type="submit" class="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-600">Update</button>
                                </form>
                                <form action="deleteQuestion" method="post" class="inline">
                                    <input type="hidden" name="questionId" value="${question.questionId}">
                                    <button type="submit" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600" onclick="return confirm('Are you sure?')">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Registrations Management -->
        <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h2 class="text-xl font-semibold mb-4">Manage Registrations</h2>
            <form id="editRegistrationForm" action="editRegistration" method="post" class="mb-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Old Student ID</label>
                        <input type="text" name="oldStudentId" id="editOldStudentId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Old Exam ID</label>
                        <input type="text" name="oldExamId" id="editOldExamId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">New Student ID</label>
                        <input type="text" name="newStudentId" id="editNewStudentId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">New Exam ID</label>
                        <input type="text" name="newExamId" id="editNewExamId" class="border p-2 w-full rounded" required>
                    </div>
                </div>
                <div class="mt-4">
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Update Registration</button>
                </div>
            </form>
            <table class="w-full border-collapse border">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="border p-2">Student ID</th>
                        <th class="border p-2">Exam ID</th>
                        <th class="border p-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="reg" items="${registrations}">
                        <tr>
                            <td class="border p-2">${reg.studentId}</td>
                            <td class="border p-2">${reg.examId}</td>
                            <td class="border p-2">
                                <form action="deleteRegistration" method="post" class="inline">
                                    <input type="hidden" name="studentId" value="${reg.studentId}">
                                    <input type="hidden" name="examId" value="${reg.examId}">
                                    <button type="submit" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600" onclick="return confirm('Are you sure?')">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Attempts Management -->
        <div class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h2 class="text-xl font-semibold mb-4">Manage Attempts</h2>
            <form id="editAttemptForm" action="editAttempt" method="post" class="mb-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Attempt ID</label>
                        <input type="text" name="attemptId" id="editAttemptId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Answers (JSON)</label>
                        <input type="text" name="answers" id="editAnswers" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Marks</label>
                        <input type="number" name="marks" id="editMarks" class="border p-2 w-full rounded" required>
                    </div>
                </div>
                <div class="mt-4">
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Update Attempt</button>
                </div>
            </form>
            <table class="w-full border-collapse border">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="border p-2">Attempt ID</th>
                        <th class="border p-2">Student ID</th>
                        <th class="border p-2">Exam ID</th>
                        <th class="border p-2">Marks</th>
                        <th class="border p-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="attempt" items="${attempts}">
                        <tr>
                            <td class="border p-2">${attempt.attemptId}</td>
                            <td class="border p-2">${attempt.studentId}</td>
                            <td class="border p-2">${attempt.examId}</td>
                            <td class="border p-2">${attempt.marks}</td>
                            <td class="border p-2">
                                <form action="deleteAttempt" method="post" class="inline">
                                    <input type="hidden" name="attemptId" value="${attempt.attemptId}">
                                    <button type="submit" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600" onclick="return confirm('Are you sure?')">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Results Management -->
        <div class="bg-white p-6 rounded-lg shadow-lg">
            <h2 class="text-xl font-semibold mb-4">Manage Results</h2>
            <form id="addResultForm" action="addResult" method="post" class="mb-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Student ID</label>
                        <input type="text" name="studentId" id="addResultStudentId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Exam ID</label>
                        <input type="text" name="examId" id="addResultExamId" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Marks</label>
                        <input type="number" name="marks" id="addResultMarks" class="border p-2 w-full rounded" required>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Grade</label>
                        <input type="text" name="grade" id="addResultGrade" class="border p-2 w-full rounded" required>
                    </div>
                </div>
                <div class="mt-4">
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Add Result</button>
                </div>
            </form>
            <c:if test="${not empty message}">
                <p class="text-green-500 mb-4 text-center">${message}</p>
            </c:if>
            <c:if test="${not empty error}">
                <p class="text-red-500 mb-4 text-center">${error}</p>
            </c:if>
            <h2 class="text-2xl font-semibold mb-4">Results</h2>
            <table class="w-full table-auto border-collapse mb-6">
                <thead>
                <tr class="bg-gray-200">
                    <th class="border p-2">Student ID</th>
                    <th class="border p-2">Exam ID</th>
                    <th class="border p-2">Marks</th>
                    <th class="border p-2">Grade</th>
                    <th class="border p-2">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="result" items="${results}">
                    <tr>
                        <td class="border p-2">${result.studentId}</td>
                        <td class="border p-2">${result.examId}</td>
                        <td class="border p-2">${result.marks}</td>
                        <td class="border p-2">${result.grade}</td>
                        <td class="border p-2">
                            <form action="editResult" method="post" class="inline">
                                <input type="hidden" name="studentId" value="${result.studentId}">
                                <input type="hidden" name="examId" value="${result.examId}">
                                <input type="number" name="marks" value="${result.marks}" required class="border p-1 mb-1 w-16" min="0" max="100">
                                <input type="text" name="grade" value="${result.grade}" required class="border p-1 mb-1 w-16">
                                <button type="submit" class="bg-blue-500 text-white px-2 py-1 rounded">Update</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
    <script>
        // Client-side validation for Add Student Form
        document.getElementById('addStudentForm').addEventListener('submit', function(event) {
            const studentId = document.getElementById('addStudentId').value.trim();
            const name = document.getElementById('addStudentName').value.trim();
            const email = document.getElementById('addStudentEmail').value.trim();
            const course = document.getElementById('addStudentCourse').value.trim();
            const password = document.getElementById('addStudentPassword').value.trim();
            const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;

            if (!studentId) {
                event.preventDefault();
                alert('Student ID is required');
            } else if (!name) {
                event.preventDefault();
                alert('Name is required');
            } else if (!emailRegex.test(email)) {
                event.preventDefault();
                alert('Invalid email format');
            } else if (!course) {
                event.preventDefault();
                alert('Course is required');
            } else if (password.length < 6) {
                event.preventDefault();
                alert('Password must be at least 6 characters');
            }
        });

        // Client-side validation for Add Exam Form
        document.getElementById('addExamForm').addEventListener('submit', function(event) {
            const examId = document.getElementById('addExamId').value.trim();
            const subject = document.getElementById('addExamSubject').value.trim();
            const date = document.getElementById('addExamDate').value;
            const duration = document.getElementById('addExamDuration').value;

            if (!examId) {
                event.preventDefault();
                alert('Exam ID is required');
            } else if (!subject) {
                event.preventDefault();
                alert('Subject is required');
            } else if (!date) {
                event.preventDefault();
                alert('Date is required');
            } else if (!duration || duration <= 0) {
                event.preventDefault();
                alert('Duration must be a positive number');
            }
        });

        // Client-side validation for Add Question Form
        document.getElementById('addQuestionForm').addEventListener('submit', function(event) {
            const questionId = document.getElementById('addQuestionId').value.trim();
            const examId = document.getElementById('addQuestionExamId').value.trim();
            const questionText = document.getElementById('addQuestionText').value.trim();
            const option1 = document.getElementById('addOption1').value.trim();
            const option2 = document.getElementById('addOption2').value.trim();
            const option3 = document.getElementById('addOption3').value.trim();
            const option4 = document.getElementById('addOption4').value.trim();
            const correctAnswer = document.getElementById('addCorrectAnswer').value.trim();

            if (!questionId) {
                event.preventDefault();
                alert('Question ID is required');
            } else if (!examId) {
                event.preventDefault();
                alert('Exam ID is required');
            } else if (!questionText) {
                event.preventDefault();
                alert('Question text is required');
            } else if (!option1 || !option2 || !option3 || !option4) {
                event.preventDefault();
                alert('All options are required');
            } else if (!correctAnswer) {
                event.preventDefault();
                alert('Correct answer is required');
            } else if (![option1, option2, option3, option4].includes(correctAnswer)) {
                event.preventDefault();
                alert('Correct answer must match one of the options');
            }
        });

        // Client-side validation for Edit Registration Form
        document.getElementById('editRegistrationForm').addEventListener('submit', function(event) {
            const oldStudentId = document.getElementById('editOldStudentId').value.trim();
            const oldExamId = document.getElementById('editOldExamId').value.trim();
            const newStudentId = document.getElementById('editNewStudentId').value.trim();
            const newExamId = document.getElementById('editNewExamId').value.trim();

            if (!oldStudentId) {
                event.preventDefault();
                alert('Old Student ID is required');
            } else if (!oldExamId) {
                event.preventDefault();
                alert('Old Exam ID is required');
            } else if (!newStudentId) {
                event.preventDefault();
                alert('New Student ID is required');
            } else if (!newExamId) {
                event.preventDefault();
                alert('New Exam ID is required');
            }
        });

        // Client-side validation for Edit Attempt Form
        document.getElementById('editAttemptForm').addEventListener('submit', function(event) {
            const attemptId = document.getElementById('editAttemptId').value.trim();
            const answers = document.getElementById('editAnswers').value.trim();
            const marks = document.getElementById('editMarks').value;

            if (!attemptId) {
                event.preventDefault();
                alert('Attempt ID is required');
            } else if (!answers) {
                event.preventDefault();
                alert('Answers are required');
            } else if (!marks || marks < 0) {
                event.preventDefault();
                alert('Marks must be a non-negative number');
            }
        });

        // Client-side validation for Add Result Form
        document.getElementById('addResultForm').addEventListener('submit', function(event) {
            const studentId = document.getElementById('addResultStudentId').value.trim();
            const examId = document.getElementById('addResultExamId').value.trim();
            const marks = document.getElementById('addResultMarks').value;
            const grade = document.getElementById('addResultGrade').value.trim();

            if (!studentId) {
                event.preventDefault();
                alert('Student ID is required');
            } else if (!examId) {
                event.preventDefault();
                alert('Exam ID is required');
            } else if (!marks || marks < 0) {
                event.preventDefault();
                alert('Marks must be a non-negative number');
            } else if (!grade) {
                event.preventDefault();
                alert('Grade is required');
            }
        });
    </script>
</body>
</html>