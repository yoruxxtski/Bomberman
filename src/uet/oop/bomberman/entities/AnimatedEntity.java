package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.HelperClass.Helper;

/**
 * AnimatedEntity = Bomber + Enemy
 */
public abstract class AnimatedEntity extends Entity {

    int animateTime = 0;

    final int MAX_ANIMATE = 6000;

    protected boolean killed = false;

    public static boolean isInvl = false;

    protected int invlTime = 80;

    public AnimatedEntity() {}

    public AnimatedEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public int getAnimate() {
        return animateTime;
    }

    public void animate() {
        if(animateTime < MAX_ANIMATE) {
            animateTime ++;
        } else {
            animateTime = 0;
        }
    }

    public void setInvl() {
        if(invlTime > 0) {
            invlTime--;
            System.out.println("InvlTime : " + invlTime);
        } else {
            invlTime = 80;
            isInvl = false;
            killed = false;
        }
    }

    public void killed() {
        if(!isInvl) {
            killed = true;
            isInvl = true;
        }
    }

    public void enemykilled() {
        killed = true;
    }

    public boolean isKilled() {
        return killed;
    }

    public abstract void update();
}
