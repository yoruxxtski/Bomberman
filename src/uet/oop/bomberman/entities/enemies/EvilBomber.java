package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.HelperClass.FindPath;
import uet.oop.bomberman.Map.GenerateMap;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.bombs.Bomb;
import uet.oop.bomberman.entities.obstacles.Brick;
import uet.oop.bomberman.entities.obstacles.Door;
import uet.oop.bomberman.entities.obstacles.Key;
import uet.oop.bomberman.entities.obstacles.Wall;
import uet.oop.bomberman.entities.status.*;

import java.util.HashMap;
import java.util.Map;

public class EvilBomber extends AnimatedEntity {

    public String direction;

    public double speed;

    public int bombQuantity;

    public int bombCount = 0;

    public int speedCount = 0;

    public int invincibleCount = 0;

    public static int bombQuality;

    public static final int OFFSET = 4;

    public boolean teleport = true;

    public static int lives;

    protected int hurt_animate = 40;

    protected int death_animate = 60;

    public static Map<String , StatusEffect> status = new HashMap<>();

    public Entity bomb;

    public static boolean hasKey;

    protected FindPath findPath = new FindPath();

    public boolean canSearch = true;

    //public boolean onPath = true;

    //public Bomber player;
    public int goalX = 1;

    public int goalY = 1;

    public boolean meetBrick = false;

    public boolean setBomb = false;

    public int bombTime = 150;

    public boolean found = false;

    public EvilBomber(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        direction = "left";
        speed = 2;
        bombQuantity = 100;
        bombQuality = 1;
        lives = 3;
        hasKey = false;
    }

    public int getBombQuality() {
        return bombQuality;
    }

    public void setBombQuality(int bombQuality) {
        this.bombQuality = bombQuality;
    }

    @Override
    public void update() {
        collideEnemy();

        if(killed) {
            if(invlTime == 80) {
                lives--;
                System.out.println("lives remained" + lives);
            }
            setInvl();
        }

        if(lives == 0) {
            if (death_animate > 0) {
                img = dieImage().getFxImage();
                death_animate--;
            } else {
                isAlive = false;
                invlTime = 0;
                isInvl = false;
                killed = false;
            }
            if (GameManagement.numOfBomber == 1) {
                GameManagement.MUSIC = false;
            }
            GameManagement.sound("player_die");
        }

        if(lives > 0)
        {
            img = changeImage().getFxImage();

            CalculateMovement();
            if (meetBrick) {
                setBomb = true;
                setBomb();

            }
            if (setBomb) {
                if (bombTime == 150) {
                    //System.out.println("direction: " + direction);
                }
                setBombTime();
            }
        }
    }

    public void getBomberXY() {
        double distance = 999;
        for (AnimatedEntity e : GameManagement.movableEntities) {
            if (!(e instanceof EvilBomber) && !(e instanceof ExplodedEnemies)) {
                if (getDistance(e.getTileX(), e.getTileY()) < distance) {
                    goalX = e.getTileX();
                    goalY = e.getTileY();
                    distance = getDistance(e.getTileX(), e.getTileY());
                }
            }
        }
    }

    public boolean isBomber(int nextX, int nextY) {
        for (AnimatedEntity e : GameManagement.movableEntities) {
            if (!(e instanceof EvilBomber) && !(e instanceof ExplodedEnemies)) {
                if (nextX / 32 == e.getTileX() && nextY / 32 == e.getTileY()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isBrick(int nextX, int nextY) {
        for (Entity e : GameManagement.stillEntities) {
            if (e instanceof Brick) {
                if (nextX / 32 == e.getTileX() && nextY / 32 == e.getTileY()) {
                    return true;
                }
            }
        }

        return false;
    }

    public void dodgeBomb() {
        if (direction.equalsIgnoreCase("up")) {
            canUp();
        } else if (direction.equalsIgnoreCase("down")) {
            canDown();
        } else if (direction.equalsIgnoreCase("right")) {
            canRight();
        } else if (direction.equalsIgnoreCase("left")) {
            canLeft();
        }
    }

    public boolean isKey(int nextX, int nextY) {
        for (Entity e : GameManagement.stillEntities) {
            if (e instanceof Key) {
                Key key = (Key) e;
                if (key.appear()) {
                    if (nextX / 32 == key.getTileX() && nextY / 32 == e.getTileY()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void getKeyXY() {
        for (Entity e : GameManagement.stillEntities) {
            if (e instanceof Key) {
                goalX = e.getTileX();
                goalY = e.getTileY();
            }
        }
    }
    public void getDoorXY() {
        for (Entity e : GameManagement.stillEntities) {

        if (e instanceof Door) {
                Door door = (Door) e;
                if (door.isOpened()) {
                    goalX = door.getTileX();
                    goalY = door.getTileY();
                }
            }
        }
    }

    public void canUp() {
        for (int j = getTileY(); j <= getTileY() + (bombQuality + 1) && j >= 0 && j <= GameManagement.HEIGHT - 1; j++) {
            for (int i = Math.abs(getTileX() - (bombQuality + 1)); i <= getTileX() + (bombQuality + 1) && i >= 0 && i <= GameManagement.WIDTH - 1; i++) {
                if (getTileY() < j && j <= getTileY() + (bombQuality + 1)) {
                    if (j == getTileY() + (bombQuality + 1)) {
                        if (i == getTileX()) {
                            if (GenerateMap.map[getTileX()][getTileY() + 1] != 2 && !findPath.node[getTileX()][getTileY() + 1].solid) {
                                if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                    goalX = i;
                                    goalY = j;
                                    System.out.println(goalX + " " + goalY);
                                    found = true;
                                    break;
                                }
                            }
                        }
                    } else if (getTileX() - (bombQuality + 1) <= i && i < getTileX() || getTileX() < i && i <= getTileX() + (bombQuality + 1)) {
                        if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                            goalX = i;
                            goalY = j;
                            System.out.println(goalX + " " + goalY);
                            found = true;
                            break;
                        }
                    }
                } else if (j == getTileY()) {
                    if (i == getTileX() - (bombQuality + 1)) {
                        if (GenerateMap.map[getTileX() - 1][getTileY()] != 2 && !findPath.node[getTileX() - 1][getTileY()].solid) {
                            if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                goalX = i;
                                goalY = j;
                                System.out.println(goalX + " " + goalY);
                                found = true;
                                break;
                            }
                        }
                    } else if (i == getTileX() + (bombQuality + 1)) {
                        if (GenerateMap.map[getTileX() + 1][getTileY()] != 2 && !findPath.node[getTileX() + 1][getTileY()].solid) {
                            if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                goalX = i;
                                goalY = j;
                                System.out.println(goalX + " " + goalY);
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (found) break;
        }
    }

    public void canDown() {
        for (int j = Math.abs(getTileY() - (bombQuality + 1)); j <= getTileY() && j >= 0 && j <= GameManagement.HEIGHT - 1; j++) {
            for (int i = Math.abs(getTileX() - (bombQuality + 1)); i <= getTileX() + (bombQuality + 1) && i >= 0 && i <= GameManagement.WIDTH - 1; i++) {
                if (getTileY() - (bombQuality + 1) <= j && j < getTileY()) {
                    if (j == getTileY() - (bombQuality + 1)) {
                        if (i == getTileX()) {
                            if (GenerateMap.map[getTileX()][getTileY() - 1] != 2 && !findPath.node[getTileX()][getTileY() - 1].solid) {
                                if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                    goalX = i;
                                    goalY = j;
                                    System.out.println(goalX + " " + goalY);
                                    found = true;
                                    break;
                                }
                            }
                        }
                    } else if (getTileX() - (bombQuality + 1) <= i && i < getTileX() || getTileX() < i && i <= getTileX() + (bombQuality + 1)) {
                        if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                            goalX = i;
                            goalY = j;
                            System.out.println(goalX + " " + goalY);
                            found = true;
                            break;
                        }
                    }
                } else if (j == getTileY()) {
                    if (i == getTileX() - (bombQuality + 1)) {
                        if (GenerateMap.map[getTileX() - 1][getTileY()] != 2 && !findPath.node[getTileX() - 1][getTileY()].solid) {
                            if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                goalX = i;
                                goalY = j;
                                System.out.println(goalX + " " + goalY);
                                found = true;
                                break;
                            }
                        }
                    } else if (i == getTileX() + (bombQuality + 1)) {
                        if (GenerateMap.map[getTileX() + 1][getTileY()] != 2 && !findPath.node[getTileX() + 1][getTileY()].solid) {
                            if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                goalX = i;
                                goalY = j;
                                System.out.println(goalX + " " + goalY);
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (found) break;
        }
    }

    public void canRight() {
        for (int j = Math.abs(getTileY() - (bombQuality + 1)); j <= getTileY() + (bombQuality + 1) && j >= 0 && j <= GameManagement.HEIGHT - 1; j++) {
            for (int i = Math.abs(getTileX() - (bombQuality + 1)); i <= getTileX() && i >= 0 && i <= GameManagement.WIDTH - 1; i++) {
                if (Math.abs(getTileX() - (bombQuality + 1)) <= i && i < getTileX()) {
                    if (i == getTileX() - (bombQuality + 1)) {
                        if (j == getTileY()) {
                            if (GenerateMap.map[getTileX() - 1][getTileY()] != 2 && !findPath.node[getTileX() - 1][getTileY()].solid) {
                                if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                    goalX = i;
                                    goalY = j;
                                    System.out.println(goalX + " " + goalY);
                                    found = true;
                                    break;
                                }
                            }
                        }
                    } else if (Math.abs(getTileY() - (bombQuality + 1)) <= j && j < getTileY() || getTileY() < j && j <= getTileY() + (bombQuality + 1)) {
                        if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                            goalX = i;
                            goalY = j;
                            System.out.println(goalX + " " + goalY);
                            found = true;
                            break;
                        }
                    }
                } else if (i == getTileX()) {
                    if (j == getTileY() - (bombQuality + 1)) {
                        if (GenerateMap.map[getTileX()][getTileY() - 1] != 2 && !findPath.node[getTileX()][getTileY() - 1].solid) {
                            if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                goalX = i;
                                goalY = j;
                                System.out.println(goalX + " " + goalY);
                                found = true;
                                break;
                            }
                        }
                    } else if (j == getTileY() + (bombQuality + 1)) {
                        if (GenerateMap.map[getTileX()][getTileY() + 1] != 2 && !findPath.node[getTileX()][getTileY() + 1].solid) {
                            if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                goalX = i;
                                goalY = j;
                                System.out.println(goalX + " " + goalY);
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (found) break;
        }
    }

    public void canLeft() {
        for (int j = Math.abs(getTileY() - (bombQuality + 1)); j <= getTileY() + (bombQuality + 1) && j >= 0 && j <= GameManagement.HEIGHT - 1; j++) {
            for (int i = getTileX(); i <= getTileX() + (bombQuality + 1) && i >= 0 && i <= GameManagement.WIDTH - 1; i++) {
                if (getTileX() < i && i <= getTileX() + (bombQuality + 1)) {
                    if (i == getTileX() + (bombQuality + 1)) {
                        if (j == getTileY()) {
                            if (GenerateMap.map[getTileX() + 1][getTileY()] != 2 && !findPath.node[getTileX() + 1][getTileY()].solid) {
                                if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                    goalX = i;
                                    goalY = j;
                                    System.out.println(goalX + " " + goalY);
                                    found = true;
                                    break;
                                }
                            }
                        }
                    } else if (Math.abs(getTileY() - (bombQuality + 1)) <= j && j < getTileY() || getTileY() < j && j <= getTileY() + (bombQuality + 1)) {
                        if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                            goalX = i;
                            goalY = j;
                            System.out.println(goalX + " " + goalY);
                            found = true;
                            break;
                        }
                    }
                } else if (i == getTileX()) {
                    if (j == getTileY() - (bombQuality + 1)) {
                        if (GenerateMap.map[getTileX()][getTileY() - 1] != 2 && !findPath.node[getTileX()][getTileY() - 1].solid) {
                            if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                goalX = i;
                                goalY = j;
                                System.out.println(goalX + " " + goalY);
                                found = true;
                                break;
                            }
                        }
                    } else if (j == getTileY() + (bombQuality + 1)) {
                        if (GenerateMap.map[getTileX()][getTileY() + 1] != 2 && !findPath.node[getTileX()][getTileY() + 1].solid) {
                            if (GenerateMap.map[i][j] != 2 && !findPath.node[i][j].solid) {
                                goalX = i;
                                goalY = j;
                                System.out.println(goalX + " " + goalY);
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (found) break;
        }
    }

    public double getDistance(int col, int row) {
        double x = (col - getTileX()) * (col - getTileX());
        double y = (row - getTileY()) * (row - getTileY());

        return Math.abs(x + y);
    }


    public void searchPath(int goalCol, int goalRow) {
        if (canSearch) {
            findPath.setNode(getTileX(), getTileY(), goalCol, goalRow);
            findPath.search();
        }
        int nextX = findPath.pathList.get(0).col * Sprite.SCALED_SIZE;
        int nextY = findPath.pathList.get(0).row * Sprite.SCALED_SIZE;

        if (y >= nextY && x >= nextX && x + 32 <= nextX + 32) {
            direction = "up";
            System.out.println("next: " + nextX / 32 + " " + nextY / 32);
        } else if (y <= nextY && x >= nextX && x + 32 <= nextX + 32) {
            direction = "down";
            System.out.println("next: " + nextX / 32 + " " + nextY / 32);
        } else if (x <= nextX && y >= nextY && y + 32 <= nextY + 32) {
            direction = "right";
            System.out.println("next: " + nextX / 32 + " " + nextY / 32);
        } else if (x >= nextX && y >= nextY && y + 32 <= nextY + 32) {
            direction = "left";
            System.out.println("next: " + nextX / 32 + " " + nextY / 32);
        }
        if (nextX / 32 == goalX && nextY / 32 == goalY) {
            canSearch = false;
        } else {
            canSearch = true;
        }
        if (x == goalX * 32 && y == goalY * 32) {
            //onPath = false;
            meetBrick = false;
            if (!setBomb) {
                canSearch = true;
            }
        }
        //else {
            //onPath = true;
        //}
        if (isBrick(nextX, nextY) || isBomber(nextX, nextY)) {
            meetBrick = true;
        }
        if (isKey(nextX, nextY)) {
            hasKey = true;
        }
    }

    public void Moving(double mx , double my) {

        if (CanMove()) {
            x += mx;
            y += my;
        }
        //else {
            //onPath = false;
            //meetBrick = true;
            //setBomb = true;
        //}

        //System.out.println(getTileX() * 32 + " " + getTileY() * 32);
        //System.out.println(x + " " + y);
        //System.out.println("meetBrick: " + meetBrick);
        //System.out.println("setBomb: " + setBomb);
        //System.out.println("canSearch: " + canSearch);
        //System.out.println("direction_: " + direction);
        //System.out.println(goalX + " " + goalY);

        canTeleport();
        if (teleport)
            Teleport();

    }

    public void CalculateMovement() {
        double mx = 0, my = 0;

        if (meetBrick) {
            //setBomb = true;
            dodgeBomb();
            meetBrick = false;
            //onPath = true;

        } else if (!setBomb && !meetBrick) {
            getBomberXY();
            if (GameManagement.numOfBomber == 1 && isAlive() && !hasKey) {
                getKeyXY();
            } else {
                getDoorXY();
            }
        }

        searchPath(goalX, goalY);

        if (direction.equalsIgnoreCase("left")) {
            mx = -speed;
            animate();
        }
        else if (direction.equalsIgnoreCase("right")) {
            mx = speed;
            animate();
        }
        else if (direction.equalsIgnoreCase("up")) {
            my = -speed;
            animate();
        }
        else if (direction.equalsIgnoreCase("down")) {
            my = speed;
            animate();
        }

        Moving(mx , my);
    }

    public void collideEnemy() {
        // neu ma collide thi dung ham killed() nhe
        for (AnimatedEntity enemy : GameManagement.movableEntities) {
            if (!(enemy instanceof Bomber) && !(enemy instanceof EvilBomber)) {
                if (Math.abs(x  - enemy.x) < 24 && Math.abs(y - enemy.y) < 24) {
                    if(enemy instanceof ExplodedEnemies) {
                        ExplodedEnemies e = (ExplodedEnemies) enemy;
                        if (e.appear()){
                            lives = 0;
                        }
                    } else {
                        killed();
                    }
                }
            }
        }
    }

    public void victory(Door door) {
        if (door.isOpened()) {
            if (Math.abs(x  - door.x) < 24 && Math.abs(y - door.y) < 24) {
                GameManagement.SCREEN = "game over";
                GameManagement.MUSIC = false;
                GameManagement.sound("next_level");
            }
        }
    }

    public boolean CanMove() {
        for (Entity entity : GameManagement.stillEntities) {
            // check collision with bricks
            if (entity instanceof Wall || entity instanceof Brick) {
                if (direction.equalsIgnoreCase("right")
                        && Math.abs(x + Sprite.SCALED_SIZE - entity.x) <= 0
                        && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                    //x = entity.x - Sprite.SCALED_SIZE;
                    return false;
                } else if (direction.equalsIgnoreCase("left")
                        && Math.abs(x - entity.x - Sprite.SCALED_SIZE) <= 0
                        && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                    //x = entity.x + Sprite.SCALED_SIZE;
                    return false;
                } else if (direction.equalsIgnoreCase("up")
                        && Math.abs(y - entity.y - Sprite.SCALED_SIZE) <= 0
                        && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                    //y = entity.y + Sprite.SCALED_SIZE;
                    return false;
                } else if (direction.equalsIgnoreCase("down")
                        && Math.abs(y + Sprite.SCALED_SIZE - entity.y) <= 0
                        && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                    //y = entity.y - Sprite.SCALED_SIZE;
                    return false;
                }
            }

            // collision with door
            if (entity instanceof Door) {
                Door door = (Door) entity;
                if (!door.isOpened()) {
                    if (direction.equalsIgnoreCase("right")
                            && Math.abs(x + Sprite.SCALED_SIZE - entity.x) <= 0
                            && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                        //x = entity.x - Sprite.SCALED_SIZE;
                        return false;
                    } else if (direction.equalsIgnoreCase("left")
                            && Math.abs(x - entity.x - Sprite.SCALED_SIZE) <= 0
                            && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                        //x = entity.x + Sprite.SCALED_SIZE;
                        return false;
                    } else if (direction.equalsIgnoreCase("up")
                            && Math.abs(y - entity.y - Sprite.SCALED_SIZE) <= 0
                            && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                        //y = entity.y + Sprite.SCALED_SIZE;
                        return false;
                    } else if (direction.equalsIgnoreCase("down")
                            && Math.abs(y + Sprite.SCALED_SIZE - entity.y) <= 0
                            && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                        //y = entity.y - Sprite.SCALED_SIZE;
                        return false;
                    }
                } else {
                    victory(door);
                }
            }
        }

        // check collision with bombs
        for (Entity entity : GameManagement.bombs) {
            if (direction.equalsIgnoreCase("right")
                    && Math.abs(x + Sprite.SCALED_SIZE - entity.x) <= OFFSET
                    && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                //x = entity.x - Sprite.SCALED_SIZE;
                return false;
            } if (direction.equalsIgnoreCase("left")
                    && Math.abs(x - entity.x - Sprite.SCALED_SIZE) <= OFFSET
                    && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                //x = entity.x + Sprite.SCALED_SIZE;
                return false;
            } if (direction.equalsIgnoreCase("up")
                    && Math.abs(y - entity.y - Sprite.SCALED_SIZE) <= OFFSET
                    && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                //y = entity.y + Sprite.SCALED_SIZE;
                return false;
            } if (direction.equalsIgnoreCase("down")
                    && Math.abs(y + Sprite.SCALED_SIZE - entity.y) <= OFFSET
                    && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                //y = entity.y - Sprite.SCALED_SIZE;
                return false;
            }
        }

        return true;
    }

    public void Teleport() {
        if (Math.abs(x - GameManagement.portals.get(0).x) < 24
                && Math.abs(y - GameManagement.portals.get(0).y) < 24) {
            x = GameManagement.portals.get(1).x;
            y = GameManagement.portals.get(1).y;
            teleport = false;
        } else if (Math.abs(x - GameManagement.portals.get(1).x) < 24
                && Math.abs(y - GameManagement.portals.get(1).y) < 24) {
            x = GameManagement.portals.get(0).x;
            y = GameManagement.portals.get(0).y;
            teleport = false;
        }
    }

    public void canTeleport() {
        if (Math.abs(x - GameManagement.portals.get(0).x) >= 24 && Math.abs(x - GameManagement.portals.get(0).x) <= Sprite.SCALED_SIZE
                || Math.abs(y - GameManagement.portals.get(0).y) >= 24 && Math.abs(y - GameManagement.portals.get(0).y) <= Sprite.SCALED_SIZE
                || Math.abs(x - GameManagement.portals.get(1).x) >= 24 && Math.abs(x - GameManagement.portals.get(1).x) <= Sprite.SCALED_SIZE
                || Math.abs(y - GameManagement.portals.get(1).y) >= 24 && Math.abs(y - GameManagement.portals.get(1).y) <= Sprite.SCALED_SIZE) {
            teleport = true;
        }
    }

    public Sprite changeImage() {
        switch (direction) {
            case "left": // left

                    return Sprite.movingSprite(Sprite.player_left
                            , Sprite.player_left_1 , Sprite.player_left_2 , getAnimate() , 24);

            case "right": // right
                    return Sprite.movingSprite(Sprite.player_right
                            , Sprite.player_right_1 , Sprite.player_right_2 , getAnimate() , 24);

            case "up": // up
                    return Sprite.movingSprite(Sprite.player_up
                            , Sprite.player_up_1 , Sprite.player_up_2 , getAnimate() , 24);
            case "down": // down
                    return Sprite.movingSprite(Sprite.player_down
                            , Sprite.player_down_1 , Sprite.player_down_2 , getAnimate() , 24);
            default:
                return null;
        }
    }

    public Sprite dieImage() {
        if (death_animate > 40) {
            return Sprite.player_dead1;
        } else if (death_animate > 20) {
            return Sprite.player_dead2;
        } else if (death_animate > 0) {
            return Sprite.player_dead3;
        } else {
            return null;
        }
    }

    public void setBomb() {
        bomb = new Bomb(getTileX() , getTileY() , Sprite.bomb.getFxImage(), bombQuality);
        GameManagement.bombs.add(bomb);
        GameManagement.sound("place_bomb");
    }

    public void setBombTime() {
        bombTime--;
        if (bombTime == -1) {
            bombTime = 150;
            setBomb = false;
            found = false;
        }
    }

}
