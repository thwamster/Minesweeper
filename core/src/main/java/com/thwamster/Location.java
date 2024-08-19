package com.thwamster;

public class Location {
    private int row;
    private int col;
    public Location (int newRow, int newCol) {

    }

    public int getRow() { return this.row; }
    public int getCol() { return this.col; }
    public void setRow(int newRow) { this.row = newRow; }
    public void setCol(int newCol) { this.col = newCol; }

    public boolean equals(Object object) {
        return false;
    }

    public String toString() {
        return "[" + this.row + ", " + this.col + "]";
    }
}
