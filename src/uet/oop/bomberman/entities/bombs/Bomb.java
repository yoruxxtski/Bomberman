package uet.oop.bomberman.entities.bombs;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.Map.GenerateMap;
import uet.oop.bomberman.entities.Entity;

import java.util.ArrayList;

/**
 * CLASS HERE CONTROLS BOMBS + RENDER explosions2
 */
public class Bomb extends Entity {

    // until it explodes
    public int bombTimer = 150; // 2,5s

    public int explode_animate = 30;

    public int bombLevel;

    ArrayList<Explosion> explosions2 = new ArrayList<>();
    ArrayList<Explosion> explosions1 = new ArrayList<>();
    ArrayList<Explosion> explosions = new ArrayList<>();

    boolean exploded = false;

    public Bomb(int xUnit, int yUnit, Image img , int bombLevel) {
        super(xUnit, yUnit, img);
        this.bombLevel = bombLevel; // ban dau = 1
    }

    public int getBombLevel() {
        return bombLevel;
    }

    /**
     *  this updates the remaining times until the bomb explodes
     *  bombTimer < 30 the bomb explodes , however , until it becomes < 0 , isAlive = false -> remove ( so that the
     *  explosions2 can be seen )
     */
    @Override
    public void update() {
        if (bombTimer > 0) { // start = 150
            if (bombTimer < 30) {
                if(!exploded) {  // exploded = true -> bomb explodes
                    explode(GenerateMap.map);
                    GameManagement.sound("explosion");
                }
                explode_animate--;
                updateExplosion();
            }
            bombTimer--;

        } else isAlive = false; // kill()
    }
    private void updateExplosion() {
        for(Explosion explosion : explosions) {
            explosion.update();
        }
    }

    /**
     * Bomb explosions2 ui
     * Here we use xUnit , yUnit which is the x ,y = 1 , 2
     * TO DO : SUA DE KHI BOMB LEVEL = 1 thi no hien ra full
     */
    public void explode(int[][] map) {
        bombTimer = 30;
        exploded = true;
        int xUnit = getTileX(); // = vi tri cua bomb
        int yUnit = getTileY(); // = vi tri cua bomb
        img = null;

        /*
                CENTER OF explosions2
         */
        Explosion explosionC2 = new Explosion(xUnit , yUnit , Sprite.bomb_exploded2.getFxImage()); // center of the explosions2
        Explosion explosionC1 = new Explosion(xUnit , yUnit , Sprite.bomb_exploded1.getFxImage()); // center of the explosions2
        Explosion explosionC = new Explosion(xUnit , yUnit , Sprite.bomb_exploded.getFxImage()); // center of the explosions2
        explosions2.add(explosionC2);
        explosions1.add(explosionC1);
        explosions.add(explosionC);

        /*
                DIRECTION = UP which means yUnit - 1
         */
        for(int i = 1 ; i <= bombLevel ; i++) {
            int UpYUnit = yUnit - i;
            if(UpYUnit == 0 || map[xUnit][UpYUnit] == 1) {
                break;
            } else if (UpYUnit == 1){
                Explosion explosionU2 = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical_top_last2.getFxImage());
                Explosion explosionU1 = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical_top_last1.getFxImage());
                Explosion explosionU = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical_top_last.getFxImage());
                explosions2.add(explosionU2);
                explosions1.add(explosionU1);
                explosions.add(explosionU);
                break;
            } else {
                Explosion explosionU2 = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical2.getFxImage());
                Explosion explosionU1 = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical1.getFxImage());
                Explosion explosionU = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical.getFxImage());
                explosions2.add(explosionU2);
                explosions1.add(explosionU1);
                explosions.add(explosionU);
            }
        }
        /*
              DIRECTION = DOWN which means yUnit + 1
         */
        for(int i = 1 ; i <= bombLevel ; i++) {
            int UpYUnit = yUnit + i;
            if (UpYUnit == GameManagement.HEIGHT || map[xUnit][UpYUnit] == 1) {
                break;
            } else if (UpYUnit == GameManagement.HEIGHT - 1){
                Explosion explosionD2 = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical_down_last2.getFxImage());
                Explosion explosionD1 = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical_down_last1.getFxImage());
                Explosion explosionD = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical_down_last.getFxImage());
                explosions2.add(explosionD2);
                explosions1.add(explosionD1);
                explosions.add(explosionD);
                break;
            } else {
                Explosion explosionD2 = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical2.getFxImage());
                Explosion explosionD1 = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical1.getFxImage());
                Explosion explosionD = new Explosion(xUnit , UpYUnit , Sprite.explosion_vertical.getFxImage());
                explosions2.add(explosionD2);
                explosions1.add(explosionD1);
                explosions.add(explosionD);
            }
        }
        /*
              DIRECTION = RIGHT which means xUnit + 1
         */
        for(int i = 1 ; i <= bombLevel ; i++) {
            int UpXUnit = xUnit + i;
            if(UpXUnit == GameManagement.WIDTH || map[UpXUnit][yUnit] == 1) {
                break;
            } else if(UpXUnit == GameManagement.WIDTH - 1){
                Explosion explosionR2 = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal_right_last2.getFxImage());
                Explosion explosionR1 = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal_right_last1.getFxImage());
                Explosion explosionR = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal_right_last.getFxImage());
                explosions2.add(explosionR2);
                explosions1.add(explosionR1);
                explosions.add(explosionR);
                break;
            } else {
                Explosion explosionR2 = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal2.getFxImage());
                Explosion explosionR1 = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal1.getFxImage());
                Explosion explosionR = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal.getFxImage());
                explosions2.add(explosionR2);
                explosions1.add(explosionR1);
                explosions.add(explosionR);
            }
        }
        /*
              DIRECTION = LEFT which means xUnit - 1
         */
        for(int i = 1 ; i <= bombLevel ; i++) {
            int UpXUnit = xUnit - i;
            if(UpXUnit == 0 || map[UpXUnit][yUnit] == 1) {
                break;
            } else if(UpXUnit == 1){
                Explosion explosionL2 = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal_left_last2.getFxImage());
                Explosion explosionL1 = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal_left_last1.getFxImage());
                Explosion explosionL = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal_left_last.getFxImage());
                explosions2.add(explosionL2);
                explosions1.add(explosionL1);
                explosions.add(explosionL);
                break;
            } else {
                Explosion explosionL2 = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal2.getFxImage());
                Explosion explosionL1 = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal1.getFxImage());
                Explosion explosionL = new Explosion(UpXUnit , yUnit, Sprite.explosion_horizontal.getFxImage());
                explosions2.add(explosionL2);
                explosions1.add(explosionL1);
                explosions.add(explosionL);
            }
        }
    }

    /**
     * render both the bombs and explosions so that when remove the bomb (after bombtimer -> 0)
     * remove the explosions also
     * @param gc
     */
    @Override
    public void render(GraphicsContext gc) {
        this.img = changeImage().getFxImage();
        gc.drawImage(img , x , y);
        if (explode_animate > 20) {
            for(Explosion explosion : explosions2) {
                explosion.render(gc);
            }
        } else if (explode_animate > 10) {
            for(Explosion explosion : explosions1) {
                explosion.render(gc);
            }
        } else {
            for(Explosion explosion : explosions) {
                explosion.render(gc);
            }
        }
    }

    public boolean isExploded() {
        return exploded;
    }

    /**
     * 150 % 24 = 6
     * time = 8
     * @return -> if > 30 change image else image = exploded so not exposing the bombs behind the explosion
     */
    public Sprite changeImage() {
        if(bombTimer > 30) {
            return Sprite.movingSprite(Sprite.bomb
                    , Sprite.bomb_1 , Sprite.bomb_2 , bombTimer , 24);
        } else {
            return Sprite.bomb_exploded;
        }
    }
}