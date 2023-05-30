package com.libgdx.game.playState.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DebugUI {

    UI ui;
    Stage stage;
    Label framesPerSecond;
    Label deltaTime;
    Label worldX;
    Label worldY;
    Label col;
    Label row;
    Table table;

    public boolean showDebug = false;

    public DebugUI(Viewport viewport, SpriteBatch sb, Label.LabelStyle style, UI ui) {
        this.ui = ui;
        stage = new Stage(viewport, sb);
        table = new Table();
        table.top();
        table.left();
        table.padTop(10f);
        table.padLeft(10f);
        table.setFillParent(true);

        framesPerSecond = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), style);
        deltaTime = new Label("Tempo Delta: " + Gdx.graphics.getDeltaTime(), style);
        worldX = new Label("Mundo X: " + ui.ps.worldX, style);
        worldY = new Label("Mundo Y: " + ui.ps.worldY, style);
        col = new Label("Coluna: " + (int) (ui.ps.worldX / ui.ps.tileSize), style);
        row = new Label("Fileira: " + (int) (ui.ps.worldY / ui.ps.tileSize), style);

        table.add(framesPerSecond).left();
        table.row();
        table.add(deltaTime).left();
        table.row();
        table.add(worldX).left();
        table.row();
        table.add(worldY).left();
        table.row();
        table.add(col).left();
        table.row();
        table.add(row).left();

        stage.addActor(table);
    }

    public void render() {
        framesPerSecond.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        deltaTime.setText("Tempo Delta: " + Gdx.graphics.getDeltaTime());
        worldX.setText("Mundo X: " + ui.ps.worldX);
        worldY.setText("Mundo Y: " + ui.ps.worldY);
        col.setText("Coluna: " + (int) (ui.ps.worldX / ui.ps.tileSize));
        row.setText("Coluna: " + (int) (ui.ps.worldY / ui.ps.tileSize));
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
