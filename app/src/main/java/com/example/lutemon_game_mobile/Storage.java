package com.example.lutemon_game_mobile;

import java.util.ArrayList;
import java.util.List;

public class Storage {

    private static Storage instance;
    private List<Lutemon> lutemons;

    private Storage() {
        lutemons = new ArrayList<>();
        initializeLutemons();
    }

    public static synchronized Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    private void initializeLutemons() {
        // Add Lutemons here
        lutemons.add(new Lutemon("White Lutemon", "White", 6, 6, 20, R.drawable.lutemon_white));
        lutemons.add(new Lutemon("Green Lutemon", "Green", 4, 8, 18, R.drawable.lutemon_green));
        lutemons.add(new Lutemon("Pink Lutemon", "Pink", 5, 7, 22, R.drawable.lutemon_pink));
        lutemons.add(new Lutemon("Orange Lutemon", "Orange", 5, 5, 25, R.drawable.lutemon_orange));
        lutemons.add(new Lutemon("Black Lutemon", "Black", 9, 3, 20, R.drawable.lutemon_black));
        // ...
    }

    public List<Lutemon> getLutemons() {
        return lutemons;
    }

    public void addLutemon(Lutemon lutemon) {
        lutemons.add(lutemon);
    }

    public void removeLutemon(Lutemon lutemon) {
        lutemons.remove(lutemon);
    }

}
