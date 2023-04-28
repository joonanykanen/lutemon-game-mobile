# Lutemon Game Mobile

Lutemon Game Mobile is a mobile game app for Android that allows users to create, train, and battle with creatures called Lutemons. Players can create Lutemons with unique attributes, train them to improve their abilities, and participate in battles against other Lutemons.

![Game Feature Image](https://raw.githubusercontent.com/joonanykanen/lutemon-game-mobile/main/lutemon_battle_showcase_28-04-2023.gif)

## Table of Contents

- [Features](#features)
- [Classes](#classes)
- [Installation](#installation)
- [License](#license)

## Features

- Create Lutemons with unique names and colors
- Train Lutemons to improve their stats
- Battle with Lutemons to test their strength and strategy
- Manage and view Lutemons in an organized list

![Game Screenshots](https://raw.githubusercontent.com/joonanykanen/lutemon-game-mobile/main/lutemon_game_features_28-04-2023.png)

## Classes

- [MainActivity](#MainActivity)
- [HomeFragment](#HomeFragment)
- [TrainingFragment](#TrainingFragment)
- [BattleFragment](#BattleFragment)
- [Lutemon](#Lutemon)
- [LutemonAdapter](#LutemonAdapter)
- [Storage](#Storage)

![Class Diagram](https://raw.githubusercontent.com/joonanykanen/lutemon-game-mobile/main/lutemon_game_class_diagram_28-04-2023.png)

### MainActivity

The `MainActivity` class represents the main activity of the game and is responsible for managing the navigation between Home, Training, and Battle fragments.

### HomeFragment

The `HomeFragment` class is responsible for displaying and managing a list of Lutemons. Users can create Lutemons by entering a name and selecting a color. The created Lutemons are displayed in a RecyclerView as a list of items, each with an image and stats.

### TrainingFragment

The `TrainingFragment` class provides an interface for training Lutemons in the game. Users can select a Lutemon from a Spinner, and its image and stats will be displayed. They can train the selected Lutemon by pressing the "Train Lutemon" button.

### BattleFragment

The `BattleFragment` class manages the UI and logic of the battle screen. Users can battle two Lutemons against each other, which are selected from two Spinners. The battle begins when users click the "Start Battle" button, and the progress is displayed through a TextView that shows battle log messages.

### Lutemon

The `Lutemon` class represents a creature in the game, which has attributes like name, color, attack, defense, experience, health, and more. The class contains methods for managing experience, health, and other statistics, as well as implementing the main battle mechanics.

### LutemonAdapter

The `LutemonAdapter` class manages and displays a collection of Lutemon objects within a RecyclerView in the HomeFragment.

### Storage

The `Storage` class serves as a storage system for Lutemons in the game. It provides methods for saving, loading, and managing Lutemons using Android's SharedPreferences and Google's Gson library.

## Installation

1. Clone the repository:

    ```git clone https://github.com/joonanykanen/lutemon-game-mobile.git```

2. Open the project in Android Studio and build the app.

3. Run the app on an emulator or a physical device.

## License

This project is licensed under the [MIT License](LICENSE.md).
