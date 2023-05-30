package com.libgdx.game.playState.tile;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.libgdx.game.playState.PlayState;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;
import com.libgdx.game.playState.PlayState;
import com.libgdx.game.playState.entities.Player;

public class CollisionChecker {
    private PlayState ps;
    private TiledMapTileLayer layer;

    public CollisionChecker(PlayState ps) {
        this.ps = ps;
        this.layer = (TiledMapTileLayer) ps.mapR.tiledMap.getLayers().get("bgd");
        createBorderMapCollision();
        createCollisionBodies();
        setupContactListener();
    }

    private void createBorderMapCollision() {
        // Crie um objeto BodyDef para a borda
        BodyDef edgeBodyDef = new BodyDef();
        edgeBodyDef.type = BodyDef.BodyType.StaticBody; // Corpo estático, não sofre influência das forças

// Crie uma instância do corpo de borda
        Body edgeBody = ps.world.createBody(edgeBodyDef);

// Crie uma forma de borda (EdgeShape) para cada segmento de borda
        EdgeShape edgeShape = new EdgeShape();

// Crie as bordas superiores e inferiores
        edgeShape.set(new Vector2(0, 0), new Vector2(ps.worldWidth, 0)); // Borda superior
        edgeBody.createFixture(edgeShape, 0); // Adicione a borda superior ao corpo de borda
        edgeShape.set(new Vector2(0, ps.worldHeight), new Vector2(ps.worldWidth, ps.worldHeight)); // Borda inferior
        edgeBody.createFixture(edgeShape, 0); // Adicione a borda inferior ao corpo de borda

// Crie as bordas laterais
        edgeShape.set(new Vector2(0, 0), new Vector2(0, ps.worldHeight)); // Borda esquerda
        edgeBody.createFixture(edgeShape, 0); // Adicione a borda esquerda ao corpo de borda
        edgeShape.set(new Vector2(ps.worldWidth, 0), new Vector2(ps.worldWidth, ps.worldHeight)); // Borda direita
        edgeBody.createFixture(edgeShape, 0); // Adicione a borda direita ao corpo de borda

// Libere os recursos da forma de borda
        edgeShape.dispose();
    }

    private void createCollisionBodies() {
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
                    ps.world.createBody(bdef).createFixture(fdef).setUserData("bgd");
                }
            }
        }

        shape.dispose();
    }

    private void setupContactListener() {
        ps.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Player player = ps.player;

                if (fixtureA.getBody().getUserData() == player || fixtureB.getBody().getUserData() == player) {
                    WorldManifold manifold = contact.getWorldManifold();
                    float normalX = manifold.getNormal().x;
                    float normalY = manifold.getNormal().y;

                    if (normalX > 0) {
                        player.setRightCollide(true);
                    } else if (normalX < 0) {
                        player.setLeftCollide(true);
                    }

                    if (normalY > 0) {
                        player.setTopCollide(true);
                    } else if (normalY < 0) {
                        player.setDownCollide(true);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                ps.player.setCollide(false);
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