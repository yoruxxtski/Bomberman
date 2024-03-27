package uet.oop.bomberman.Map;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.obstacles.*;
import uet.oop.bomberman.entities.enemies.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * THIS CLASS GENERATES THE MAP FROM THE TXTFILE in res -> IF YOU WANT TO CHANGE THE MAP -> CHANGE IN TXT FILE
 * PLEASE NOTE WHAT YOU CHANGE THERE
 */
public class GenerateMap {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 16;

    public static int[][] map = new int[WIDTH][HEIGHT]; //int[27][15]
    public static void inputMap() {
        try {
            File f = new File("./res/levels/Test.txt");
            Scanner sc = new Scanner(f);
            for (int j = 0 ; j < HEIGHT ; j++) {
                for (int i = 0 ; i < WIDTH ; i++) {
                    map[i][j] = Integer.parseInt(sc.next().trim());
                }
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createMap() throws FileNotFoundException {
        inputMap();
        for (int i = 0; i < WIDTH; i++) { // 27 -> default size = 16 -> scale = 32 * 27
            for (int j = 0; j < HEIGHT; j++) { // 26
                switch (map[i][j])
                {
                    case 0: // case grass = 32 x 32
                        Entity grass = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass);
                        break;
                    case 1: // case wall = 32 x 32
                        Entity wall = new Wall(i, j, Sprite.wall.getFxImage());
                        GameManagement.stillEntities.add(wall);
                        break;
                    case 2:
                        Entity brick = new Brick(i, j, Sprite.brick.getFxImage());
                        GameManagement.stillEntities.add(brick);
                        break;
                    case 3: // case player ->
                        /*
                            FOR SOME REASONS the image rendered must be on a entity so that it's transparent
                            & can be seen through
                         */
                        Entity grass1 = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass1);
                        AnimatedEntity player = new Bomber(i , j, Sprite.player_right.getFxImage());
                        GameManagement.movableEntities.add(player);
                        GameManagement.numOfBomber++;
                        break;
                    case 4: // case ballom ->
                        /*
                            FOR SOME REASONS the image rendered must be on a entity so that it's transparent
                            & can be seen through
                         */
                        Entity grass2 = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass2);
                        AnimatedEntity ballom = new Balloom(i, j, Sprite.balloom_left1.getFxImage());
                        GameManagement.movableEntities.add(ballom);
                        GameManagement.numOfEnemy++;
                        break;
                    case 5: // case oneal ->
                        /*
                            FOR SOME REASONS the image rendered must be on a entity so that it's transparent
                            & can be seen through
                         */
                        Entity grass3 = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass3);
                        AnimatedEntity oneal = new Oneal(i, j, Sprite.oneal_left1.getFxImage());
                        GameManagement.movableEntities.add(oneal);
                        GameManagement.numOfEnemy++;
                        break;

                    case 6: // chasing enemy
                        Entity grass4 = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass4);
                        AnimatedEntity doll = new ChasingEnemies(i, j, Sprite.doll_left1.getFxImage());
                        GameManagement.movableEntities.add(doll);
                        GameManagement.numOfEnemy++;
                        break;
                    case 7: // case portal
                        Entity portal = new Portal(i, j, Sprite.portal.getFxImage());
                        GameManagement.portals.add(portal);
                        break;
                    case 8: // case door
                        Entity grass5 = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass5);
                        Entity door = new Door(i, j, Sprite.door_close.getFxImage());
                        GameManagement.stillEntities.add(door);
                        break;
                    case 9: // case key
                        /*
                            FOR SOME REASONS the image rendered must be on a entity so that it's transparent
                            & can be seen through
                         */
                        Entity grass6 = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass6);
                        Entity key = new Key(i, j, Sprite.key.getFxImage());
                        GameManagement.stillEntities.add(key);
                        break;
                    case 10: // boss
                        Entity grass7 = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass7);
                        AnimatedEntity minvo = new ExplodedEnemies(i, j, Sprite.minvo_left1.getFxImage());
                        GameManagement.movableEntities.add(minvo);
                        break;
                    case 11: // case evil bomber

                        /*
                            FOR SOME REASONS the image rendered must be on a entity so that it's transparent
                            & can be seen through
                         */
                        Entity grass8 = new Grass(i, j, Sprite.grass.getFxImage());
                        GameManagement.stillEntities.add(grass8);
                        AnimatedEntity evil = new EvilBomber(i , j, Sprite.player_left.getFxImage());
                        GameManagement.movableEntities.add(evil);
                        GameManagement.numOfBomber++;
                        break;
                }
            }
        }
    }
}
