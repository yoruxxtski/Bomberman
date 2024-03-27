package uet.oop.bomberman.entities.status;

import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.entities.Bomber;

public class Speed extends StatusEffect{


    public Speed(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_speed.getFxImage());
    }

    public Speed(Bomber player) {
        super(player);
    }

    @Override
    public void update() {
        if(duration > 0) {
            duration --;
            System.out.println("duration :" + duration);
            if(player.speedCount > 1) {
                duration = 500;
                player.speedCount --;
                player.speed = player.speed - 0.2;
                System.out.println(player.speed);
            }
        } else {
            isActive = false;
            duration = 500;
            player.speed = player.speed - 0.2;
            player.speedCount = 0;
            System.out.println(player.speed);
        }
    }

    @Override
    public void init() {
        player.increaseSpeed();
    }
}
