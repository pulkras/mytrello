package ru.pulkras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Task> taskList = new ArrayList<>();
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Загрузка сохраненных данных
        loadTasks();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new TaskAdapter(taskList, this::openTaskForEditing);
        recyclerView.setAdapter(taskAdapter);

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String taskTitle = data.getStringExtra("task_title");
            ArrayList<String> subtaskDescriptions = data.getStringArrayListExtra("subtasks");

            if (requestCode == 1) {  // Adding a new task
                Task newTask = new Task(taskTitle);
                for (String desc : subtaskDescriptions) {
                    newTask.addSubtask(new Subtask(desc));
                }
                taskList.add(newTask);
            } else if (requestCode == 2) {  // Editing an existing task
                int position = data.getIntExtra("task_position", -1);
                if (position != -1) {
                    Task task = taskList.get(position);
                    task.setTitle(taskTitle);
                    task.getSubtasks().clear();
                    for (String desc : subtaskDescriptions) {
                        task.addSubtask(new Subtask(desc));
                    }
                }
            }
            taskAdapter.notifyDataSetChanged();
            saveTasks();  // Сохранение данных после изменения
        } else if (resultCode == RESULT_CANCELED && data != null) {  // Task deletion
            int position = data.getIntExtra("task_position", -1);
            if (position != -1) {
                taskList.remove(position);
                taskAdapter.notifyDataSetChanged();
                saveTasks();  // Сохранение данных после изменения
            }
        }
    }

    private void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString("task_list", json);
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task_list", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        taskList = gson.fromJson(json, type);

        if (taskList == null) {
            taskList = new ArrayList<>();
        }
    }

    private void openTaskForEditing(int position) {
        Task task = taskList.get(position);
        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
        intent.putExtra("task_title", task.getTitle());
        intent.putExtra("subtasks", convertSubtasksToStrings(task.getSubtasks()));
        intent.putExtra("task_position", position);
        startActivityForResult(intent, 2);
    }

    private ArrayList<String> convertSubtasksToStrings(List<Subtask> subtasks) {
        ArrayList<String> subtaskDescriptions = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            subtaskDescriptions.add(subtask.getDescription());
        }
        return subtaskDescriptions;
    }
}