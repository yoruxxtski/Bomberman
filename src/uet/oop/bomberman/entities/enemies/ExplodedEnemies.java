package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.Map.GenerateMap;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Bomber;

public class ExplodedEnemies extends AnimatedEntity {

    public String direction;

    public double speed;
    protected int death_animate = 20;

    public int OFFSET = 4;

    public double distance;

    public int diffX;
    public int diffY;

    public int bomberX;
    public int bomberY;

    public ExplodedEnemies(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        speed = 2;
    }

    @Override
    public void update() {
        if (appear()) {
            img = changeImage().getFxImage();
            CalculateMovement();
        }
    }

    public void Moving(double mx, double my) {
        x += mx;
        y += my;
    }

    public boolean appear() {
        if (GameManagement.numOfEnemy == 0 && GameManagement.numOfBomber == 1) {
            return true;
        }

        return false;
    }

    public void getBomberXY() {
        for(AnimatedEntity entity : GameManagement.movableEntities) {
            if (entity instanceof Bomber) {
                bomberX = entity.getTileX();
                bomberY = entity.getTileY();
            }
        }
    }
    public void CalculateMovement() {
        double mx = 0, my = 0;
        getBomberXY();

        diffX = getTileX() - bomberX;
        diffY = getTileY() - bomberY;

        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        if(distance <= 5 && GameManagement.numOfEnemy == 0) {
            mx = (-1 / distance) * diffX;
            my = (-1 / distance) * diffY;
            if(distance < 1) {
                mx = 0;
                my = 0;
            }
        } else {
            mx = 0;
            my = 0;
        }
        animate();
        Moving(mx, my);
    }

    public Sprite changeImage() {
        return Sprite.movingSprite(Sprite.minvo_left1
                , Sprite.minvo_left2 , Sprite.minvo_left3 , getAnimate() , 24);
    }

    public Sprite dieImage() {
        if (death_animate > 0) {
            return Sprite.minvo_dead;
        } else {
            return null;
        }
    }
}
