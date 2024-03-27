package uet.oop.bomberman.Map;

import javafx.scene.Scene;

/**
 * THIS CLASS controls the userInput
 */
public class InputControl {
    private static boolean up = false, down = false, left = false, right = false, space = false;

    public static boolean killAll = false;

    private static long timePressed = 0;
    public static void keyboardHandle(Scene s) {
        s.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    up = true;
                    break;
                case S:
                    down = true;
                    break;
                case A:
                    left = true;
                    break;
                case D:
                    right = true;
                    break;
                case SPACE:
                    if(System.currentTimeMillis() - timePressed > 200) {
                        space = true;
                        timePressed = System.currentTimeMillis();
                    }
                    break;
                case K:
                    killAll = true;
            }
        });
        s.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    up = false;
                    break;
                case S:
                    down = false;
                    break;
                case A:
                    left = false;
                    break;
                case D:
                    right = false;
                    break;
            }
        });
    }

    public static boolean isUp() {
        return up;
    }

    public static boolean isDown() {
        return down;
    }

    public static boolean isLeft() {
        return left;
    }

    public static boolean isRight() {
        return right;
    }

    public static boolean isSpace() {
        return space;
    }

    public static void setSpace() {
        space = false;
    }

    public static boolean isKillAll() {
        return killAll;
    }
}
