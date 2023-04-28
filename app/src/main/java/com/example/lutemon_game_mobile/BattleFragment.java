package com.example.lutemon_game_mobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
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
    private int turn;
    private TextView lutemonHealthTextA;
    private TextView lutemonHealthTextB;

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
        lutemonHealthTextA = rootView.findViewById(R.id.lutemonHealthTextA);
        lutemonHealthTextB = rootView.findViewById(R.id.lutemonHealthTextB);

        storage = Storage.getInstance(getActivity());

        ArrayAdapter<Lutemon> lutemonArrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, storage.getLutemons());
        lutemonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lutemonSpinnerA.setAdapter(lutemonArrayAdapter);
        lutemonSpinnerB.setAdapter(lutemonArrayAdapter);

        // Check if there are no Lutemons available
        if (storage.getLutemons().isEmpty()) {
            battleLogMessage.setText("No Lutemons available. Please add Lutemons first.");
            lutemonSpinnerA.setEnabled(false);
            lutemonSpinnerB.setEnabled(false);
            startBattleButton.setEnabled(false);
            return rootView;
        }

        lutemonSpinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLutemonA = (Lutemon) parent.getItemAtPosition(position);
                lutemonIconA.setImageResource(selectedLutemonA.getImageResource());
                lutemonHealthA.setMax(selectedLutemonA.getMaxHealth());
                lutemonHealthA.setProgress(selectedLutemonA.getHealth());
                lutemonHealthTextA.setText(selectedLutemonA.getHealth() + "/" + selectedLutemonA.getMaxHealth());
                updateHealthBarColor(lutemonHealthA, selectedLutemonA.getHealth(), selectedLutemonA.getMaxHealth());

                // Update Spinner B selection
                if (selectedLutemonA == selectedLutemonB && lutemonSpinnerB.getAdapter().getCount() > 1) {
                    lutemonSpinnerB.setSelection((position + 1) % lutemonSpinnerB.getAdapter().getCount());
                }
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
                lutemonHealthTextB.setText(selectedLutemonB.getHealth() + "/" + selectedLutemonB.getMaxHealth());
                updateHealthBarColor(lutemonHealthB, selectedLutemonB.getHealth(), selectedLutemonB.getMaxHealth());

                // Update Spinner A selection
                if (selectedLutemonA == selectedLutemonB && lutemonSpinnerA.getAdapter().getCount() > 1) {
                    lutemonSpinnerA.setSelection((position + 1) % lutemonSpinnerA.getAdapter().getCount());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        startBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turn = 0;

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

        final Handler handler = new Handler();
        final Runnable battleSequence = new Runnable() {
            @Override
            public void run() {
                // Perform the Battle
                if (selectedLutemonA.isAlive() && selectedLutemonB.isAlive()) {
                    boolean isCriticalHit = selectedLutemonA.isCriticalHit();
                    ImageView attacker = (turn % 2 == 0) ? lutemonIconA : lutemonIconB;
                    ImageView defender = (turn % 2 == 0) ? lutemonIconB : lutemonIconA;
                    animateAttack(attacker, defender, isCriticalHit);

                    int damageDealt = selectedLutemonA.attack() - selectedLutemonB.getDefense();

                    if (damageDealt > 0) {
                        selectedLutemonB.takeDamage(damageDealt);
                        lutemonHealthB.setProgress(selectedLutemonB.getHealth());
                        lutemonHealthTextB.setText(selectedLutemonB.getHealth() + "/" + selectedLutemonB.getMaxHealth());
                        updateHealthBarColor(lutemonHealthB, selectedLutemonB.getHealth(), selectedLutemonB.getMaxHealth());
                        battleLogMessage.setText(selectedLutemonA.getName() + " dealt " + damageDealt + " damage to " + selectedLutemonB.getName() + ".");
                    } else if (damageDealt == 0) {
                        battleLogMessage.setText(selectedLutemonA.getName() + " missed their attack on " + selectedLutemonB.getName() + ".");
                    } else {
                        if (selectedLutemonA.attack() < selectedLutemonB.getDefense()) {
                            selectedLutemonB.takeDamage(1); // Apply minimum damage
                            lutemonHealthB.setProgress(selectedLutemonB.getHealth());
                            lutemonHealthTextB.setText(selectedLutemonB.getHealth() + "/" + selectedLutemonB.getMaxHealth());
                            updateHealthBarColor(lutemonHealthB, selectedLutemonB.getHealth(), selectedLutemonB.getMaxHealth());
                            battleLogMessage.setText(selectedLutemonA.getName() + " dealt minimal damage to " + selectedLutemonB.getName() + ".");
                        } else {
                            battleLogMessage.setText(selectedLutemonB.getName() + " evaded the attack from " + selectedLutemonA.getName() + ".");
                        }
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

                        // Swap health bar texts
                        TextView tempHealthText = lutemonHealthTextA;
                        lutemonHealthTextA = lutemonHealthTextB;
                        lutemonHealthTextB = tempHealthText;

                        turn++;
                        // Continue the battle sequence with a random delay between 1000 ms and 2000 ms
                        handler.postDelayed(this, 1000 + new Random().nextInt(1000));
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
        lutemonHealthTextA.setText(selectedLutemonA.getHealth() + "/" + selectedLutemonA.getMaxHealth());
        updateHealthBarColor(lutemonHealthA, selectedLutemonA.getHealth(), selectedLutemonA.getMaxHealth());
        lutemonHealthB.setProgress(selectedLutemonB.getHealth()); // Update health bar
        lutemonHealthTextB.setText(selectedLutemonB.getHealth() + "/" + selectedLutemonB.getMaxHealth());
        updateHealthBarColor(lutemonHealthB, selectedLutemonB.getHealth(), selectedLutemonB.getMaxHealth());
        storage.saveLutemons(); // Save the Lutemons' stats
    }

    private void playAttackSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.hit_sound);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }

    private void playCriticalHitSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.hit_critical_sound);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }

    private void animateAttack(final ImageView attacker, final ImageView defender, final boolean isCriticalHit) {
        ObjectAnimator attackAnimation = ObjectAnimator.ofFloat(attacker, "translationX", 0, defender.getX() - attacker.getX(), 0);
        attackAnimation.setDuration(500);

        if (isCriticalHit && attacker == lutemonIconA) {
            // Scale animation for critical hit
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(attacker, "scaleX", -1.0f, -1.5f, -1.0f);
            scaleX.setDuration(500);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(attacker, "scaleY", 1.0f, 1.5f, 1.0f);
            scaleY.setDuration(500);
            scaleX.start();
            scaleY.start();
        } else if (isCriticalHit && attacker == lutemonIconB) {
            // Scale animation for critical hit
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(attacker, "scaleX", 1.0f, 1.5f, 1.0f);
            scaleX.setDuration(500);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(attacker, "scaleY", 1.0f, 1.5f, 1.0f);
            scaleY.setDuration(500);
            scaleX.start();
            scaleY.start();
        }

        attackAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isCriticalHit) {
                    playCriticalHitSound();
                } else {
                    playAttackSound();
                }
            }
        });

        attackAnimation.start();
    }
    private void updateHealthBarColor(ProgressBar healthBar, int currentHealth, int maxHealth) {
        double healthPercentage = (double) currentHealth / maxHealth;
        if (healthPercentage <= 0.2) {
            healthBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        } else {
            healthBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }
    }

}
