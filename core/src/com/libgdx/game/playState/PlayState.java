package com.libgdx.game.playState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.game.playState.entities.Player;
import com.libgdx.game.playState.tile.CollisionChecker;
import com.libgdx.game.playState.tile.MapRender;
import com.libgdx.game.playState.ui.UI;

import main.GameState;
import main.GameStateManager;
import main.Main;

public class PlayState extends GameState {

	GameStateManager gsm;
	Main main;
	public World world;
	private Box2DDebugRenderer debugRenderer;
	KeyHandler keyH;
	public Player player;
	public MapRender mapR;
	public UI ui;
    public OrthographicCamera camera;
	Viewport viewport;
	SpriteBatch worldBatch;
	public int worldX;
	public int worldY;
	public int tileSize;
	public float worldWidth;
	public float worldHeight;
	public int playState = 0;
	public int currentState = playState;
	CollisionChecker collisionChecker;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
        collisionChecker = new CollisionChecker(this);
	}

	@Override
	public void create() {
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		main = new Main();
		tileSize = 48;
		worldWidth = main.wordlWidth;
		worldHeight = main.worldHeight;
		camera = new OrthographicCamera(worldWidth, worldHeight);
		camera.update();
		viewport = new ExtendViewport(worldWidth, worldHeight, camera);
		viewport.apply();

		worldBatch = new SpriteBatch();

		mapR = new MapRender(this);

		worldWidth = mapR.tiledMap.getProperties().get("width", Integer.class) * 48f;
		worldHeight = mapR.tiledMap.getProperties().get("height", Integer.class) * 48f;


		player = new Player(this);
		ui = new UI(this, worldBatch);

		keyH = new KeyHandler(this);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(ui.mcUI.stage); // Adicione o stage do MobileControlUI
		inputMultiplexer.addProcessor(keyH); // Adicione o KeyHandler

		// Defina o InputMultiplexer como processador de entrada
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public void show() {

	}

	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(1 / 60f, 6, 2);
		// Update
		player.update();
		camera.position.set(
				MathUtils.clamp(player.x + player.width / 2, camera.viewportWidth / 2 * camera.zoom, worldWidth - camera.viewportWidth / 2 * camera.zoom),
				MathUtils.clamp(player.y + player.height / 2, camera.viewportHeight / 2 * camera.zoom, worldHeight - camera.viewportHeight / 2 * camera.zoom),
				0
		);
		camera.update();


		worldBatch.setProjectionMatrix(camera.combined);

		worldX = (int) (player.x);
		worldY = (int) (player.y);

		checkFullScreen();

		// Render
        mapR.render(camera);
        worldBatch.begin();
        player.render(worldBatch);
        worldBatch.end();
		ui.render();

		debugRenderer.render(world, camera.combined);
	}

	public void resize(int width, int height) {

		viewport.update(width, height, true);
		viewport.apply();
		camera.update();
		ui.resize(width, height);
	}

	public void pause() {

	}

	public void resume() {

	}

	public void hide() {

	}

	public void dispose() {
		mapR.dispose();
		player.dispose();
		worldBatch.dispose();
		mapR.dispose();
		ui.dispose();
	}

	public void checkFullScreen() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setWindowedMode(1080, 600);
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			}
		}
	}
}
