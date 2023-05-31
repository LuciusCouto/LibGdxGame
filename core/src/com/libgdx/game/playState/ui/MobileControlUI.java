package com.libgdx.game.playState.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.game.playState.PlayState;

public class MobileControlUI {
    PlayState ps;
    UI ui;
    public Touchpad touchpad;
    TextureAtlas atlas;
    public Stage stage;
    Vector2 p;
    public Rectangle b;
    Skin skin;
    ImageButton debugButton;
    public Table table;

    public MobileControlUI(Viewport viewport, SpriteBatch sb, final PlayState ps, UI ui) {
        this.ui = ui;
        this.ps = ps;
        this.stage = new Stage(viewport, sb);

        atlas = new TextureAtlas(Gdx.files.internal("ui/mobileControls.atlas"));
        skin = new Skin(Gdx.files.internal("ui/mobileControls.json"), atlas);
        table = new Table();
        table.setFillParent(true);

        debugButton = new ImageButton(skin);
        table.add(debugButton).size(50, 50).top().padTop(10).center();

        table.row();

        touchpad = new Touchpad(20, skin);
        table.add(touchpad).size(200).bottom().padBottom(150).left().padLeft(150);

        p = new Vector2();
        b = new Rectangle();
        //if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            stage.addListener(new InputListener() {

                Vector2 p = new Vector2();
                Rectangle b = new Rectangle();

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (event.getTarget() != touchpad && x < ps.worldWidth / 2 && y < ps.worldHeight / 1.3) {
                        // If we didn't actually touch the touchpad, set position to our touch point
                        b.set(touchpad.getX(), touchpad.getY(), touchpad.getWidth(), touchpad.getHeight());
                        b.setCenter(x, y);
                        touchpad.setBounds(b.x, b.y, b.width, b.height);

                        // Let the touchpad know to start tracking touch
                        touchpad.fire(event);
                    }
                    return true;
                }


                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    // Put the touchpad back to its original position
                    touchpad.clearActions();
                    touchpad.addAction(Actions.moveTo(150, 150, 0.15f));
                }
            });
        //}

        debugButton.addListener(new InputListener() {
            private boolean isButtonPressed = false;
            private int x;
            private int y;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Verifica se o botão foi pressionado
                if (debugButton.isPressed()) {
                    isButtonPressed = true;
                }

                x = x;

                y = y;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Se o botão estava sendo pressionado e o toque ocorreu no lado direito da tela e abaixo do botão
                if (isButtonPressed && this.x >= table.getWidth() / 2 && this.y >= debugButton.getY() + 10) {
                    // Ativa a função desejada
                    ui.dUI.showCollisionBox = !ui.dUI.showCollisionBox;
                } else {
                    ui.dUI.showDebug = !ui.dUI.showDebug;
                }

                // Redefine as variáveis para os próximos toques
                isButtonPressed = false;
            }
        });


        stage.addActor(table);
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose () {
        stage.dispose();
        atlas.dispose();
        skin.dispose();

    }
}