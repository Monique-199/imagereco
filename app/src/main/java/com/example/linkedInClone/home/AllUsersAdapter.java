package com.example.linkedInClone.home;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkedInClone.R;

import java.util.List;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.AllUsersViewHolder> {

    private List<UserProfile> userList;
    private OnItemClickListener listener;

    public AllUsersAdapter(List<UserProfile> userList) {
        this.userList = userList;
    }

    public interface OnItemClickListener {
        void onItemClick(UserProfile user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercard_view, parent, false);
        return new AllUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersViewHolder holder, int position) {
        UserProfile user = userList.get(position);

        // Bind data to the ViewHolder's views
        holder.profileImageView.setImageResource(user.getProfilePicture());
        holder.usernameTextView.setText(user.getUsername());
        holder.genderTextView.setText(user.getGender());
        holder.phoneNumberTextView.setText(user.getPhoneNumber());
        holder.shortBioTextView.setText(user.getShortBio());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class AllUsersViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView usernameTextView, genderTextView, phoneNumberTextView, shortBioTextView;

        public AllUsersViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView); // Replace with the appropriate resource ID
            usernameTextView = itemView.findViewById(R.id.usernameTextView); // Replace with the appropriate resource ID
            genderTextView = itemView.findViewById(R.id.genderTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneTextView);
            shortBioTextView = itemView.findViewById(R.id.shortBioTextView);
        }
    }
}