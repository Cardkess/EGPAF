package com.interview.egpaf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordViewAdapter extends RecyclerView.Adapter<RecordViewAdapter.ViewHolder>{

    private List<Record> recordsList;
    private Context context;

    public RecordViewAdapter(List<Record> recordsList, Context context) {
        this.recordsList = recordsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Record record = recordsList.get(position);

        holder.recordItemDate.setText(record.getDate());
        holder.recordItemDiagnosis.setText(record.getDiagnosis());
        holder.recordItemWeight.setText(record.getWeight());
        holder.recordItemHeight.setText(record.getHeight());
        holder.recordItemTempReading.setText(record.getTemp_reading());

    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView recordItemDate, recordItemDiagnosis, recordItemWeight, recordItemHeight, recordItemTempReading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recordItemDate = (TextView) itemView.findViewById(R.id.recordItemDate);
            recordItemDiagnosis = (TextView) itemView.findViewById(R.id.recordItemDiagnosis);
            recordItemWeight = (TextView) itemView.findViewById(R.id.recordItemWeight);
            recordItemHeight = (TextView) itemView.findViewById(R.id.recordItemHeight);
            recordItemTempReading = (TextView) itemView.findViewById(R.id.recordItemTempReading);
        }
    }
}
