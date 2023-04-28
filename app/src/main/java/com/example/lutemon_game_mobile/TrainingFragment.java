package com.example.lutemon_game_mobile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class TrainingFragment extends Fragment {

    private Spinner lutemonSpinner;
    private TextView lutemonStatsTextView;
    private Button trainLutemonButton;
    private Storage storage;
    private Lutemon selectedLutemon;

    private ImageView lutemonImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_training, container, false);

        lutemonSpinner = rootView.findViewById(R.id.lutemonSpinner);
        lutemonStatsTextView = rootView.findViewById(R.id.lutemonStatsTextView);
        trainLutemonButton = rootView.findViewById(R.id.trainLutemonButton);
        lutemonImageView = rootView.findViewById(R.id.lutemonImageView);

        storage = Storage.getInstance(getActivity());

        ArrayAdapter<Lutemon> lutemonArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, storage.getLutemons());
        lutemonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lutemonSpinner.setAdapter(lutemonArrayAdapter);

        // Check if there are no Lutemons available
        if (storage.getLutemons().isEmpty()) {
            lutemonStatsTextView.setText("No Lutemons available. Please add Lutemons first.");
            lutemonSpinner.setEnabled(false);
            trainLutemonButton.setEnabled(false);
            return rootView;
        }

        lutemonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLutemon = (Lutemon) parent.getItemAtPosition(position);
                updateLutemonStats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        trainLutemonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLutemon != null) {
                    selectedLutemon.train();
                    updateLutemonStats();
                    storage.saveLutemons(); // Save the Lutemons' stats
                }
            }
        });

        return rootView;
    }

    private void updateLutemonStats() {
        if (selectedLutemon != null) {
            lutemonImageView.setImageResource(selectedLutemon.getImageResource());
            lutemonStatsTextView.setText(selectedLutemon.getStats());
        } else {
            lutemonImageView.setImageDrawable(null);
            lutemonStatsTextView.setText("");
        }
    }
}
