package com.libgdx.game.playState.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.libgdx.game.playState.PlayState;

public class Player extends Entity {
    PlayState ps;
    private TextureAtlas atlas;
    private Animation<AtlasRegion> downAnimation;
    private Animation<AtlasRegion> topAnimation;
    private Animation<AtlasRegion> leftAnimation;
    private Animation<AtlasRegion> rightAnimation;
    private float stateTime;
    private TextureRegion currentFrame;
    public Body body;

    public Player(PlayState gp) {
        this.ps = gp;
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
        speed = 200f;
    }

    public void keyboardMovePlayer(float stateTime) {
        if (down && right) {
            if (!rightCollide && !downCollide) {
                x += (speed * Gdx.graphics.getDeltaTime()) / 1.41421356f;
                y -= (speed * Gdx.graphics.getDeltaTime()) / 1.41421356f;
            }
            currentFrame = rightAnimation.getKeyFrame(stateTime);
        } else if (down && left) {
            if (!leftCollide && !downCollide) {
                x -= (speed * Gdx.graphics.getDeltaTime()) / 1.41421356f;
                y -= (speed * Gdx.graphics.getDeltaTime()) / 1.41421356f;
            }
            currentFrame = leftAnimation.getKeyFrame(stateTime);
        } else if (top && right) {
            if (!rightCollide && !topCollide) {
                x += (speed * Gdx.graphics.getDeltaTime()) / 1.41421356f;
                y += (speed * Gdx.graphics.getDeltaTime()) / 1.41421356f;
            }
            currentFrame = rightAnimation.getKeyFrame(stateTime);
        } else if (top && left) {
            if (!leftCollide && !topCollide) {
                x -= (speed * Gdx.graphics.getDeltaTime()) / 1.41421356f;
                y += (speed * Gdx.graphics.getDeltaTime()) / 1.41421356f;
            }
            currentFrame = leftAnimation.getKeyFrame(stateTime);
        } else if (top) {
            if (!topCollide) {
                y += speed * Gdx.graphics.getDeltaTime();
            }
            currentFrame = topAnimation.getKeyFrame(stateTime);
        } else if (down) {
            if (!downCollide) {
                y -= speed * Gdx.graphics.getDeltaTime();
            }
            currentFrame = downAnimation.getKeyFrame(stateTime);
        } else if (left) {
            if (!leftCollide) {
                x -= speed * Gdx.graphics.getDeltaTime();
            }
            currentFrame = leftAnimation.getKeyFrame(stateTime);
        } else if (right) {
            if (!rightCollide) {
                x += speed * Gdx.graphics.getDeltaTime();
            }
            currentFrame = rightAnimation.getKeyFrame(stateTime);
        }

    }

    public void joystickMovePlayer(float stateTime) {
        if (ps.ui.mcUI.touchpad.getKnobPercentY() > 0 && Math.abs(ps.ui.mcUI.touchpad.getKnobPercentY()) >= Math.abs(ps.ui.mcUI.touchpad.getKnobPercentX())) {
            currentFrame = topAnimation.getKeyFrame(stateTime);
        }
        if (ps.ui.mcUI.touchpad.getKnobPercentY() < 0 && Math.abs(ps.ui.mcUI.touchpad.getKnobPercentY()) >= Math.abs(ps.ui.mcUI.touchpad.getKnobPercentX())) {
            currentFrame = downAnimation.getKeyFrame(stateTime);
        }
        if (ps.ui.mcUI.touchpad.getKnobPercentX() > 0 && Math.abs(ps.ui.mcUI.touchpad.getKnobPercentX()) >= Math.abs(ps.ui.mcUI.touchpad.getKnobPercentY())) {
            currentFrame = rightAnimation.getKeyFrame(stateTime);
        }
        if (ps.ui.mcUI.touchpad.getKnobPercentX() < 0 && Math.abs(ps.ui.mcUI.touchpad.getKnobPercentX()) >= Math.abs(ps.ui.mcUI.touchpad.getKnobPercentY())) {
            currentFrame = leftAnimation.getKeyFrame(stateTime);
        }

        x += ps.ui.mcUI.touchpad.getKnobPercentX() * speed * Gdx.graphics.getDeltaTime();
        y += ps.ui.mcUI.touchpad.getKnobPercentY() * speed * Gdx.graphics.getDeltaTime();
    }

    public void update() {
        stateTime += 0.25f * Gdx.graphics.getDeltaTime();

        if (ps.ui.mcUI.touchpad.isTouchFocusTarget() == false) {
            keyboardMovePlayer(stateTime);
        }
        if (!top && !down && !left && !right) {
            joystickMovePlayer(stateTime);
        }

        body.setTransform(x + width / 2, y + height / 2, 0);

        if (x < 0) {
            x = 0;
        } else if (x + width > ps.worldWidth) {
            x = ps.worldWidth - width;
        }
        if (y < 0) {
            y = 0;
        } else if (y + height > ps.worldHeight) {
            y = ps.worldHeight - height;
        }
    }

    public void render(SpriteBatch batch) {
        currentFrame.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        batch.draw(currentFrame, x, y, width, height);
    }

    public void dispose() {
        atlas.dispose();
    }
}