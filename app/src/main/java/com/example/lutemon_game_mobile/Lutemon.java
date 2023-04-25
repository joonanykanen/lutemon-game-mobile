package com.example.lutemon_game_mobile;

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

    private static int idCounter = 0;

    public Lutemon(String name, int attack, int defense, int maxHealth, int imageResource) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.experience = 0;
        this.imageResource = imageResource;
        this.id = idCounter++;
    }

    public int attack() {
        return attack + experience * 2;
    }

    public void defense(Lutemon attacker) {
        int damage = attacker.attack() - defense;
        if (damage > 0) {
            health -= damage;
        }
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
