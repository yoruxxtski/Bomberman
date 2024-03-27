package uet.oop.bomberman.entities.status;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemies.EvilBomber;

import java.util.Random;

public abstract class StatusEffect extends Entity {

    static Bomber player;
    static EvilBomber evil;
    public int duration = 500;
    public boolean isActive = false;

    public StatusEffect(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public StatusEffect(Bomber player) {
        super();
        this.player = player;
    }

    public abstract void init();

    public int getDuration() {
        return duration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void disappear() {

    }

    public static StatusEffect initStatus(int x, int y) {
        Random random = new Random();
        
        switch (random.nextInt(4)) {
            case 0 :
                return new Heal(x , y); // thay anh
            case 1 :
                return new IncreaseBombLevel(x , y); // oke
            case 2 :
                return new Speed(x, y); // check lai collision
            case 3:
                return new Invincibility(x, y); // oke
            default:
                return null;
        }
    }
}
