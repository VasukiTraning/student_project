package com.example;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentServlet extends HttpServlet {
    private List<Student> studentList = new ArrayList<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                RequestDispatcher dispatcher = request.getRequestDispatcher("addStudent.jsp");
                dispatcher.forward(request, response);
                break;
            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("student", studentList.get(id));
                RequestDispatcher dispatcherEdit = request.getRequestDispatcher("editStudent.jsp");
                dispatcherEdit.forward(request, response);
                break;
            default:
                request.setAttribute("students", studentList);
                RequestDispatcher listDispatcher = request.getRequestDispatcher("listStudents.jsp");
                listDispatcher.forward(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        Student student = new Student(name, email);
        studentList.add(student);

        response.sendRedirect("student");
    }
}
