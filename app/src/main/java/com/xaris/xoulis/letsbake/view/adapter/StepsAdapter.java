package com.xaris.xoulis.letsbake.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.databinding.StepItemBinding;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {
    private List<Step> steps;

    private int selectedPosition;
    private boolean isTablet;

    private StepClickListener listener;

    public StepsAdapter(StepClickListener listener, boolean isTablet) {
        this.listener = listener;
        this.isTablet = isTablet;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        StepItemBinding stepItemBinding = StepItemBinding.inflate(inflater, parent, false);
        return new StepsAdapterViewHolder(stepItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapterViewHolder holder, int position) {
        if (isTablet)
            holder.itemView.setSelected(selectedPosition == position);
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        if (steps != null)
            return steps.size();
        return 0;
    }

    class StepsAdapterViewHolder extends RecyclerView.ViewHolder {
        private final StepItemBinding stepItemBinding;

        StepsAdapterViewHolder(StepItemBinding stepItemBinding) {
            super(stepItemBinding.getRoot());
            this.stepItemBinding = stepItemBinding;

            stepItemBinding.getRoot().setOnClickListener(v -> {
                Step step = steps.get(getAdapterPosition());
                if (step != null && listener != null) {
                    highlightSelection();
                    listener.onStepClick(step);
                }
            });
        }

        void bind(Step step) {
            stepItemBinding.setStep(step);
        }

        private void highlightSelection() {
            if (isTablet) {
                notifyItemChanged(selectedPosition);
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
            }
        }
    }

    public interface StepClickListener {
        void onStepClick(Step step);
    }
}
