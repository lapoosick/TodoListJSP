package ru.academits.orlov.todolistjsp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.academits.orlov.todolistjsp.entities.TodoItem;
import ru.academits.orlov.todolistjsp.repositories.TodoItemsInMemoryRepository;
import ru.academits.orlov.todolistjsp.repositories.TodoItemsRepository;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

@WebServlet("")
public class TodoListServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();

        String emptyCreateItemError = session.getAttribute("emptyCreateItem") != null
                ? (String) session.getAttribute("emptyCreateItem")
                : "";

        session.removeAttribute("emptyCreateItem");

        req.setAttribute("emptyCreateItem", emptyCreateItemError);

        TodoItemsRepository repository = new TodoItemsInMemoryRepository();
        List<TodoItem> todoItems = repository.getAll();

        req.setAttribute("todoItems", todoItems);

        Integer emptyUpdateItemId = (Integer) session.getAttribute("emptyUpdateItemId");
        String emptyUpdateItemError = (String) session.getAttribute("emptyUpdateItem");

        req.setAttribute("emptyUpdateItemId", emptyUpdateItemId);
        req.setAttribute("emptyUpdateItem", emptyUpdateItemError);

        session.removeAttribute("emptyUpdateItemId");
        session.removeAttribute("emptyUpdateItem");

        getServletContext().getRequestDispatcher("/todo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        if (action != null) {
            TodoItemsRepository todoItemsRepository = new TodoItemsInMemoryRepository();

            switch (action) {
                case "create" -> {
                    if (req.getParameter("text") != null) {
                        String itemText = req.getParameter("text").trim();

                        if (!itemText.isEmpty()) {
                            todoItemsRepository.create(new TodoItem(itemText));
                        } else {
                            HttpSession session = req.getSession();

                            session.setAttribute("emptyCreateItem", "Заметка не может быть пустой.");
                        }
                    }
                }

                case "update" -> {
                    if (req.getParameter("text") != null) {
                        try {
                            int itemId = Integer.parseInt(req.getParameter("id"));
                            String itemText = req.getParameter("text").trim();

                            if (!itemText.isEmpty()) {
                                todoItemsRepository.update(new TodoItem(itemId, itemText));
                            } else {
                                HttpSession session = req.getSession();

                                session.setAttribute("emptyUpdateItemId", itemId);
                                session.setAttribute("emptyUpdateItem", "Заметка не может быть пустой.");
                            }
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }

                case "delete" -> {
                    try {
                        int itemId = Integer.parseInt(req.getParameter("id"));

                        todoItemsRepository.delete(itemId);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/");
    }
}
