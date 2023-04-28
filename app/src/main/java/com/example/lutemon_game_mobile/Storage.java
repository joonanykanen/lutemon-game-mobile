package com.example.lutemon_game_mobile;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private static Storage instance;
    private List<Lutemon> lutemons;
    private static final String LUTEMON_PREFS = "LutemonPrefs";
    private static final String LUTEMON_KEY = "Lutemons";
    private Context context;
    private Storage(Context context) {
        this.context = context;
        lutemons = new ArrayList<>();
        loadLutemons();
    }

    public static synchronized Storage getInstance(Context context) {
        if (instance == null) {
            instance = new Storage(context);
        }
        return instance;
    }

    public void saveLutemons() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LUTEMON_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lutemons);
        editor.putString(LUTEMON_KEY, json);
        editor.apply();
    }

    public void loadLutemons() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LUTEMON_PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LUTEMON_KEY, null);
        Type type = new TypeToken<ArrayList<Lutemon>>() {}.getType();
        List<Lutemon> loadedLutemons = gson.fromJson(json, type);

        if (loadedLutemons != null) {
            lutemons.addAll(loadedLutemons);
        } else {
            initializeLutemons();
        }
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

    public void removeLutemon(int position) {
        lutemons.remove(position);
    }


}
