package com.thwamster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;

public class GameBoard {
    public static final int bomb = -1; // helps with readability

    private int[][] board; // the data structure as described above
    private int numBombs; // the number of bombs in the grid
    private int numFlags; // the number of flags that still have to be placed by the player
    private GameplayScreen gameScreen; // access to the GameplayScreen

    private Texture emptyTile;
    private Texture emptyFloorTile;
    private Texture oneTile;
    private Texture twoTile;
    private Texture threeTile;
    private Texture fourTile;
    private Texture fiveTile;
    private Texture sixTile;
    private Texture sevenTile;
    private Texture eightTile;
    private Texture bombTile;
    private Texture flagTile;

    public GameBoard(GameplayScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.board = new int[16][30];
        this.numBombs = 50;
        this.numFlags = this.numBombs;

        loadGraphics();
    }

    public GameBoard(GameplayScreen gameScreen, int newNumRows, int newNumCols, int newNumBombs) {
        this.gameScreen = gameScreen;
        this.board = new int[newNumRows][newNumCols];
        this.numBombs = newNumBombs;
        this.numFlags = this.numBombs;

        loadGraphics();
    }

    public int getNumCols() {
        return this.board[0].length;
    }
    public int getNumRows() {
        return this.board.length;
    }

    private void loadGraphics() {
        emptyTile = new Texture("emptyTile.jpg");
        emptyFloorTile= new Texture("empty floor.jpg");
        oneTile= new Texture("oneTile.jpg");
        twoTile= new Texture("twoTile.jpg");
        threeTile= new Texture("threeTile.jpg");
        fourTile= new Texture("fourTile.jpg");
        fiveTile= new Texture("fiveTile.jpg");
        sixTile= new Texture("sixTile.jpg");
        sevenTile= new Texture("sevenTile.jpg");
        eightTile= new Texture("eightTile.jpg");
        bombTile= new Texture("bomb.jpg");
        flagTile= new Texture("flagTile.jpg");
    }

    public void draw(SpriteBatch spriteBatch) {
        // temp
        spriteBatch.draw(emptyTile, 0, 0);
    }

    private Texture drawTile(int value) {
        if (value < 9) { return emptyTile; }
        else if (value == 9) { return bombTile; }
        else if (value == 10) { return emptyFloorTile; }
        else if (value == 11) { return oneTile; }
        else if (value == 12) { return twoTile; }
        else if (value == 13) { return threeTile; }
        else if (value == 14) { return fourTile; }
        else if (value == 15) { return fiveTile; }
        else if (value == 16) { return sixTile; }
        else if (value == 17) { return sevenTile; }
        else if (value == 18) { return eightTile; }
        else if (value <= 28) { return flagTile; }
        else { return emptyTile; }
    }

    private void placeBombs() {
        // temp
        board[0][0] = 11;
        board[0][1] = 12;
        board[0][2] = 13;
        board[0][3] = 14;
        board[0][4] = 15;
        board[0][5] = 16;
        board[0][6] = 17;
        board[0][7] = 18;
        board[0][8] = 9;
        board[0][9] = 10;
        board[0][10] = 19;
    }

    public Location getEmptyLocation() {
        ArrayList<Location> allEmpty = new ArrayList<Location>();

        for (int r = 0; r < getNumRows(); r++) {
            for (int c = 0; c < getNumCols(); c++) {
                if (this.board[r][c] == 0) {
                    allEmpty.add(new Location(r, c));
                }
            }
        }

        Collections.shuffle(allEmpty);

        if (!allEmpty.isEmpty()) { return allEmpty.get(0); }
        else { return new Location(-1, -1); }
    }
}
