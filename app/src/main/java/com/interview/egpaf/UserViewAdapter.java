package com.interview.egpaf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.ViewHolder> {

    private List<User> usersList;
    private Context context;

    public UserViewAdapter(List<User> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = usersList.get(position);

        holder.textViewID.setText(String.valueOf(user.getEmail()));
        holder.textViewFName.setText(user.getFirstname());
        holder.textViewSurname.setText(user.getSurname());


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewID, textViewFName, textViewSurname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFName = (TextView) itemView.findViewById(R.id.userItemFName);
            textViewSurname = (TextView) itemView.findViewById(R.id.userItemSurname);
            textViewID = (TextView) itemView.findViewById(R.id.userItemID);

        }
    }
}
