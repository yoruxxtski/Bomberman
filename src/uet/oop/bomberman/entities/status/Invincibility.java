package uet.oop.bomberman.entities.status;

import javafx.scene.image.Image;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.entities.Bomber;

public class Invincibility extends StatusEffect{

    public Invincibility(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.powerup_invincibility.getFxImage()); // thay anh thang nay
    }

    public Invincibility(Bomber player) {
        super(player);
    }

    @Override
    public void update() {
        if(duration > 0) {
            duration --;
            System.out.println("duration :" + duration);
            if(player.invincibleCount > 1) {
                duration = 500;
                player.invincibleCount --;
            }
        } else {
            isActive = false;
            duration = 500;
            player.isInvl = false;
            player.invincibleCount = 0;
        }
    }

    @Override
    public void init() {
        player.beInvincible();
    }
}
