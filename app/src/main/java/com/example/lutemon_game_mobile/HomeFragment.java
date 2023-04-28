package com.example.lutemon_game_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;

public class HomeFragment extends Fragment {

    private RecyclerView lutemonRecyclerView;
    private LutemonAdapter lutemonAdapter;
    private Storage storage;
    private EditText lutemonNameEditText;
    private Spinner lutemonColorSpinner;
    private Button createLutemonButton;

    private Lutemon createLutemon(String name, String color) {
        int[] baseStats = getBaseStatsForColor(color);
        int imageResource = getImageResourceForColor(color);

        int attack = baseStats[0];
        int defense = baseStats[1];
        int maxHealth = baseStats[2];

        return new Lutemon(name, color, attack, defense, maxHealth, imageResource);
    }

    private int[] getBaseStatsForColor(String color) {
        String[] lutemonColors = getResources().getStringArray(R.array.lutemon_colors);
        int colorIndex = Arrays.asList(lutemonColors).indexOf(color);

        if (colorIndex == -1) {
            throw new IllegalStateException("Unexpected value: " + color);
        }

        int[][] lutemonBaseStats = new int[lutemonColors.length][];

        for (int i = 0; i < lutemonColors.length; i++) {
            lutemonBaseStats[i] = getResources().getIntArray(getResources().obtainTypedArray(R.array.lutemon_base_stats).getResourceId(i, 0));
        }

        return lutemonBaseStats[colorIndex];
    }

    private int getImageResourceForColor(String color) {
        String[] lutemonColors = getResources().getStringArray(R.array.lutemon_colors);
        int colorIndex = Arrays.asList(lutemonColors).indexOf(color);

        if (colorIndex == -1) {
            throw new IllegalStateException("Unexpected value: " + color);
        }

        TypedArray imageResources = getResources().obtainTypedArray(R.array.lutemon_image_resources);
        int imageResource = imageResources.getResourceId(colorIndex, 0);
        imageResources.recycle();

        return imageResource;
    }

    private class LutemonSwipeCallback extends ItemTouchHelper.SimpleCallback {
        public LutemonSwipeCallback() {
            super(0, ItemTouchHelper.LEFT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Let Go Lutemon")
                    .setMessage("Are you sure you want to let go this Lutemon?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            storage.removeLutemon(position);
                            storage.saveLutemons();
                            lutemonAdapter.notifyItemRemoved(position);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lutemonAdapter.notifyItemChanged(position);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View itemView = viewHolder.itemView;
            int itemHeight = itemView.getHeight();

            // Set the background color
            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(getActivity(), R.color.swipe_background));

            // Draw the background color
            RectF backgroundRect = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            c.drawRect(backgroundRect, paint);

            // Set the "let go" icon
            Drawable letGoIcon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_let_go);
            letGoIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.swipe_icon), PorterDuff.Mode.SRC_ATOP);

            // Calculate the icon's position
            int letGoIconTop = itemView.getTop() + (itemHeight - letGoIcon.getIntrinsicHeight()) / 2;
            int letGoIconMargin = (itemHeight - letGoIcon.getIntrinsicHeight()) / 2;
            int letGoIconLeft = itemView.getRight() - letGoIconMargin - letGoIcon.getIntrinsicWidth();
            int letGoIconRight = itemView.getRight() - letGoIconMargin;
            int letGoIconBottom = letGoIconTop + letGoIcon.getIntrinsicHeight();

            // Draw the "let go" icon
            letGoIcon.setBounds(letGoIconLeft, letGoIconTop, letGoIconRight, letGoIconBottom);
            letGoIcon.draw(c);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        lutemonRecyclerView = rootView.findViewById(R.id.lutemonRecyclerView);
        lutemonNameEditText = rootView.findViewById(R.id.lutemonNameEditText);
        lutemonColorSpinner = rootView.findViewById(R.id.lutemonColorSpinner);
        createLutemonButton = rootView.findViewById(R.id.createLutemonButton);

        storage = Storage.getInstance(getActivity());
        lutemonAdapter = new LutemonAdapter(getActivity(), storage.getLutemons());
        lutemonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lutemonRecyclerView.setAdapter(lutemonAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new LutemonSwipeCallback());
        itemTouchHelper.attachToRecyclerView(lutemonRecyclerView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lutemon_colors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lutemonColorSpinner.setAdapter(adapter);

        createLutemonButton.setOnClickListener(v -> {
            String name = lutemonNameEditText.getText().toString();
            String color = lutemonColorSpinner.getSelectedItem().toString();
            Lutemon newLutemon = createLutemon(name, color);
            storage.addLutemon(newLutemon);
            storage.saveLutemons();
            lutemonAdapter.notifyDataSetChanged();
            lutemonNameEditText.setText("");
        });

        return rootView;
    }
}
