package com.example.timebomb.ui.sound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timebomb.databinding.ItemSoundBinding;
import com.example.timebomb.ui.sound.model.SoundModel;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundHolder> {
    private List<SoundModel> soundLists;
    private Context context;
    private ClickItem clickItem;

    public SoundAdapter(List<SoundModel> soundLists, Context context, ClickItem clickItem) {
        this.soundLists = soundLists;
        this.context = context;
        this.clickItem = clickItem;
    }

    @NonNull
    @Override
    public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSoundBinding binding = ItemSoundBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new SoundHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
        SoundModel soundModel = soundLists.get(position);
        holder.binding.img.setImageResource(soundModel.getImg());
        holder.itemView.setOnClickListener(v -> clickItem.clickItem(position));

    }

    @Override
    public int getItemCount() {
        if (soundLists != null) {
            return soundLists.size();
        } else {
            return 0;
        }
    }

    public class SoundHolder extends RecyclerView.ViewHolder {
        ItemSoundBinding binding;

        public SoundHolder(ItemSoundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ClickItem {
        void clickItem(int position);
    }
}
