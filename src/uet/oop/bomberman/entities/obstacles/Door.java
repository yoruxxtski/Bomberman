package uet.oop.bomberman.entities.obstacles;

import javafx.scene.image.Image;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemies.EvilBomber;

public class Door extends Entity {

    public int open_animate = 130;

    public Door(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit,img);
    }

    @Override
    public void update() {
        if (Bomber.hasKey || EvilBomber.hasKey) {
            if (!isOpened()) {
                img = changeImage().getFxImage();
                open_animate--;
                System.out.println("Open:" + open_animate);
            }
        }
    }

    public Sprite changeImage() {
        if (open_animate > 120) {
            return Sprite.door_open;
        } else if (open_animate > 110) {
            return Sprite.door_open1;
        } else if (open_animate > 100) {
            return Sprite.door_open2;
        } else if (open_animate > 90) {
            return Sprite.door_open3;
        } else if (open_animate > 80) {
            return Sprite.door_open4;
        } else if (open_animate > 70) {
            return Sprite.door_open5;
        } else if (open_animate > 60) {
            return Sprite.door_open6;
        } else if (open_animate > 50) {
            return Sprite.door_open7;
        } else if (open_animate > 40) {
            return Sprite.door_open8;
        } else if (open_animate > 30) {
            return Sprite.door_open9;
        } else if (open_animate > 20) {
            return Sprite.door_open10;
        } else if (open_animate > 10) {
            return Sprite.door_open11;
        } else  {
            return Sprite.door_open12;
        }
    }

    public boolean isOpened() {
        if (open_animate > 0) {
            return false;
        } else {
            return true;
        }
    }
}
