package com.libgdx.game.main;

import com.badlogic.gdx.Game;
import com.libgdx.game.playState.PlayState;

public class Main extends Game {
	private GameStateManager gsm;

	public float wordlWidth = 22f;
	public float worldHeight = 6f;

	public void create () {
		gsm = new GameStateManager(this);
		gsm.push(new PlayState(gsm));
	}
}