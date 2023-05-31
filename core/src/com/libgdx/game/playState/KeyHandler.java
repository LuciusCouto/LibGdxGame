package com.libgdx.game.playState;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyHandler implements InputProcessor {

    private boolean f3Pressed = false;
    private PlayState ps;

    public KeyHandler(PlayState ps) {
        this.ps = ps;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (ps.currentState == ps.playState) {
            if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
                ps.player.top = true;
            }
            if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
                ps.player.down = true;
            }
            if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
                ps.player.right = true;
            }
            if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
                ps.player.left = true;
            }

            if (keycode == Input.Keys.F3) {
                f3Pressed = true;
            } else if (f3Pressed && keycode == Input.Keys.B) {
                ps.ui.dUI.showCollisionBox = !ps.ui.dUI.showCollisionBox;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (ps.currentState == ps.playState) {
            if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
                ps.player.top = false;
            }
            if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
                ps.player.down = false;
            }
            if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
                ps.player.right = false;
            }
            if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
                ps.player.left = false;
            }

            if (keycode == Input.Keys.F3) {
                if (!ps.ui.dUI.showCollisionBox) {
                    ps.ui.dUI.showDebug = !ps.ui.dUI.showDebug;
                }
                f3Pressed = false;
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (ps.currentState == ps.playState) {

            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}