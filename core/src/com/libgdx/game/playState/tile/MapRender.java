package com.libgdx.game.playState.tile;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.libgdx.game.playState.PlayState;

public class MapRender {

    PlayState ps;
    public TiledMap tiledMap;
    public OrthogonalTiledMapRenderer renderer;

    public MapRender(PlayState ps) {
        this.ps = ps;
        tiledMap = new TmxMapLoader().load("maps/map01.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, ps.tileSize /16f);

    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void dispose() {
        renderer.dispose();
        tiledMap.dispose();
    }
}

