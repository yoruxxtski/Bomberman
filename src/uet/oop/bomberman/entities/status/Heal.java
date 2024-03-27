package uet.oop.bomberman.entities.status;

import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.entities.Bomber;

public class Heal extends StatusEffect{

    public Heal(int xUnit, int yUnit) {
        super(xUnit, yUnit , Sprite.powerup_health.getFxImage());
    } // thay anh thang nay

    public Heal(Bomber bomber) {
        super(bomber);
    }

    @Override
    public void update() {
        isActive = false;
    }

    @Override
    public void init() {
        player.increaseLives();
    }
}
