package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.Map.GenerateMap;
import uet.oop.bomberman.Map.InputControl;
import uet.oop.bomberman.Menu.GameMenu;
import uet.oop.bomberman.Menu.GameOverScreen;
import uet.oop.bomberman.Menu.VictoryScreen;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.obstacles.Key;
import uet.oop.bomberman.entities.enemies.*;
import uet.oop.bomberman.entities.obstacles.Brick;
import uet.oop.bomberman.entities.obstacles.Grass;
import uet.oop.bomberman.entities.status.StatusEffect;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class will control the gameLoop
 */
public class GameManagement {
    public static Scene mainScene;
    public static Canvas canvas;
    public static Group root;

    public static GraphicsContext gc;

    public static final int WIDTH = 27;

    public static final int HEIGHT = 16;

    public static final int CANVAS_WIDTH = Sprite.SCALED_SIZE * WIDTH + 136; // 32 * 27
    public static final int CANVAS_HEIGHT = Sprite.SCALED_SIZE * HEIGHT; // 32 * 16

    private static long currentGameTime = 0;

    private static long startNanoTime;

    public static int numOfEnemy;

    public static int numOfBomber;

    public static int point;

    public static ArrayList<Entity> stillEntities = new ArrayList<>();

    public static ArrayList<AnimatedEntity> movableEntities = new ArrayList<>();

    public static ArrayList<Entity> bombs = new ArrayList<>();

    public static ArrayList<Entity> statusList = new ArrayList<>();

    public static ArrayList<Entity> portals = new ArrayList<>();

    public static GameMenu gameMenu = new GameMenu();

    public static GameOverScreen gameOverScreen = new GameOverScreen();

    public static VictoryScreen victoryScreen = new VictoryScreen();

    public static boolean SOUND = true;

    public static boolean MUSIC = true;

    public static String SCREEN = "main menu";

    public static MediaPlayer mediaPlayer;

    public static MediaPlayer soundPlayer;

    public static String musicPath;

    public static String soundPath;

    public static int previousPoint = 0;

    /**
     * Get Scene -> stage.setScene()
     * @return
     */
    public static Scene getScene() {
        return mainScene;
    }

    public static long getCurrentGameTime() {
        return currentGameTime;
    }

    /**
     * GAME LOOP HERE -----------------------------------------------------------------
     */

    public static void start() {
        music();

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long l) {

                if ((l - startNanoTime) / 100000000 > currentGameTime) {
                    ++currentGameTime;
                    //System.out.println(currentGameTime);
                }

                musicStatus();

                if (SCREEN.equalsIgnoreCase("main menu")) {
                    destroyGameOverScreen();
                    destroyVictoryScreen();
                    GameMenu();
                    currentGameTime = 0;
                } else if (SCREEN.equalsIgnoreCase("game")) {
                    destroyGameMenu();
                    destroyGameOverScreen();
                    destroyVictoryScreen();

                    gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                    update();
                    render();

                } else if (SCREEN.equalsIgnoreCase("game over")) {
                    GameOverScreen();
                    currentGameTime = 0;
                } else if (SCREEN.equalsIgnoreCase("victory")) {
                    VictoryScreen();
                    currentGameTime = 0;
                }
            }
        };

        timer.start();
    }

    /**
     * RENDER HERE ---------------------------------
     */
    private static void render() {
        for (Entity e : stillEntities) {
            if (e instanceof Key) {
                Key key = (Key) e;
                if (key.appear()) {
                    e.render(gc);
                }
            }
            else {
                e.render(gc);
            }
        }
        for (Entity e : portals) {
            e.render(gc);
        }
        for (Entity e : statusList) {
            e.render(gc);
        }
        for (Entity e : bombs) {
            e.render(gc);
        }
        for (AnimatedEntity e : movableEntities) {
            if (e instanceof ExplodedEnemies) {
                ExplodedEnemies enemy = (ExplodedEnemies) e;
                if (enemy.appear()) {
                    e.render(gc);
                }
            } else {
                e.render(gc);
            }
        }
        renderPlayerInfor();
    }

    /**
     * UPDATE ENTITIES HERE ---------------------------------
     */
    public static void update() {
        if (numOfBomber == 0) {
            SCREEN = "game over";
        }

        // update movable entities
        for(Iterator<AnimatedEntity> e = movableEntities.iterator() ; e.hasNext();) {
            AnimatedEntity mE = e.next();
            mE.update();
            if(!mE.isAlive()) {
                if (mE instanceof Bomber) {
                    //SCREEN = "game over";
                    numOfBomber--;
                }
                if (mE instanceof EvilBomber) {
                    numOfBomber--;
                }
                if (mE instanceof Oneal || mE instanceof Balloom || mE instanceof ChasingEnemies) {
                    numOfEnemy--;
                    System.out.println("Enemy: " + numOfEnemy);
                    point += 100;
                }
                e.remove();
            }
        }


        // USE THIS BECAUSE IF USE ARRAYLIST , WHEN REMOVES THE SIZE = 0 -> will cause errors
        for(Iterator<Entity> e = bombs.iterator(); e.hasNext();) {
            Entity bomb = e.next();
            bomb.update();
            if(!bomb.isAlive()) {
                e.remove();
            }
        }

        ArrayList<Entity> newGrassList = new ArrayList<>();

        for(ListIterator<Entity> e = stillEntities.listIterator(); e.hasNext();) {
            Entity brick = e.next();
            brick.update();
            if(!brick.isAlive()) {
                Random random = new Random();
                // chay tu 0 den 49 : item chiem 70 % = 35 tuong duong tu 0 den 34
                if(random.nextInt(50) < 35 && brick instanceof Brick) {

                    //IncreaseBombLevel statusEffect = new IncreaseBombLevel(brick.getTileX(), brick.getTileY());
                    StatusEffect statusEffect = StatusEffect.initStatus(brick.getTileX() , brick.getTileY());
                    statusList.add(statusEffect);

                    e.remove();
                } else {
                    Entity newGrass = new Grass(brick.getTileX(), brick.getTileY(), Sprite.grass.getFxImage());
                    newGrassList.add(newGrass);
                    e.remove();
                }
            }
        }

        for(Iterator<Entity> e = statusList.iterator(); e.hasNext();) {
            Entity s = e.next();
            if(!s.isAlive()) {
                Entity newGrass = new Grass(s.getTileX(), s.getTileY(), Sprite.grass.getFxImage());
                newGrassList.add(newGrass);
                e.remove();
            }
        }

        stillEntities.addAll(newGrassList);
    }


    /**
     * CREATE GAME WINDOW + GET INPUT CONTROL + CREATE MAP + GAME LOOP START
     * @throws FileNotFoundException
     */

    public static void init() throws FileNotFoundException {
        root = new Group();
        mainScene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        // control
        InputControl.keyboardHandle(mainScene);


        // start game loop.
        start();
    }

    public static void gameInit() throws FileNotFoundException {
        stillEntities = new ArrayList<>();

        movableEntities = new ArrayList<>();

        bombs = new ArrayList<>();

        portals = new ArrayList<>();

        statusList = new ArrayList<>();

        if (GameMenu.musicStatus.equalsIgnoreCase("ENABLE")) {
            MUSIC = true;
        } else {
            MUSIC = false;
        }

        if (GameMenu.soundStatus.equalsIgnoreCase("ENABLE")) {
            SOUND = true;
        } else {
            SOUND = false;
        }

        numOfEnemy = 0;

        numOfBomber = 0;

        point = 0;

        GenerateMap.createMap();

    }

    public static void GameMenu() {
        if (!root.getChildren().contains(gameMenu)) {
            root.getChildren().add(gameMenu);
        }
    }

    public static void destroyGameMenu() {
        if (root.getChildren().contains(gameMenu)) {
            root.getChildren().remove(gameMenu);
        }
    }

    public static void GameOverScreen() {
        if (!root.getChildren().contains(gameOverScreen)) {
            root.getChildren().add(gameOverScreen);
        }
    }

    public static void destroyGameOverScreen() {
        if (root.getChildren().contains(gameOverScreen)) {
            root.getChildren().remove(gameOverScreen);
        }
    }

    public static void VictoryScreen() {
        if (!root.getChildren().contains(victoryScreen)) {
            root.getChildren().add(victoryScreen);
        }
    }

    public static void destroyVictoryScreen() {
        if (root.getChildren().contains(victoryScreen)) {
            root.getChildren().remove(victoryScreen);
        }
    }

    public static void music() {
        musicPath = "/music/stage_theme.mp3";
        Media newMedia = new Media(GameManagement.class.getResource(musicPath).toExternalForm());
        mediaPlayer = new MediaPlayer(newMedia);
    }

    public static void musicStatus() {
        if (MUSIC) {
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }
    }

    public static void sound(String name) {
        soundPath = "/sound/" + name + ".wav";
        Media newMedia = new Media(GameManagement.class.getResource(soundPath).toExternalForm());
        soundPlayer = new MediaPlayer(newMedia);
        if (SOUND) {
            soundPlayer.play();
        }
    }

    public static void renderPlayerInfor() {
        gc.setFill(Color.GREEN);
        gc.fillRect(864,0,136,512);

        gc.setFill(Color.BLACK);
        Font bomber = Font.loadFont("/fonts/BOMBERMA.ttf",15);
        gc.setFont(bomber);
        gc.fillText("Items" , 868 , 20);

        if(Bomber.hasKey == true) {
            gc.drawImage(Sprite.key.getFxImage(), 864 , 30 , 32 , 32);
        }

        gc.setFill(Color.BLACK);
        gc.setFont(bomber);
        gc.fillText("Points : " + point , 868 , 140);


        gc.setFill(Color.BLACK);
        gc.setFont(bomber);
        gc.fillText("Lives: " , 868 , 260);

        int j = 0;
        for(int i = 0 ; i < Bomber.lives ; i++) {
            gc.drawImage(new Image("./textures/life.png"),868 + j , 270,32,32);
            j += 42;
        }


        gc.setFill(Color.BLACK);
        gc.setFont(bomber);
        gc.fillText("Time : " + currentGameTime , 868 , 380);
    }
}