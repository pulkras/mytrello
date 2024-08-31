package ru.pulkras;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String title;
    private List<Subtask> subtasks;

    public Task(String title) {
        this.title = title;
        this.subtasks = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }
}