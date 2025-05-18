<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Login</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <div class="bg-white p-6 rounded-lg shadow-lg max-w-md mx-auto">
        <h2 class="text-2xl font-bold mb-6 text-center">Student Login</h2>
        <c:if test="${not empty error}">
            <p class="text-red-500 mb-4 text-center">${error}</p>
        </c:if>
        <form action="login" method="post">
            <div class="mb-4">
                <label class="block text-gray-700">Student ID</label>
                <input type="text" name="studentId" required class="w-full border p-2 rounded">
            </div>
            <div class="mb-4">
                <label class="block text-gray-700">Password</label>
                <input type="password" name="password" required class="w-full border p-2 rounded">
            </div>
            <button type="submit" class="bg-blue-500 text-white w-full py-2 rounded hover:bg-blue-600">Login</button>
        </form>
        <p class="mt-4 text-center">
            <a href="homepage.jsp" class="text-blue-500 hover:underline">Back to Home</a>
        </p>
    </div>
</div>
</body>
</html>