package ru.pulkras;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskSubtaskAdapter extends RecyclerView.Adapter<TaskSubtaskAdapter.SubtaskViewHolder> {

    private List<Subtask> subtasks;
    private OnSubtaskChangedListener onSubtaskChangedListener;

    public interface OnSubtaskChangedListener {
        void onSubtaskUpdated(int position, String newDescription);
        void onSubtaskDeleted(int position);
    }

    public TaskSubtaskAdapter(List<Subtask> subtasks, OnSubtaskChangedListener onSubtaskChangedListener) {
        this.subtasks = subtasks;
        this.onSubtaskChangedListener = onSubtaskChangedListener;
    }

    @NonNull
    @Override
    public SubtaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_subtask, parent, false);
        return new SubtaskViewHolder(view, onSubtaskChangedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubtaskViewHolder holder, int position) {
        Subtask subtask = subtasks.get(position);
        holder.bind(subtask, position);
    }

    @Override
    public int getItemCount() {
        return subtasks.size();
    }

    static class SubtaskViewHolder extends RecyclerView.ViewHolder {
        EditText subtaskDescription;
        Button deleteSubtaskButton;

        public SubtaskViewHolder(@NonNull View itemView, OnSubtaskChangedListener listener) {
            super(itemView);
            subtaskDescription = itemView.findViewById(R.id.subtaskDescriptionInput);
            deleteSubtaskButton = itemView.findViewById(R.id.deleteSubtaskButton);

            subtaskDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listener.onSubtaskUpdated(getAdapterPosition(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            deleteSubtaskButton.setOnClickListener(v -> {
                listener.onSubtaskDeleted(getAdapterPosition());
            });
        }

        public void bind(Subtask subtask, int position) {
            subtaskDescription.setText(subtask.getDescription());
        }
    }
}