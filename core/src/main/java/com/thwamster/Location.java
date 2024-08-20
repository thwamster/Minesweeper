package com.thwamster;

public class Location {
    /* Instance Variables */
    private int row;
    private int col;

    /* Constructor */
    public Location (int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    /* Helper Methods */
    public int getRow() { return this.row; }
    public int getCol() { return this.col; }

    /* Other Methods */
    public boolean equals(Location newLocation) {
        return newLocation.getRow() == this.row && newLocation.getCol() == this.col;
    }
}
