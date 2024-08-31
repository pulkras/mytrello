package ru.pulkras;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private EditText taskTitleInput;
    private EditText subtaskInput;
    private List<Subtask> subtasks = new ArrayList<>();
    private TaskSubtaskAdapter subtaskAdapter;
    private int taskPosition = -1;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskTitleInput = findViewById(R.id.taskTitleInput);
        subtaskInput = findViewById(R.id.subtaskInput);
        Button addSubtaskButton = findViewById(R.id.addSubtaskButton);
        Button saveTaskButton = findViewById(R.id.saveTaskButton);
        Button deleteTaskButton = findViewById(R.id.deleteTaskButton);
        RecyclerView subtasksRecyclerView = findViewById(R.id.subtasksRecyclerView);

        subtasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subtaskAdapter = new TaskSubtaskAdapter(subtasks, new TaskSubtaskAdapter.OnSubtaskChangedListener() {
            @Override
            public void onSubtaskUpdated(int position, String newDescription) {
                subtasks.get(position).setDescription(newDescription);
            }

            @Override
            public void onSubtaskDeleted(int position) {
                subtasks.remove(position);
                subtaskAdapter.notifyItemRemoved(position);
            }
        });
        subtasksRecyclerView.setAdapter(subtaskAdapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("task_title")) {
            isEditing = true;
            taskPosition = intent.getIntExtra("task_position", -1);
            taskTitleInput.setText(intent.getStringExtra("task_title"));
            ArrayList<String> subtaskDescriptions = intent.getStringArrayListExtra("subtasks");
            for (String desc : subtaskDescriptions) {
                subtasks.add(new Subtask(desc));
            }
            subtaskAdapter.notifyDataSetChanged();
        }

        addSubtaskButton.setOnClickListener(v -> {
            String subtaskText = subtaskInput.getText().toString();
            if (!subtaskText.isEmpty()) {
                subtasks.add(new Subtask(subtaskText));
                subtaskInput.setText("");
                subtaskAdapter.notifyItemInserted(subtasks.size() - 1);
            }
        });

        saveTaskButton.setOnClickListener(v -> {
            String taskTitle = taskTitleInput.getText().toString();
            if (!taskTitle.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("task_title", taskTitle);
                resultIntent.putStringArrayListExtra("subtasks", convertSubtasksToStrings());
                resultIntent.putExtra("task_position", taskPosition);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        deleteTaskButton.setOnClickListener(v -> {
            if (isEditing) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("task_position", taskPosition);
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });
    }

    private ArrayList<String> convertSubtasksToStrings() {
        ArrayList<String> subtaskDescriptions = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            subtaskDescriptions.add(subtask.getDescription());
        }
        return subtaskDescriptions;
    }
}