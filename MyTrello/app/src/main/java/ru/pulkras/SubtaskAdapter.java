package ru.pulkras;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder> {

    private List<Subtask> subtasks;

    public SubtaskAdapter(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @NonNull
    @Override
    public SubtaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtask, parent, false);
        return new SubtaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubtaskViewHolder holder, int position) {
        Subtask subtask = subtasks.get(position);
        holder.bind(subtask);
    }

    @Override
    public int getItemCount() {
        return subtasks.size();
    }

    static class SubtaskViewHolder extends RecyclerView.ViewHolder {
        TextView subtaskDescription;

        public SubtaskViewHolder(@NonNull View itemView) {
            super(itemView);
            subtaskDescription = itemView.findViewById(R.id.subtaskDescription);
        }

        public void bind(Subtask subtask) {
            subtaskDescription.setText(subtask.getDescription());
        }
    }
}