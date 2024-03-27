package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.HelperClass.PathFinder;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Bomber;

public class ChasingEnemies extends AnimatedEntity {

    public String direction;

    public double speed;

    public static final int OFFSET = 4;

    protected int death_animate = 60;

    public PathFinder pathFinder = new PathFinder();

    protected boolean onPath = true;

    public int bomberX;
    public int bomberY;

    @Override
    public void update() {
        if(killed) {
            if(death_animate > 0) {
                img = dieImage().getFxImage();
                death_animate--;
            } else {
                isAlive = false;
            }
        } else  {
            img = changeImage().getFxImage();
            CalculateMovement();
        }
    }

    public ChasingEnemies(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        direction = "left";
        speed = 1;
    }

    public void getBomberXY() {
        for(AnimatedEntity e : GameManagement.movableEntities) {
            if(e instanceof Bomber) {
                bomberX = e.getTileX();
                bomberY = e.getTileY();
            }
        }
    }

    public boolean isBomber(int nextX, int nextY) {
        for (AnimatedEntity e : GameManagement.movableEntities) {
            if (e instanceof Bomber) {
                if (nextX / 32 == e.getTileX() && nextY / 32 == e.getTileY()) {
                    return true;
                }
            }
        }

        return false;
    }

    public double getDistance(int col, int row) {
        double x = (col - getTileX()) * (col - getTileX());
        double y = (row - getTileY()) * (row - getTileY());

        return Math.abs(x + y);
    }

    public void searchPath(int goalCol, int goalRow) {
        if(onPath) {
            pathFinder.setNode(getTileX(), getTileY(), goalCol, goalRow);
            pathFinder.search();
        }

        /**
         *  BUG : if (startNode = (1,1)) then when enemies collide, it will move to (0,0)
         */

        int nextX = 0;
        int nextY = 0;

        if(pathFinder.pathList.size() > 0) {
             nextX = pathFinder.pathList.get(0).col * Sprite.SCALED_SIZE;
             nextY = pathFinder.pathList.get(0).row * Sprite.SCALED_SIZE;
        }
        if (y >= nextY && x >= nextX && x + 32 <= nextX + 32) {
            direction = "up";
            //System.out.println(direction);
        } else if (y <= nextY && x >= nextX && x + 32 <= nextX + 32) {
            direction = "down";
            //System.out.println(direction);
        } else if (x <= nextX && y >= nextY && y + 32 <= nextY + 32) {
            direction = "right";
            //System.out.println(direction);
        } else if (x >= nextX && y >= nextY && y + 32 <= nextY + 32) {
            direction = "left";
            //System.out.println(direction);
        }
        if (nextX / 32 == bomberX && nextY / 32 == bomberY) {
            onPath = false;
        }
        if (x == bomberX * 32 && y == bomberY * 32) {
            if (!isBomber(nextX, nextY)) {
                onPath = true;
            }
        }
    }

    public void Moving(double mx, double my) {
        x += mx;
        y += my;
    }

    public void CalculateMovement() {
        double mx = 0, my = 0;

        getBomberXY();

        {
            searchPath(bomberX, bomberY);
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

        }

        Moving(mx , my);
    }
    public Sprite dieImage() {
        if (death_animate > 0) {
            return Sprite.doll_dead;
        } else {
            return null;
        }
    }

    public Sprite changeImage() {
        switch (direction) {
            case "left": // left
                return Sprite.movingSprite(Sprite.doll_left1
                        ,Sprite.doll_left3, getAnimate() , 24);
            case "right": // right
                return Sprite.movingSprite(Sprite.doll_right1
                        ,Sprite.doll_right3, getAnimate() , 24);
            case "up": // up
                return Sprite.movingSprite(Sprite.doll_left2
                        ,Sprite.doll_left3, getAnimate() , 24);
            case "down": // down
                return Sprite.movingSprite(Sprite.doll_right2
                        ,Sprite.doll_right3, getAnimate() , 24);
            default:
                return null;
        }
    }
}
