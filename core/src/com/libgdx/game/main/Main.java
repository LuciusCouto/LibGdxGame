package com.libgdx.game.main;

import com.badlogic.gdx.Game;
import com.libgdx.game.playState.PlayState;

public class Main extends Game {
	private GameStateManager gsm;

	public final int wordlWidth = 1080;
	public final int worldHeight = 600;

	public void create () {
		gsm = new GameStateManager(this);
		gsm.push(new PlayState(gsm));
	}
}