package com.thwamster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;

public class GameplayScreen implements Screen {
    /* Display Characteristics */
    private static final float worldWidth = 1280;
    private static final float worldHeight = 728;

    /* Objects */
    private SpriteBatch spriteBatch; // object that allows us to draw all our graphics
    private ShapeRenderer shapeRenderer; // object that allows us to draw shapes
    private Camera camera; // camera to view our virtual world
    private Viewport viewport; // control how the camera views the world, zooms and scales.
    private BitmapFont font; // custom font

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
        this.board = new GameBoard();
        this.board = new GameBoard();

        // fonts
        FreeTypeFontGenerator importFont = new FreeTypeFontGenerator(Gdx.files.internal("mine-sweeper.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;

        this.font = importFont.generateFont(parameter);
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

        int gameStatus = this.board.getGameStatus();
        if (gameStatus == 0 || gameStatus == 1) {
            checkMouseInput();
        }
        else {
            checkKeyboardInput();
        }
        draw(gameStatus);
    }

    public void draw(int gameStatus) {
        float margin = 80;
        float gameWidth = GameplayScreen.getWorldWidth() - 2 * margin;
        float gameHeight = GameplayScreen.getWorldHeight() - 2 * margin;

        float scale = Math.min(gameWidth / this.board.getNumCols(), gameHeight / this.board.getNumRows());
        float xIndent = (gameWidth - (scale * this.board.getNumCols())) / 2;
        float yIndent = (gameHeight - (scale * this.board.getNumRows())) / 2;

        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(0.5F, 0.5F, 0.5F, 0);
        this.shapeRenderer.rect(xIndent, yIndent, GameplayScreen.reverse2xWidth(xIndent), GameplayScreen.reverse2xHeight(yIndent));
        this.shapeRenderer.end();

        this.spriteBatch.begin();
        for (int r = 0; r < this.board.getNumRows(); r++) {
            for (int c = 0; c < this.board.getNumCols(); c++) {
                this.spriteBatch.draw(this.board.drawTile(this.board.getBoard()[r][c]), c * scale + xIndent + margin, r * scale + yIndent + margin, scale, scale);
            }
        }
        this.font.draw(this.spriteBatch, "Flags Remaining: " + (this.board.getNumBombs() - this.board.getNumFlags()), xIndent + 80, reverseHeight(yIndent + 40));
        this.font.draw(this.spriteBatch, "Time Elapsed:  " + (this.board.getTime()), reverseWidth(xIndent + 250), reverseHeight(yIndent + 40));

        ArrayList<Long> scoreboard = this.board.getScoreBoard();
        if (scoreboard != null && !scoreboard.isEmpty()) {
            Collections.sort(scoreboard);

            this.font.draw(this.spriteBatch, "Scores: ", xIndent + 7, reverseHeight(110));
            for (int i = 0; i < scoreboard.size() && i < 24; i++) {
                this.font.draw(this.spriteBatch, (i + 1) + ". " + this.board.formatTime(scoreboard.get(i)), xIndent + 7, reverseHeight(110 + (i + 1) * 20));
            }
        }

        if (gameStatus == -1) {
            this.font.draw(this.spriteBatch, "Game over. Loss.", xIndent + 80, yIndent + 40);
            this.font.draw(this.spriteBatch, "R to restart.", reverseWidth(xIndent + 195), yIndent + 40);
        }
        else if (gameStatus == 0) {
            this.font.draw(this.spriteBatch, "Left mouse to start.", reverseWidth(xIndent + 265), yIndent + 40);
        }
        else if (gameStatus == 2) {
            this.font.draw(this.spriteBatch, "Game over. Victory.", xIndent + 80, yIndent + 40);
            this.font.draw(this.spriteBatch, "R to restart.", reverseWidth(xIndent + 195), yIndent + 40);
        }

        this.spriteBatch.end();
    }

    public void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    // Inputs
    private void checkMouseInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            this.board.leftMouse(Gdx.input.getX(), Gdx.input.getY());
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            this.board.rightMouse(Gdx.input.getX(), Gdx.input.getY());
        }
    }

    private void checkKeyboardInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            this.board.reset();
        }
    }

    /* Camera Methods */
    @Override
    public void resize(int width, int height) { this.viewport.update(width,height); }
    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }
    @Override
    public void dispose() {
        this.spriteBatch.dispose();
        this.shapeRenderer.dispose();
        this.font.dispose();
    }

    /* Helper Methods */
    public static float reverseWidth(float x) { return worldWidth - x; }
    public static float reverseHeight(float y) { return worldHeight - y; }
    public static float reverse2xWidth(float x) { return worldWidth - x * 2; }
    public static float reverse2xHeight(float y) { return worldHeight - y * 2; }
    public static float getWorldWidth() { return worldWidth; }
    public static float getWorldHeight() { return worldHeight; }
}
