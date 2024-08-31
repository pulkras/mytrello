package ru.pulkras;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnTaskClickListener onTaskClickListener;

    public interface OnTaskClickListener {
        void onTaskClick(int position);
    }

    public TaskAdapter(List<Task> tasks, OnTaskClickListener onTaskClickListener) {
        this.tasks = tasks;
        this.onTaskClickListener = onTaskClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view, onTaskClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskTitle;
        OnTaskClickListener onTaskClickListener;

        public TaskViewHolder(@NonNull View itemView, OnTaskClickListener onTaskClickListener) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            this.onTaskClickListener = onTaskClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Task task) {
            taskTitle.setText(task.getTitle());
        }

        @Override
        public void onClick(View v) {
            onTaskClickListener.onTaskClick(getAdapterPosition());
        }
    }
}