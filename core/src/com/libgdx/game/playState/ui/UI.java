package com.libgdx.game.playState.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.game.playState.PlayState;

public class UI {

    PlayState ps;
    Stage stage;
    Viewport viewport;

    Label label;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    BitmapFont font;
    public DebugUI dUI;
    public MobileControlUI mcUI;
    SpriteBatch sb;

    public UI(PlayState ps, SpriteBatch sb) {
        this.ps = ps;
        this.sb = sb;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/MaruMonica.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        font = generator.generateFont(parameter);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        viewport = new ExtendViewport(1080, 600, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        dUI = new DebugUI(viewport, sb, style, this);
        mcUI = new MobileControlUI(viewport, sb, ps);
    }

    public void render() {
        sb.setProjectionMatrix(stage.getCamera().combined);
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            mcUI.render();
        }
        if (dUI.showDebug == true) {
            dUI.render();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        dUI.resize(width, height);
        mcUI.resize(width, height);
    }

    public void dispose() {
        stage.dispose();
        generator.dispose();
        font.dispose();
        mcUI.dispose();
        dUI.dispose();
    }
}
