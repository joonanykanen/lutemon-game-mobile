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
        lutemons.add(new Lutemon("White Lutemon", 5, 4, 20, R.drawable.lutemon_white));
        lutemons.add(new Lutemon("Green Lutemon", 6, 3, 19, R.drawable.lutemon_green));
        lutemons.add(new Lutemon("Pink Lutemon", 7, 2, 18, R.drawable.lutemon_pink));
        lutemons.add(new Lutemon("Orange Lutemon", 8, 1, 17, R.drawable.lutemon_orange));
        lutemons.add(new Lutemon("Black Lutemon", 9, 0, 16, R.drawable.lutemon_black));
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
