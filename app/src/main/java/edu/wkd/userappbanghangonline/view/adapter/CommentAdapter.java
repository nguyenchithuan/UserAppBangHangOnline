package edu.wkd.userappbanghangonline.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.model.obj.Comment;
import edu.wkd.userappbanghangonline.model.obj.Product;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> listComment;

    public List<Comment> getListComment() {
        return listComment;
    }

    public CommentAdapter(Context context, List<Comment> listComment) {
        this.context = context;
        this.listComment = listComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.listComment = listComment;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_comment,parent,false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment commentObj = listComment.get(position);
        if (commentObj == null) {
            return;
        }
        holder.tvDateTime.setText("Ngày: "+commentObj.getDatetime());
        if (commentObj.getNote() == null){
            holder.tvComment.setVisibility(View.INVISIBLE);
        }else
            holder.tvComment.setText(commentObj.getNote());
        if (commentObj.getImage() != null){
            Glide.with(context)
                    .load(ApiService.URL_MAIN+commentObj.getImage())
                    .into(holder.imgvComment);
        }else {
            holder.imgvComment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(listComment != null){
            return  listComment.size();
        }
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgvComment;
        private TextView tvComment,tvDateTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgvComment = itemView.findViewById(R.id.imgv_item_comment);
            tvComment = itemView.findViewById(R.id.tv_item_comment);
            tvDateTime = itemView.findViewById(R.id.tv_item_date_time);
        }
    }
}
