package com.chobocho.tetris;

public interface ITetrisGameState {
    void addRemoveLineCount(int line);
    void ClearBoard();
    void update();
    void updateHighScore();
}
