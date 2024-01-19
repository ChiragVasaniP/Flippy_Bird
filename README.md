# Flippy Bird Game Development

## Overview

This Android game project implements a simple Flappy Bird-like game using the Model-View-ViewModel (MVM) architecture with Kotlin and the Android framework. The game features a bird that can be controlled by the user's touch input, flying through pipes and avoiding collisions.

## Features

- Bird animation with multiple frames.
- User interaction: Tap to make the bird fly.
- Pipes moving across the screen.
- Collision detection between the bird and pipes.
- Game over condition with the ability to reset the game.

## Code Structure

### GameViewModel.kt

This Kotlin class extends `ViewModel` and handles the game logic. Here are some key functionalities:

- **Bird Animation:** The bird has multiple frames, and the ViewModel manages the animation.
- **User Input:** Detects user touch to control the bird's movement.
- **Pipes:** Manages the movement of pipes across the screen.
- **Collision Detection:** Checks for collisions between the bird and pipes.
- **Game Loop:** Uses coroutines for the main game loop.

### MainActivity.kt

The main activity class sets up the game environment, including UI components and touch input handling. Some key points:

- **Observing ViewModel:** Listens to changes in the ViewModel's LiveData.
- **Screen Dimensions:** Retrieves screen dimensions for boundary checks.
- **Touch Input:** Handles touch events to control the bird.

## Usage

1. Clone the repository.

   ```bash
   git clone https://github.com/ChiragVasaniP/Flippy_Bird.git
