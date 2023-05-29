package com.libgdx.game.playState.tile;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;
import com.libgdx.game.playState.PlayState;

public class CollisionChecker {
    public CollisionChecker(final PlayState ps) {
        TiledMapTileLayer layer = (TiledMapTileLayer) ps.mapR.tiledMap.getLayers().get("bgd");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        int mapWidth = layer.getWidth();
        int mapHeight = layer.getHeight();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set((x + 0.5f) * ps.tileSize, (y + 0.5f) * ps.tileSize);

                    shape.setAsBox(0.5f * ps.tileSize, 0.5f * ps.tileSize);
                    fdef.shape = shape;
                    ps.world.createBody(bdef).createFixture(fdef).setUserData("bgd"); // Defina o UserData como "bgd" para identificar a colisão com os tiles do mapa
                }
            }
        }

        // Adicione um ContactListener ao mundo Box2D
        ps.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Verifique se um dos fixtures é o jogador
                if (fixtureA.getBody().getUserData() == ps.player || fixtureB.getBody().getUserData() == ps.player) {
                    // Verifique qual lado do jogador colidiu
                    WorldManifold manifold = contact.getWorldManifold();
                    if (manifold.getNormal().x > 0) {
                        ps.player.rightCollide = true;
                    }
                    if (manifold.getNormal().x < 0) {
                        ps.player.leftCollide = true;
                    }
                    if (manifold.getNormal().y > 0) {
                        ps.player.topCollide = true;
                    }
                    if (manifold.getNormal().y < 0) {
                        ps.player.downCollide = true;
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                ps.player.topCollide = false;
                ps.player.downCollide = false;
                ps.player.leftCollide = false;
                ps.player.rightCollide = false;
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }
}