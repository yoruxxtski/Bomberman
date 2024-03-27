package uet.oop.bomberman.entities.obstacles;

import javafx.scene.image.Image;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.enemies.EvilBomber;

public class Key extends AnimatedEntity {

    public Key(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        if (appear()) {
            img = changeImage().getFxImage();
            animate();
        }
    }

    public Sprite changeImage() {
        return Sprite.movingSprite(Sprite.key,
                Sprite.key1 , Sprite.key2 , Sprite.key3 , Sprite.key4 ,
                Sprite.key5 , getAnimate() , 48);
    }

    public boolean appear() {
        if (GameManagement.numOfEnemy == 0 && GameManagement.numOfBomber == 1 && !Bomber.hasKey && !EvilBomber.hasKey) {
            return true;
        }

        return false;
    }
}
