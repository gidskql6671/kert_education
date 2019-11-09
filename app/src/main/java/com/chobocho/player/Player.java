package com.chobocho.player;

import com.chobocho.tetris.Score;
import com.chobocho.tetris.Tetrominos;

public interface Player {
    boolean setInputDevice(PlayerInput pi);
    boolean setView(PlayerUI pu);
    boolean setSCore(Score score);
    void register(PlayerObserver observer);
    void init();
    boolean MoveLeft();
    boolean MoveRight();
    boolean MoveDown();
    boolean rotate();
    boolean MoveBottom();
    boolean clickStartButton();
    boolean play();

    boolean pause();
    int getWidth();
    int getHeight();
    int[][] getBoard();
    int getScore();
    int getHighScore();
    int getRemovedLineCount();

    boolean isIdleState();
    boolean isGameOverState();
    boolean isPlayState();
    boolean isPauseState();
    boolean isEnableShadow();

    Tetrominos getCurrentBlock();
    Tetrominos getNextBlock();
    Tetrominos getShadowBlock();
}
