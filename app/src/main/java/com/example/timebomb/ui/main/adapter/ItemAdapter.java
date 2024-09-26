package com.example.timebomb.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timebomb.databinding.ItemHomeBinding;
import com.example.timebomb.ui.main.model.ItemModel;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<ItemModel> listItem;
    private ClickItem clickItem;
    private Context context;

    public ItemAdapter(List<ItemModel> listItem, ClickItem clickItem, Context context) {
        this.listItem = listItem;
        this.clickItem = clickItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeBinding binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel itemModel = listItem.get(position);
        holder.binding.ivImg.setImageResource(itemModel.getImg());
        holder.binding.tvName.setText(itemModel.getName());
        holder.itemView.setOnClickListener(v -> clickItem.clickItem(position));
    }

    @Override
    public int getItemCount() {
        if (listItem != null) {
            return listItem.size();
        } else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemHomeBinding binding;
        public ItemViewHolder(ItemHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ClickItem{
        void clickItem(int position);
    }
}
