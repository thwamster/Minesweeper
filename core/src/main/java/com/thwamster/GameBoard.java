package com.thwamster;

import com.badlogic.gdx.graphics.Texture;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameBoard {
    /* Instance Variables */
    // Elements
    private int[][] board; // the data structure as described above
    private int numBombs; // the number of bombs in the grid
    private int numFlags; // the number of flags that still have to be placed by the player
    private int gameStatus; // status of game ending
    private long startTime; // beginning timer
    private long finishTime; // end timer
    private ArrayList<Long> scoreBoard;

    // Textures
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

    /* Constructors */
<<<<<<< Updated upstream
    public GameBoard() {
        this.initialize(8, 8, 1);
        this.scoreBoard = new ArrayList<Long>();
        // this.setDifficulty(0);
=======
    public GameBoard(GameplayScreen gameScreen) {
        this(gameScreen, 30, 50, 50);
    }

    public GameBoard(GameplayScreen gameScreen, int newNumRows, int newNumCols, int newNumBombs) {
        this.gameScreen = gameScreen;
        this.board = new int[newNumRows][newNumCols];
        this.numBombs = newNumBombs;
        this.reset();

        this.loadGraphics();
>>>>>>> Stashed changes
    }

    /* Helper Methods */
    // Get Methods
    public int[][] getBoard() { return this.board; }
    public int getNumCols() { return this.board[0].length; }
    public int getNumRows() { return this.board.length; }
    public int getNumBombs() { return this.numBombs; }
    public int getNumFlags() { return this.numFlags; }
    public ArrayList<Long> getScoreBoard() { return this.scoreBoard; }

    public String getTime() {
        long time;
        if (this.gameStatus == -1 || this.gameStatus == 2) {
            time = this.finishTime;
        }
        else if (this.gameStatus == 1) {
            time = System.currentTimeMillis() - this.startTime;
        }
        else {
            time = 0;
        }
        return this.formatTime(time);
    }

    public String formatTime(Long time) {
        int minutes = (int) (time / (60 * 1000));
        int seconds = (int) ((time / 1000) % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

    public int getGameStatus() {
        if (this.numFlags == this.numBombs) {
            boolean finish = true;

            outerloop:
            for (int r = 0; r < this.getNumRows(); r++) {
                for (int c = 0; c < this.getNumCols(); c++) {
                    int value = this.board[r][c];
                    if (value >= 0 && value <= 8) {
                        finish = false;
                        break outerloop;
                    }
                    else if (value >= 19 && value <= 28) {
                        value -= 20;
                        if (value != -1) {
                            finish = false;
                            break outerloop;
                        }
                    }
                }
            }
            if (finish) {
                this.finish(2);
            }
        }
        return this.gameStatus;
    }

    // Other Methods
    public Location positionToLocation(float x, float y) {
        float margin = 80;
        float gameWidth = GameplayScreen.getWorldWidth() - 2 * margin;
        float gameHeight = GameplayScreen.getWorldHeight() - 2 * margin;

        float scale = Math.min(gameWidth / this.getNumCols(), gameHeight / this.getNumRows());
        float xIndent = (gameWidth - (scale * this.getNumCols())) / 2;
        float yIndent = (gameHeight - (scale * this.getNumRows())) / 2;

        return new Location((int) ((GameplayScreen.reverseHeight(y) - yIndent - margin) / scale),
                (int) ((x - xIndent - margin) / scale));
    }

    public boolean validLocation(Location location) {
        return location != null && location.getRow() >= 0 && location.getRow() < this.getNumRows() &&
                location.getCol() >= 0 && location.getCol() < this.getNumCols();
    }

    /* Initialization Methods */
    public void initialize(int newNumRows, int newNumCols, int newNumBombs) {
        this.board = new int[newNumRows][newNumCols];
        this.numBombs = newNumBombs;
        this.reset();
        this.loadGraphics();
    }

    public void setDifficulty(int difficulty) {
        switch (difficulty) {
            case 1:
                this.initialize(8, 8, 10);
                break;
            case 3:
                this.initialize(16, 30, 99);
                break;
            default:
                this.initialize(16, 16, 40);
                break;
        }
    }

    public void start(Location startLocation) {
        this.gameStatus = 1;
        this.startTime = System.currentTimeMillis();

        this.placeBombs(startLocation);
        this.numberBoard();
    }

    public void finish(int finishStatus) {
        this.finishTime = System.currentTimeMillis() - this.startTime;
        if (gameStatus == 1 && finishStatus == 2) {
            this.scoreBoard.add(this.finishTime);
        }
        this.gameStatus = finishStatus;
    }

    public void reset() {
        for (int[] values : this.board) {
            Arrays.fill(values, 0);
        }
        this.numFlags = 0;
        this.gameStatus = 0;
    }

    // Textures
    private void loadGraphics() {
        this.emptyTile = new Texture("emptyTile.jpg");
        this.emptyFloorTile = new Texture("empty floor.jpg");
        this.oneTile = new Texture("oneTile.jpg");
        this.twoTile = new Texture("twoTile.jpg");
        this.threeTile = new Texture("threeTile.jpg");
        this.fourTile = new Texture("fourTile.jpg");
        this.fiveTile = new Texture("fiveTile.jpg");
        this.sixTile = new Texture("sixTile.jpg");
        this.sevenTile = new Texture("sevenTile.jpg");
        this.eightTile = new Texture("eightTile.jpg");
        this.bombTile = new Texture("bomb.jpg");
        this.flagTile = new Texture("flagTile.jpg");
    }

    // Bombs
    private void placeBombs(Location startLocation) {
        int count = 0;
        for (int i = 0; i < this.numBombs; i++) {
            Location location = this.getEmptyLocation(startLocation);
            if (this.validLocation(location)) {
                this.board[location.getRow()][location.getCol()] = -1;
                count++;
            }
        }
        this.numBombs = count;
    }

    private Location getEmptyLocation(Location startLocation) {
        ArrayList<Location> allEmpty = new ArrayList<Location>();

        for (int r = 0; r < this.getNumRows(); r++) {
            for (int c = 0; c < this.getNumCols(); c++) {
                if (this.board[r][c] == 0 && !(new Location(r,c).equals(startLocation))) {
                    allEmpty.add(new Location(r, c));
                }
            }
        }

        Collections.shuffle(allEmpty);

        if (!allEmpty.isEmpty()) { return allEmpty.get(0); }
        else { return null; }
    }

    // Numbers
    private void numberBoard() {
        for (int r = 0; r < this.getNumRows(); r++) {
            for (int c = 0; c < this.getNumCols(); c++) {
                if (this.board[r][c] != -1) {
                    ArrayList<Location> allNeighbors = this.getNeighbors(new Location(r, c));
                    int number = 0;

                    for (Location neighbor : allNeighbors) {
                        if (this.board[neighbor.getRow()][neighbor.getCol()] == -1) {
                            number++;
                        }
                    }

                    this.board[r][c] = number;
                }
            }
        }
    }

    private ArrayList<Location> getNeighbors(Location location) {
        ArrayList<Location> allNeighbors = new ArrayList<Location>();

        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                Location neighbor = new Location(location.getRow() + r, location.getCol() + c);

                if (this.validLocation(neighbor) && !location.equals(neighbor)) {
                    allNeighbors.add(neighbor);
                }
            }
        }

        return allNeighbors;
    }

    /* Drawing Methods */
    public Texture drawTile(int value) {
        if (value < 9) { return this.emptyTile; }
        else if (value == 9) { return this.bombTile; }
        else if (value == 10) { return this.emptyFloorTile; }
        else if (value == 11) { return this.oneTile; }
        else if (value == 12) { return this.twoTile; }
        else if (value == 13) { return this.threeTile; }
        else if (value == 14) { return this.fourTile; }
        else if (value == 15) { return this.fiveTile; }
        else if (value == 16) { return this.sixTile; }
        else if (value == 17) { return this.sevenTile; }
        else if (value == 18) { return this.eightTile; }
        else if (value <= 28) { return this.flagTile; }
        else { return this.emptyTile; }
    }

    /* Clicking Methods */
    public void leftMouse(float x, float y) {
        Location location = this.positionToLocation(x, y);

        if (this.getGameStatus() == 0) {
            this.start(location);
        }

        if (this.validLocation(location)) {
            int value = this.board[location.getRow()][location.getCol()];
            if (value == 0) {
                this.uncoverLocation(location);
            }
            else if (value == -1) {
                this.finish(-1);
                this.uncoverAllLocations();
            }
            else if (value >= -1 && value <= 8) {
                this.board[location.getRow()][location.getCol()] += 10;
            }

        }
    }

    public void rightMouse(float x, float y) {
        Location location = this.positionToLocation(x, y);
        if (this.validLocation(location)) {
            int value = this.board[location.getRow()][location.getCol()];

            if (value >= -1 && value <= 8) {
                this.board[location.getRow()][location.getCol()] += 20;
                this.numFlags++;

            }
            else if (value >= 19 && value <= 28) {
                this.board[location.getRow()][location.getCol()] -= 20;
                this.numFlags--;
            }
        }
    }

    private void uncoverLocation(Location location) {
        ArrayList<Location> allNeighbors = this.getNeighbors(location);
        int value = this.board[location.getRow()][location.getCol()];
        if (value >= 0 && value <= 8) {
            this.board[location.getRow()][location.getCol()] += 10;
        }

        for (Location neighbor : allNeighbors) {
            value = this.board[neighbor.getRow()][neighbor.getCol()];
            if (this.board[neighbor.getRow()][neighbor.getCol()] == 0) {
                this.uncoverLocation(neighbor);
            }

            if (value >= 1 && value <= 8) {
                this.board[neighbor.getRow()][neighbor.getCol()] += 10;
            }
        }
    }

    private void uncoverAllLocations() {
        for (int r = 0; r < this.getNumRows(); r++) {
            for (int c = 0; c < this.getNumCols(); c++) {
                int value = this.board[r][c];
                if (value >= -1 && value <= 8) {
                    this.board[r][c] += 10;
                }
                else if (value >= 19 && value <= 28) {
                    this.board[r][c] -= 10;
                }
            }
        }
    }
}
