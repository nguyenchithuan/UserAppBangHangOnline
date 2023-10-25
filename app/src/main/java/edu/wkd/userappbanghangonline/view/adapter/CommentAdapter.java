package edu.wkd.userappbanghangonline.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.LayoutItemCommentBinding;
import edu.wkd.userappbanghangonline.model.obj.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> list;

    public CommentAdapter(ArrayList<Comment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemCommentBinding binding = LayoutItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = list.get(position);
        if (comment == null){
            return;
        }
        holder.binding.tvNoteComment.setText(comment.getNote());
        holder.binding.tvUsernameComment.setText(comment.getUsername());
        holder.binding.imgComment.setVisibility(View.VISIBLE);
        if(comment.getAvatar().contains("uploads")) {
            Glide.with(holder.itemView.getContext())
                    .load("https://guyinterns2003.000webhostapp.com/" + comment.getAvatar())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.user_circle)
                    .into(holder.binding.avatarUser);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(comment.getImage())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.user_circle)
                    .into(holder.binding.avatarUser);
        }
        if (comment.getImage() != null){
            holder.binding.imgComment.setVisibility(View.VISIBLE);
            if(comment.getImage().contains("uploads")) {
                Glide.with(holder.itemView.getContext())
                        .load("https://guyinterns2003.000webhostapp.com/" + comment.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(holder.binding.imgComment);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(comment.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(holder.binding.imgComment);
            }
        }
        holder.binding.tvDatetimeComment.setText(comment.getDateTime());

    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LayoutItemCommentBinding binding;
        public ViewHolder(@NonNull LayoutItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
