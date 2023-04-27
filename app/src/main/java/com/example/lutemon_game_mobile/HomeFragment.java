package com.example.lutemon_game_mobile;

import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        lutemonRecyclerView = rootView.findViewById(R.id.lutemonRecyclerView);
        lutemonNameEditText = rootView.findViewById(R.id.lutemonNameEditText);
        lutemonColorSpinner = rootView.findViewById(R.id.lutemonColorSpinner);
        createLutemonButton = rootView.findViewById(R.id.createLutemonButton);

        storage = Storage.getInstance();
        lutemonAdapter = new LutemonAdapter(getActivity(), storage.getLutemons());
        lutemonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lutemonRecyclerView.setAdapter(lutemonAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lutemon_colors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lutemonColorSpinner.setAdapter(adapter);

        createLutemonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = lutemonNameEditText.getText().toString();
                String color = lutemonColorSpinner.getSelectedItem().toString();
                Lutemon newLutemon = createLutemon(name, color);
                storage.addLutemon(newLutemon);
                lutemonAdapter.notifyDataSetChanged();
                lutemonNameEditText.setText("");
            }
        });

        return rootView;
    }
}
