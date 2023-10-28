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
import edu.wkd.userappbanghangonline.model.obj.User;
import edu.wkd.userappbanghangonline.model.response.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            holder.tvComment.setVisibility(View.GONE);
        }else
            holder.tvComment.setText(commentObj.getNote());
        if (!commentObj.getImage().equals("")){
            Glide.with(context)
                    .load(ApiService.URL_MAIN+commentObj.getImage())
                    .into(holder.imgvComment);
        }else {
            holder.imgvComment.setVisibility(View.GONE);
        }

        //call user
        ApiService.apiService.getUser(commentObj.getUser_id())
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()){
                            UserResponse userResponse = response.body();
                            if (userResponse.isSuccess()){
                                List<User> user = userResponse.getResult();
                                String avatar = user.get(0).getAvatar();
                                Glide.with(context)
                                        .load(ApiService.URL_MAIN+avatar)
                                        .error(R.drawable.baseline_person_24)
                                        .into(holder.imgUserAvatar);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        if(listComment != null){
            return  listComment.size();
        }
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgvComment, imgUserAvatar;
        private TextView tvComment,tvDateTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgvComment = itemView.findViewById(R.id.imgv_item_comment);
            tvComment = itemView.findViewById(R.id.tv_item_comment);
            tvDateTime = itemView.findViewById(R.id.tv_item_date_time);
            imgUserAvatar = itemView.findViewById(R.id.img_item_avatar_user_cmt);
        }
    }
}