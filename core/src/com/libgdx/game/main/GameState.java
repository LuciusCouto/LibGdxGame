package main;

import com.badlogic.gdx.Screen;

public abstract class GameState implements Screen {

    protected GameStateManager gsm;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        create();
    }

    public abstract void create();

    public abstract void render(float delta);


    public abstract void resize(int width, int height);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
