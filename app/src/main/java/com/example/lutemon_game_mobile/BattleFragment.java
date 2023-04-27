package com.example.lutemon_game_mobile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class BattleFragment extends Fragment {

    private Spinner lutemonSpinnerA;
    private Spinner lutemonSpinnerB;
    private ImageView lutemonIconA;
    private ImageView lutemonIconB;
    private ProgressBar lutemonHealthA;
    private ProgressBar lutemonHealthB;
    private Button startBattleButton;
    private Storage storage;
    private Lutemon selectedLutemonA;
    private Lutemon selectedLutemonB;
    private TextView battleLogMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_battle, container, false);

        lutemonSpinnerA = rootView.findViewById(R.id.lutemonSpinnerA);
        lutemonSpinnerB = rootView.findViewById(R.id.lutemonSpinnerB);
        lutemonIconA = rootView.findViewById(R.id.lutemonIconA);
        lutemonIconB = rootView.findViewById(R.id.lutemonIconB);
        lutemonHealthA = rootView.findViewById(R.id.lutemonHealthA);
        lutemonHealthB = rootView.findViewById(R.id.lutemonHealthB);
        startBattleButton = rootView.findViewById(R.id.startBattleButton);
        battleLogMessage = rootView.findViewById(R.id.battleLogMessage);

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
                lutemonIconA.setImageResource(selectedLutemonA.getImageResource());
                lutemonHealthA.setMax(selectedLutemonA.getMaxHealth());
                lutemonHealthA.setProgress(selectedLutemonA.getHealth());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        lutemonSpinnerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLutemonB = (Lutemon) parent.getItemAtPosition(position);
                lutemonIconB.setImageResource(selectedLutemonB.getImageResource());
                lutemonHealthB.setMax(selectedLutemonB.getMaxHealth());
                lutemonHealthB.setProgress(selectedLutemonB.getHealth());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        startBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ... (previous checks)

                if (selectedLutemonA != null && selectedLutemonB != null && selectedLutemonA != selectedLutemonB) {
                    animateBattle();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSpinnerAdapter();
    }

    private void updateSpinnerAdapter() {
        if (getActivity() == null) {
            return;
        }

        ArrayAdapter<Lutemon> lutemonArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, storage.getLutemons());
        lutemonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lutemonSpinnerA.setAdapter(lutemonArrayAdapter);
        lutemonSpinnerB.setAdapter(lutemonArrayAdapter);
    }

    private void animateBattle() {
        // Add your animation logic here. You can use ObjectAnimator, ValueAnimator, or custom animations.
        // Update the health progress bars according to the Lutemon's health.

        final Handler handler = new Handler();
        final Runnable battleSequence = new Runnable() {
            @Override
            public void run() {
                // Perform the Battle
                if (selectedLutemonA.isAlive() && selectedLutemonB.isAlive()) {
                    int damageDealt = selectedLutemonA.attack() - selectedLutemonB.getDefense();

                    if (damageDealt > 0) {
                        selectedLutemonB.takeDamage(damageDealt);
                        lutemonHealthB.setProgress(selectedLutemonB.getHealth());
                        battleLogMessage.setText(selectedLutemonA.getName() + " dealt " + damageDealt + " damage to " + selectedLutemonB.getName() + ".");
                    } else if (damageDealt == 0) {
                        battleLogMessage.setText(selectedLutemonA.getName() + " missed their attack on " + selectedLutemonB.getName() + ".");
                    } else {
                        battleLogMessage.setText(selectedLutemonB.getName() + " evaded the attack from " + selectedLutemonA.getName() + ".");
                    }

                    if (!selectedLutemonB.isAlive()) {
                        handleBattleEnd();
                    } else {
                        // Swap Lutemons
                        Lutemon temp = selectedLutemonA;
                        selectedLutemonA = selectedLutemonB;
                        selectedLutemonB = temp;

                        // Swap health bars
                        ProgressBar tempHealth = lutemonHealthA;
                        lutemonHealthA = lutemonHealthB;
                        lutemonHealthB = tempHealth;

                        // Continue the battle sequence
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        };

        // Start the battle sequence
        handler.postDelayed(battleSequence, 1000);
    }

    private void handleBattleEnd() {
        battleLogMessage.setText(selectedLutemonB.getName() + " was defeated.");
        selectedLutemonA.addExperience(1);
        selectedLutemonA.incrementBattlesWon(); // Update battle statistics
        selectedLutemonB.incrementBattlesLost(); // Update battle statistics
        selectedLutemonA.heal(); // Heal the winning Lutemon
        selectedLutemonB.heal(); // Heal the defeated Lutemon
        selectedLutemonB.applyStatPenalty(); // Apply stat penalty to the defeated Lutemon
        lutemonHealthA.setProgress(selectedLutemonA.getHealth()); // Update health bar
        lutemonHealthB.setProgress(selectedLutemonB.getHealth()); // Update health bar
    }

}
