package com.example.lutemon_game_mobile;

import java.util.Random;

public class Lutemon {
    private String name;
    private String color;
    private int attack;
    private int defense;
    private int experience;
    private int health;
    private int maxHealth;
    private int id;
    private int imageResource;

    private int battlesWon;
    private int battlesLost;
    private static int idCounter = 0;

    public Lutemon(String name, String color, int attack, int defense, int maxHealth, int imageResource) {
        this.name = name;
        this.color = color;
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.experience = 0;
        this.imageResource = imageResource;
        this.battlesWon = 0;
        this.battlesLost = 0;
        this.id = idCounter++;
    }
    private static final Random random = new Random();

    public boolean isCriticalHit() {
        // Critical hit chance: 10%
        return random.nextInt(10) == 0;
    }

    private boolean isMiss() {
        // Miss chance: 20%
        return random.nextInt(5) == 0;
    }

    private boolean isEvade() {
        // Evasion chance: 15%
        return random.nextInt(100) < 15;
    }

    public int attack() {
        int baseAttack = attack + experience * 2;

        if (isMiss()) {
            baseAttack = 0;
        } else if (isCriticalHit()) {
            baseAttack *= 2;
        }

        return baseAttack;
    }

    public void defense(Lutemon attacker) {
        int damage = attacker.attack() - defense;

        if (isEvade()) {
            damage = 0;
        }

        if (damage > 0) {
            health -= damage;
        }
    }

    public void applyStatPenalty() {
        int penaltyFactor = 2;
        attack = Math.max(1, attack - penaltyFactor);
        defense = Math.max(1, defense - penaltyFactor);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public void train() {
        experience++;
    }

    public String getStats() {
        return "Color: " + color + ", Attack: " + attack + ", Defense: " + defense + ", Experience: " + experience + ", Health: " + health + "/" + maxHealth;
    }

    public void incrementBattlesWon() {
        battlesWon++;
    }

    public void incrementBattlesLost() {
        battlesLost++;
    }

    public String getBattleStats() {
        return "Battles Won: " + battlesWon + ", Battles Lost: " + battlesLost;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int exp) {
        experience += exp;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getId() {
        return id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void heal() {
        health = maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
