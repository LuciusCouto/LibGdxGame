package com.libgdx.game.playState.entities;

public class Entity {
    public boolean top = false, down = false, left = false, right = false;
    public boolean topCollide = false, downCollide = false, leftCollide = false, rightCollide = false;
    public float width, height;
    float speed;
    public float x, y;
}
