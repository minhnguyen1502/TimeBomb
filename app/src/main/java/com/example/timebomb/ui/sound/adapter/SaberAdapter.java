package com.example.timebomb.ui.sound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timebomb.databinding.ItemSoundBinding;
import com.example.timebomb.ui.sound.model.Saber;
import com.example.timebomb.ui.sound.model.SoundModel;

import java.util.List;

public class SaberAdapter extends RecyclerView.Adapter<SaberAdapter.SaberHolder> {
    private List<Saber> saberList;
    private Context context;
    private ClickItem clickItem;

    public SaberAdapter(List<Saber> saberList, Context context, ClickItem clickItem) {
        this.saberList = saberList;
        this.context = context;
        this.clickItem = clickItem;
    }

    @NonNull
    @Override
    public SaberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSoundBinding binding = ItemSoundBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new SaberHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SaberHolder holder, int position) {
        Saber saber = saberList.get(position);
        holder.binding.img.setImageResource(saber.getImg());
        holder.itemView.setOnClickListener(v -> clickItem.clickItem(position, saber));

    }

    @Override
    public int getItemCount() {
        if (saberList != null) {
            return saberList.size();
        } else {
            return 0;
        }
    }

    public class SaberHolder extends RecyclerView.ViewHolder {
        ItemSoundBinding binding;

        public SaberHolder(ItemSoundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ClickItem {
        void clickItem(int position, Saber saber);
    }
}
