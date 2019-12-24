package com.chobocho.tetris;

public class AdvancedTetrisBoard extends TetrisBoard {

    public AdvancedTetrisBoard(int width, int height) {
         super(width, height);
    }

    public void addRandomLine() {
        int w = this.getWidth();
        int h = this.getHeight();
        int x = 0;
        int y = 0;
        int m = 0;

        for (y = h - 1; y > 0; y--) {
            for (x = 0; x < w; x++) {
                for (m = y; m > 0; m--) {
                    board[m][x] = board[m + 1][x];
                }
            }
        }

        for (x = 0; x < w; x++) {
              board[h-1][x] = (int)(Math.random() * 2);
        }
    }

    public void removeLine() {

    }

    public void setBoard(int [][]newBoard) {

    }

    public void clearBoard() {
        int h = this.getHeight();
        int w = this.getWidth();
        for(int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                board[i][j] = 0;
            }
        }
    }

    public void dropBoom(int x, int y, int range) {

    }
}
