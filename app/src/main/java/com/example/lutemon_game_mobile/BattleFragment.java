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
import android.widget.Spinner;
import android.widget.TextView;

public class BattleFragment extends Fragment {

    private Spinner lutemonSpinnerA;
    private Spinner lutemonSpinnerB;
    private TextView battleLogTextView;
    private Button startBattleButton;
    private Storage storage;
    private Lutemon selectedLutemonA;
    private Lutemon selectedLutemonB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_battle, container, false);

        lutemonSpinnerA = rootView.findViewById(R.id.lutemonSpinnerA);
        lutemonSpinnerB = rootView.findViewById(R.id.lutemonSpinnerB);
        battleLogTextView = rootView.findViewById(R.id.battleLogTextView);
        startBattleButton = rootView.findViewById(R.id.startBattleButton);

        storage = Storage.getInstance();

        ArrayAdapter<Lutemon> lutemonArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, storage.getLutemons());
        lutemonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lutemonSpinnerA.setAdapter(lutemonArrayAdapter);
        lutemonSpinnerB.setAdapter(lutemonArrayAdapter);

        lutemonSpinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLutemonA = (Lutemon) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        lutemonSpinnerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLutemonB = (Lutemon) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        startBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLutemonA != null && selectedLutemonB != null && selectedLutemonA != selectedLutemonB) {
                    StringBuilder battleLog = new StringBuilder();
                    Lutemon.performBattle(selectedLutemonA, selectedLutemonB, battleLog);
                    battleLogTextView.setText(battleLog.toString());
                }
            }
        });

        return rootView;
    }
}
