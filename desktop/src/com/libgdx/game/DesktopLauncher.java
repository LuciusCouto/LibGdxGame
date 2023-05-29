package com.libgdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.libgdx.game.main.Main;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1080, 600);
		config.setForegroundFPS(60);
		config.useVsync(true);
		config.setWindowIcon("player/boy_down_1.png");
		config.setTitle("LibGdx Game");
		new Lwjgl3Application(new Main(), config);
	}
}
