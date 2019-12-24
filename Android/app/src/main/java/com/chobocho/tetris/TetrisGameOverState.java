package com.chobocho.tetris;

/**
 * 
 */
public class TetrisGameOverState extends TetrisGameState {

    /**
     * Default constructor
     */
    public TetrisGameOverState(Tetris tetris) {
        this.tetris = tetris;
    }

    public boolean isGameOverState() {
        return true;
    }
}

