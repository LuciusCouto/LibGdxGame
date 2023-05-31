package com.libgdx.game.playState.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.libgdx.game.playState.PlayState;

public class Player extends Entity {
    private PlayState ps;
    private TextureAtlas atlas;
    private Animation<AtlasRegion> downAnimation;
    private Animation<AtlasRegion> topAnimation;
    private Animation<AtlasRegion> leftAnimation;
    private Animation<AtlasRegion> rightAnimation;
    private float stateTime;
    private TextureRegion currentFrame;
    private Body body;

    public Player(PlayState ps) {
        this.ps = ps;
        setDefaultValues();

        atlas = new TextureAtlas(Gdx.files.internal("player/player.atlas"));
        downAnimation = new Animation<>(1/10f, atlas.findRegions("boy_down"), Animation.PlayMode.LOOP);
        topAnimation = new Animation<>(1/10f, atlas.findRegions("boy_up"), Animation.PlayMode.LOOP);
        leftAnimation = new Animation<>(1/10f, atlas.findRegions("boy_left"), Animation.PlayMode.LOOP);
        rightAnimation = new Animation<>(1/10f, atlas.findRegions("boy_right"), Animation.PlayMode.LOOP);
        stateTime = 0f;
        currentFrame = downAnimation.getKeyFrame(stateTime);

        // Crie o corpo do jogador no Box2D
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x + width / 2, y + height / 2);
        body = ps.world.createBody(bodyDef);

        // Defina o UserData do corpo como o objeto do jogador
        body.setUserData(this);

        // Crie a forma do corpo do jogador
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        // Crie a definição do fixture do corpo do jogador
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // Crie o fixture do corpo do jogador
        body.createFixture(fixtureDef);

        // Libere a forma do corpo
        shape.dispose();
    }

    public void setDefaultValues() {
        width = ps.tileSize;
        height = ps.tileSize;
        x = 10f * ps.tileSize;
        y = 10f * ps.tileSize;
        speed = 4f * ps.tileSize;
    }

    public void movePlayer(float stateTime, Vector2 direction) {
        float speedModifier = (direction.x != 0 && direction.y != 0) ? 0.7071f : 1.0f; // Adjust speed for diagonal movement

        // Normalizar o vetor de direção
        if (direction.len2() > 1.0f) {
            direction.nor();
        }

        float deltaX = direction.x * speed * speedModifier * Gdx.graphics.getDeltaTime();
        float deltaY = direction.y * speed * speedModifier * Gdx.graphics.getDeltaTime();

        // Verificar as colisões nas direções vertical e horizontal
        if (deltaY > 0 && !topCollide) {
            body.setLinearVelocity(body.getLinearVelocity().x, speed * speedModifier);
        } else if (deltaY < 0 && !downCollide) {
            body.setLinearVelocity(body.getLinearVelocity().x, -speed * speedModifier);
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
        }

        if (deltaX > 0 && !rightCollide) {
            body.setLinearVelocity(speed * speedModifier, body.getLinearVelocity().y);
        } else if (deltaX < 0 && !leftCollide) {
            body.setLinearVelocity(-speed * speedModifier, body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }


        // Atualizar o frame com base na direção do movimento
        if (deltaY > 0 && !topCollide) {
            currentFrame = topAnimation.getKeyFrame(stateTime);
        } else if (deltaY < 0 && !downCollide) {
            currentFrame = downAnimation.getKeyFrame(stateTime);
        } if (deltaX > 0 && !rightCollide) {
            currentFrame = rightAnimation.getKeyFrame(stateTime);
        } else if (deltaX < 0 && !leftCollide) {
            currentFrame = leftAnimation.getKeyFrame(stateTime);
        }

        // Atualizar a posição do jogador com base no corpo
        x = body.getPosition().x - width / 2;
        y = body.getPosition().y - height / 2;
    }


    public void update() {
        stateTime += 0.25f * Gdx.graphics.getDeltaTime();

        if (ps.ui.mcUI.touchpad.isTouchFocusTarget()) {
            Vector2 direction = new Vector2(ps.ui.mcUI.touchpad.getKnobPercentX(), ps.ui.mcUI.touchpad.getKnobPercentY());
            movePlayer(stateTime, direction);
        } else {
            Vector2 direction = new Vector2(0, 0);
            if (left) {
                direction.x = -1;
            } else if (right) {
                direction.x = 1;
            }
            if (down) {
                direction.y = -1;
            } else if (top) {
                direction.y = 1;
            }
            movePlayer(stateTime, direction);
        }
    }

    // Métodos de colisão
    public void setRightCollide(boolean collide) {
        rightCollide = collide;
        if (collide) {
            // Lógica específica para colisão à direita
            // Por exemplo, diminuir a velocidade do jogador quando andar na diagonal
            if (topCollide || downCollide) {
                body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * 0.5f);
            }
        }
    }

    public void setLeftCollide(boolean collide) {
        leftCollide = collide;
        if (collide) {
            // Lógica específica para colisão à esquerda
            // Por exemplo, diminuir a velocidade do jogador quando andar na diagonal
            if (topCollide || downCollide) {
                body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * 0.5f);
            }
        }
    }

    public void setTopCollide(boolean collide) {
        topCollide = collide;
        if (collide) {
            // Lógica específica para colisão superior
        }
    }

    public void setDownCollide(boolean collide) {
        downCollide = collide;
        if (collide) {
            // Lógica específica para colisão inferior
        }
    }

    public void setCollide(boolean collide) {
        topCollide = collide;
        downCollide = collide;
        leftCollide = collide;
        rightCollide = collide;
    }

    public void render(SpriteBatch batch) {
        currentFrame.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        batch.draw(currentFrame, x, y, width, height);
    }

    public void dispose() {
        atlas.dispose();
    }
}