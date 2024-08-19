package com.thwamster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

public class GameplayScreen implements Screen {

    /* Display Characteristics */
    private static final float worldWidth = 1280;
    private static final float worldHeight = 728;

    /* Objects */
    private SpriteBatch spriteBatch; // object that allows us to draw all our graphics
    private ShapeRenderer shapeRenderer; // object that allows us to draw shapes
    private Camera camera; // camera to view our virtual world
    private Viewport viewport; // control how the camera views the world, zooms and scales.

    /* Game Mechanics */
    private GameBoard board;

    /* Runs one time, at the very beginning. All setup should happen here. */
    @Override
    public void show() {
        this.camera = new OrthographicCamera(); // 2D camera
        this.camera.position.set(worldWidth / 2, worldHeight / 2,0); // move the camera
        this.camera.update(); // required to save and update the camera to the changes above

        // freeze my view2, to 1280 x 720, no matter the window size
        this.viewport = new FitViewport(worldWidth, worldHeight, this.camera);

        // empty instantiation of objects that will draw graphics for us
        this.spriteBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true); // solution to an annoying problem

        // game mechanics
        this.board = new GameBoard(this);
    }

    public void clearScreen() {
        // screen --> white
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /*
     * This method runs as fast as it can, repeatedly, constantly looped.
     * 1. Process user input
     * 2. AI
     * 3. Draw all graphics
     */
    @Override
    public void render(float delta) {
        clearScreen();

        // all drawing of shapes between this
        this.shapeRenderer.begin();
        this.shapeRenderer.end();

        // all drawing of graphics between this
        this.spriteBatch.begin();
        board.draw(this.spriteBatch);
        this.spriteBatch.end();
    }
    @Override
    public void resize(int width, int height) { viewport.update(width,height); }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.spriteBatch.dispose();
        this.shapeRenderer.dispose();
    }

    /* Get Methods */
    public static float reverseWidth(int x) { return worldWidth - x; }
    public static float reverseHeight(int y) { return worldHeight - y; }
    public static float getWorldWidth() { return worldWidth; }
    public static float getWorldHeight() { return worldHeight; }
}
