package com.libgdx.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.Stack;

public class GameStateManager {

    private Game game;
    private Stack<Screen> screens;

    public GameStateManager(Game game) {
        this.game = game;
        screens = new Stack<Screen>();
    }

    public void push(Screen screen) {
        screens.push(screen);
        game.setScreen(screen);
    }

    public void pop() {
        screens.pop();
        game.setScreen(screens.peek());
    }

    public void set(Screen screen) {
        screens.pop();
        screens.push(screen);
        game.setScreen(screen);
    }
}
