package com.example.timebomb.ui.background;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timebomb.databinding.ItemBackgroundBinding;
import com.example.timebomb.databinding.ItemSoundBinding;
import com.example.timebomb.ui.sound.adapter.SoundAdapter;
import com.example.timebomb.ui.sound.model.SoundModel;

import java.util.List;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.BackgroundHolder> {

    private final Context context;
    private final List<BackgroundModel> backgroundList;
    private int selectedPosition; // Add selectedPosition here
    ClickItem clickItem;

    // Updated constructor to accept selectedPosition
    public BackgroundAdapter(Context context, List<BackgroundModel> backgroundList, int selectedPosition, ClickItem clickItem) {
        this.context = context;
        this.backgroundList = backgroundList;
        this.selectedPosition = selectedPosition; // Initialize it
        this.clickItem = clickItem;
    }

    @NonNull
    @Override
    public BackgroundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBackgroundBinding binding = ItemBackgroundBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new BackgroundHolder(binding);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull BackgroundHolder holder, @SuppressLint("RecyclerView") int position) {
        BackgroundModel backgroundModel = backgroundList.get(position);
        holder.binding.img.setImageResource(backgroundModel.getImg());
        if (selectedPosition == position) {
            holder.binding.choose.setVisibility(View.VISIBLE);
        } else {
            holder.binding.choose.setVisibility(View.INVISIBLE);
        }
        holder.binding.img.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            clickItem.clickItem(position, backgroundModel);
        });


    }

    @Override
    public int getItemCount() {
        if (backgroundList != null) {
            return backgroundList.size();
        } else {
            return 0;
        }
    }

    public class BackgroundHolder extends RecyclerView.ViewHolder {
        ItemBackgroundBinding binding;

        public BackgroundHolder(ItemBackgroundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ClickItem {
        void clickItem(int position, BackgroundModel backgroundModel);
    }
}
