package ru.academits.orlov.todolistjsp.repositories;

import ru.academits.orlov.todolistjsp.entities.TodoItem;

import java.util.List;

public interface TodoItemsRepository {
    List<TodoItem> getAll();

    void create(TodoItem item);

    void update(TodoItem item) throws IllegalArgumentException;

    void delete(int id);
}
