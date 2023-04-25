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
        lutemons.add(new Lutemon("Lutemon A", 10, 10, 10, R.drawable.lutemon_white));
        lutemons.add(new Lutemon("Lutemon B", 12, 8, 12, R.drawable.lutemon_green));
        lutemons.add(new Lutemon("Lutemon C", 8, 14, 8, R.drawable.lutemon_pink));
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
