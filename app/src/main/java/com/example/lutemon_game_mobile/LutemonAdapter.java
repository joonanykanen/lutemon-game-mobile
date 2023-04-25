package com.example.lutemon_game_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LutemonAdapter extends RecyclerView.Adapter<LutemonAdapter.LutemonViewHolder> {

    private List<Lutemon> lutemons;
    private LayoutInflater inflater;

    public LutemonAdapter(Context context, List<Lutemon> lutemons) {
        this.lutemons = lutemons;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LutemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.lutemon_item, parent, false);
        return new LutemonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LutemonViewHolder holder, int position) {
        Lutemon lutemon = lutemons.get(position);
        holder.bind(lutemon);
    }

    @Override
    public int getItemCount() {
        return lutemons.size();
    }

    class LutemonViewHolder extends RecyclerView.ViewHolder {
        private TextView lutemonNameTextView;
        private TextView lutemonStatsTextView;
        private ImageView lutemonImageView;

        public LutemonViewHolder(@NonNull View itemView) {
            super(itemView);
            lutemonNameTextView = itemView.findViewById(R.id.lutemonNameTextView);
            lutemonStatsTextView = itemView.findViewById(R.id.lutemonStatsTextView);
            lutemonImageView = itemView.findViewById(R.id.lutemonImageView);
        }

        public void bind(Lutemon lutemon) {
            lutemonNameTextView.setText(lutemon.getName());
            lutemonStatsTextView.setText(lutemon.getStats());
            lutemonImageView.setImageResource(lutemon.getImageResource());
        }
    }
}
