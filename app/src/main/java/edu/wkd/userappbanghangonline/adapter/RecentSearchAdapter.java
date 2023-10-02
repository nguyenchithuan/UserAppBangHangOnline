package edu.wkd.userappbanghangonline.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.databinding.LayoutItemRecentSerachBinding;
import edu.wkd.userappbanghangonline.model.obj.RecentSearch;


public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
    private ArrayList<RecentSearch> list;

    public RecentSearchAdapter(ArrayList<RecentSearch> list) {
        this.list = list;
    }

    public void setData(ArrayList<RecentSearch> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemRecentSerachBinding binding = LayoutItemRecentSerachBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentSearch obj = list.get(position);
        holder.binding.tvTitleRecentSearch.setText(obj.getTitle());
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemRecentSerachBinding binding;
        public ViewHolder(LayoutItemRecentSerachBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
