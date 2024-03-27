package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.obstacles.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.obstacles.Wall;

import java.util.Random;

public class Oneal extends AnimatedEntity {

    public String direction;

    public double speed;

    protected int death_animate = 20;

    public int OFFSET = 4;

    public Oneal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        speed = 1;
        direction = getInitDirection();
    }

    @Override
    public void update() {
        if(killed) {
            if (death_animate > 0) {
                img = dieImage().getFxImage();
                death_animate--;
            } else {
                isAlive = false;
            }
        } else {
            img = changeImage().getFxImage();
            CalculateMovement();
        }
    }

    public void Moving(double mx , double my) {
        if (CanMove()) {
            x += mx;
            y += my;
        } else {
            getDirection();
        }
    }

    public void CalculateMovement() {
        double mx = 0 , my = 0;
        if (direction.equalsIgnoreCase("left")) {
            mx = - speed;
            animate();
        } else if (direction.equalsIgnoreCase("right")) {
            mx = speed;
            animate();
        } else if (direction.equalsIgnoreCase("up")) {
            my = -speed;
            animate();
        } else if (direction.equalsIgnoreCase("down")) {
            my = speed;
            animate();
        }

        Moving(mx , my);
    }

    public boolean CanMove() {
        // check collision with walls and bricks
        for (Entity entity : GameManagement.stillEntities) {
            if (entity instanceof Wall || entity instanceof Brick) {
                if (direction.equalsIgnoreCase("right")
                        && Math.abs(x + Sprite.SCALED_SIZE - entity.x) <= OFFSET
                        && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                    x = entity.x - Sprite.SCALED_SIZE;
                    return false;
                } else if (direction.equalsIgnoreCase("left")
                        && Math.abs(x - entity.x - Sprite.SCALED_SIZE) <= OFFSET
                        && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                    x = entity.x + Sprite.SCALED_SIZE;
                    return false;
                } else if (direction.equalsIgnoreCase("up")
                        && Math.abs(y - entity.y - Sprite.SCALED_SIZE) <= OFFSET
                        && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                    y = entity.y + Sprite.SCALED_SIZE;
                    return false;
                } else if (direction.equalsIgnoreCase("down")
                        && Math.abs(y + Sprite.SCALED_SIZE - entity.y) <= OFFSET
                        && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                    y = entity.y - Sprite.SCALED_SIZE;
                    return false;
                }
            }
        }

        // check collision with bombs
        for (Entity entity : GameManagement.bombs) {
            if (direction.equalsIgnoreCase("right")
                    && Math.abs(x + Sprite.SCALED_SIZE - entity.x) <= OFFSET
                    && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                x = entity.x - Sprite.SCALED_SIZE;
                return false;
            } if (direction.equalsIgnoreCase("left")
                    && Math.abs(x - entity.x - Sprite.SCALED_SIZE) <= OFFSET
                    && Math.abs(y - entity.y) < Sprite.SCALED_SIZE - OFFSET / 2) {
                x = entity.x + Sprite.SCALED_SIZE;
                return false;
            } if (direction.equalsIgnoreCase("up")
                    && Math.abs(y - entity.y - Sprite.SCALED_SIZE) <= OFFSET
                    && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                y = entity.y + Sprite.SCALED_SIZE;
                return false;
            } if (direction.equalsIgnoreCase("down")
                    && Math.abs(y + Sprite.SCALED_SIZE - entity.y) <= OFFSET
                    && Math.abs(x - entity.x) < Sprite.SCALED_SIZE - OFFSET / 2) {
                y = entity.y - Sprite.SCALED_SIZE;
                return false;
            }
        }

        return true;
    }

    public Sprite changeImage() {
        switch (direction) {
            case "left": // left
                return Sprite.movingSprite(Sprite.oneal_left1
                        , Sprite.oneal_left2 , Sprite.oneal_left3 , getAnimate() , 24);
            case "right": // right
                return Sprite.movingSprite(Sprite.oneal_right1
                        , Sprite.oneal_right2 , Sprite.oneal_right3 , getAnimate() , 24);
            case "up": // up
                return Sprite.movingSprite(Sprite.oneal_left1
                        , Sprite.oneal_left2 , Sprite.oneal_left3 , getAnimate() , 24);
            case "down": // down
                return Sprite.movingSprite(Sprite.oneal_left1
                        , Sprite.oneal_left2 , Sprite.oneal_left3 , getAnimate() , 24);
            default:
                return null;
        }
    }

    public void getDirection() {
        int number = generateRandomNumber();

        switch (number) {
            case 0:
                direction = "left";
                break;
            case 1:
                direction = "right";
                break;
            case 2:
                direction = "up";
                break;
            case 3:
                direction = "down";
                break;
        }
    }

    public String getInitDirection() {
        getDirection();
        return this.direction;
    }

    public int generateRandomNumber() {
        Random randomNumber = new Random();

        return randomNumber.nextInt(4);
    }

    public Sprite dieImage() {
        if (death_animate > 0) {
            return Sprite.oneal_dead;
        } else {
            return null;
        }
    }
}
