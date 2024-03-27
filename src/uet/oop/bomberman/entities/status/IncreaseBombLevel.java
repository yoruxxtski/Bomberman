package uet.oop.bomberman.entities.status;

import javafx.scene.image.Image;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.entities.Bomber;

public class IncreaseBombLevel extends StatusEffect{


    public IncreaseBombLevel(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_bombs.getFxImage());
    }

    public IncreaseBombLevel(Bomber bomber) {
        super(bomber);
    }

    @Override
    public void update() {
        if(duration > 0) {
            duration --;
            System.out.println("duration :" + duration);
            if(player.bombCount > 1) {
                duration = 500;
                player.bombCount--;
                player.bombQuality--;
                System.out.println(player.bombQuality);
            }
        } else {
            isActive = false;
            duration = 500;
            player.bombQuality--;
            player.bombCount = 0;
            System.out.println(player.bombQuality);
        }
    }

    @Override
    public void init() {
        player.increaseBombLevel();
    }
}
