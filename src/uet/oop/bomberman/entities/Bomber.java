package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.Map.InputControl;
import uet.oop.bomberman.entities.bombs.Bomb;
import uet.oop.bomberman.entities.enemies.EvilBomber;
import uet.oop.bomberman.entities.enemies.ExplodedEnemies;
import uet.oop.bomberman.entities.obstacles.Brick;
import uet.oop.bomberman.entities.obstacles.Door;
import uet.oop.bomberman.entities.obstacles.Key;
import uet.oop.bomberman.entities.obstacles.Wall;
import uet.oop.bomberman.entities.status.*;

import java.util.HashMap;
import java.util.Map;

/**
 * THIS CLASS IS THE PLAYER WHICH CONTAINS ALL PLAYERS ATTRIBUTES & ACTIONS
 */
public class Bomber extends AnimatedEntity {

    public String direction;

    public double speed;

    public int bombQuantity;

    public int bombCount = 0;

    public int speedCount = 0;

    public int invincibleCount = 0;

    public static int bombQuality;

    public static final int OFFSET = 4;

    public boolean teleport = true;

    public static int lives;

    protected int hurt_animate = 40;

    protected int death_animate = 60;

    public static Map<String , StatusEffect> status = new HashMap<>();

    public Entity bomb;

    public static boolean hasKey;

    /**
     * Constructor
     * @param x
     * @param y
     * @param img
     */
    public Bomber(int x, int y, Image img) {
        super(x, y, img);
        speed = 2;
        direction = "right";
        bombQuantity = 100;
        bombQuality = 1;
        lives = 3;
        hasKey = false;
        status.clear();
        status.put("heal" , new Heal(this));
        status.put("incrsBLv" , new IncreaseBombLevel(this));
        status.put("speed" , new Speed(this));
        status.put("invincible" , new Invincibility(this));
    }

    public int getBombQuality() {
        return bombQuality;
    }

    public void setBombQuality(int bombQuality) {
        this.bombQuality = bombQuality;
    }

    /**
     * Update bomber by gameTime -> CalculateMovement => thay doi anh trong changeImage
     * update se duoc goi trong gameLoop
     */
    @Override
    public void update() {
        collideEnemy();

        if(killed) {
            if(invlTime == 80) {
                lives--;
                //System.out.println("lives remained" + lives);
            }
            setInvl();
        }

        if(lives == 0) {
            if (death_animate > 0) {
                img = dieImage().getFxImage();
                death_animate--;
            } else {
                isAlive = false;
                invlTime = 0;
                isInvl = false;
                killed = false;
                status.get("heal").duration = 0;
                status.get("incrsBLv").duration = 0;
                status.get("speed").duration = 0;
                status.get("invincible").duration = 0;
            }
            if (GameManagement.numOfBomber == 1) {
                GameManagement.MUSIC = false;
            }
            GameManagement.sound("player_die");
        }

        if(lives > 0) {
            img = changeImage().getFxImage();
            CalculateMovement();
            pickPowerUp();
            if (InputControl.isSpace()) {
                setBomb();
                InputControl.setSpace();
            }
//            if(InputControl.isKillAll()) {
//                for(Iterator<AnimatedEntity> e = GameManagement.movableEntities.iterator(); e.hasNext();) {
//                    AnimatedEntity mE = e.next();
//                    if (!(mE instanceof Bomber)) {
//                        mE.isAlive = false;
//                    }
//                }
//            }
            updateStatus();
        }
    }

    /**
     *  Bomber MoveMent : from mx , my -> direction -> changeImage()
     */

    public void Moving(double mx , double my) {
        if (mx < 0) {
            direction = "left";
        } else if (mx > 0) {
            direction = "right";
        } else if (my < 0) {
            direction = "up";
        } else if (my > 0) {
            direction = "down";
        }

        if (InputControl.isLeft() || InputControl.isRight()) {
            if (CanMoveWidth(mx)) {
                x += mx;
            }
        }
        if (InputControl.isUp() || InputControl.isDown()) {
            if (CanMoveHeight(my)) {
                y += my;
            }
        }

        canTeleport();
        if (teleport)
            Teleport();

    }

    /**
     * Collide enemy
     */
    public void collideEnemy() {
        // neu ma collide thi dung ham killed() nhe
        for (AnimatedEntity enemy : GameManagement.movableEntities) {
            if (!(enemy instanceof Bomber) && !(enemy instanceof EvilBomber)) {
                if (Math.abs(x  - enemy.x) < 24 && Math.abs(y - enemy.y) < 24) {
                    if(enemy instanceof ExplodedEnemies) {
                        ExplodedEnemies e = (ExplodedEnemies) enemy;
                        if (e.appear()){
                            lives = 0;
                        }
                    } else {
                        killed();
                    }
                }
            }
        }
    }

    /**
     * Get key
     */
    public void getKey(Key key) {
        if (key.appear()) {
            if (Math.abs(x  - key.x) < 24 && Math.abs(y - key.y) < 24) {
                hasKey = true;
            }
        }
    }

    /**
     * Go through door (Victory)
     */
    public void victory(Door door) {
        if (door.isOpened()) {
            if (Math.abs(x  - door.x) < 24 && Math.abs(y - door.y) < 24) {
                GameManagement.SCREEN = "victory";
                GameManagement.MUSIC = false;
                GameManagement.sound("next_level");
            }
        }
    }

    /**
     * Calculate Movement -> Check inputUser huong ve ben nao
     * Neu la left mx = - 2 -> left
     * right mx = 2 -> right
     * up my = - 2 -> up
     * down my = 2 -> down
     * su dung ham animate() -> tang ham getAnimate() -> thay doi anh
     */
    public void CalculateMovement() {
        double mx = 0, my = 0;
        if (InputControl.isLeft()) {
            mx = -speed;
            animate();
        }
        if (InputControl.isRight()) {
            mx = speed;
            animate();
        }
        if (InputControl.isUp()) {
            my = -speed;
            animate();
        }
        if (InputControl.isDown()) {
            my = speed;
            animate();
        }

        Moving(mx , my);
    }

    /**
     * Check if it can move
     * @param //mx
     * @param //my
     * @return
     *  mx = posX , my = PosY
     */

    public boolean CanMoveWidth(double mx) {
        for (Entity entity : GameManagement.stillEntities) {
            // collision with bricks and walls
            if (entity instanceof Wall || entity instanceof Brick) {
                if (entity.getBoundary().getBoundsInParent().intersects(x + speed + mx, y + speed, 22, 28)) { // 34, 34, 22, 28
                    return false;
                }
            }
            // collision with door
            if (entity instanceof Door) {
                Door door = (Door) entity;
                if (!door.isOpened()) {
                    if (entity.getBoundary().getBoundsInParent().intersects(x + speed + mx, y + speed, 22, 28)) { // 34, 34, 22, 28
                        return false;
                    }
                } else {
                    victory(door);
                }
            }
            // collision with key
            if (entity instanceof Key) {
                Key key = (Key) entity;
                getKey(key);
            }
        }

        // collision with bombs
        for (Entity entity : GameManagement.bombs) {
            if (x + 24 <= entity.x || x >= entity.x + Sprite.SCALED_SIZE || y + 28 <= entity.y || y >= entity.y + Sprite.SCALED_SIZE) {
                if (entity.getBoundary().getBoundsInParent().intersects(x + speed + mx, y + speed, 22, 28)) { // 34, 34, 22, 28
                    return false;
                }
            }
        }

        return true;
    }

    public boolean CanMoveHeight(double my) {
        for (Entity entity : GameManagement.stillEntities) {
            // collision with bricks and walls
            if (entity instanceof Brick || entity instanceof Wall) {
                if (entity.getBoundary().getBoundsInParent().intersects(x + speed, y + speed + my, 22, 28)) { // 34, 34, 22, 28
                    return false;
                }
            }
            // collision with door
            if (entity instanceof Door) {
                Door door = (Door) entity;
                if (!door.isOpened()) {
                    if (entity.getBoundary().getBoundsInParent().intersects(x + speed, y + speed + my, 22, 28)) { // 34, 34, 22, 28
                        return false;
                    }
                } else {
                    victory(door);
                }
            }
            // collision with key
            if (entity instanceof Key) {
                Key key = (Key) entity;
                getKey(key);
            }
        }

        // collision with bombs
        for (Entity entity : GameManagement.bombs) {
            if (x + 24 <= entity.x || x >= entity.x + Sprite.SCALED_SIZE || y + 28 < entity.y || y >= entity.y + Sprite.SCALED_SIZE) {
                if (entity.getBoundary().getBoundsInParent().intersects(x + speed, y + speed + my, 22, 28)) { // 34, 34, 22, 28
                    return false;
                }
            }

        }

        return true;
    }

    public void Teleport() {
        if (Math.abs(x - GameManagement.portals.get(0).x) < 24
                && Math.abs(y - GameManagement.portals.get(0).y) < 24) {
            x = GameManagement.portals.get(1).x;
            y = GameManagement.portals.get(1).y;
            teleport = false;
        } else if (Math.abs(x - GameManagement.portals.get(1).x) < 24
                && Math.abs(y - GameManagement.portals.get(1).y) < 24) {
            x = GameManagement.portals.get(0).x;
            y = GameManagement.portals.get(0).y;
            teleport = false;
        }
    }

    public void canTeleport() {
        if (Math.abs(x - GameManagement.portals.get(0).x) >= 24 && Math.abs(x - GameManagement.portals.get(0).x) <= Sprite.SCALED_SIZE
                || Math.abs(y - GameManagement.portals.get(0).y) >= 24 && Math.abs(y - GameManagement.portals.get(0).y) <= Sprite.SCALED_SIZE
                || Math.abs(x - GameManagement.portals.get(1).x) >= 24 && Math.abs(x - GameManagement.portals.get(1).x) <= Sprite.SCALED_SIZE
                || Math.abs(y - GameManagement.portals.get(1).y) >= 24 && Math.abs(y - GameManagement.portals.get(1).y) <= Sprite.SCALED_SIZE) {
            teleport = true;
        }
    }

    /**
     * Bomber Sprites when change the direction with
     * calc = getAnimate() % time = 24 -> calc = (0 , 24)
     * dif = time / 3 = 8
     * if(calc < dif = 8) -> player
     * calc < dif *2 = 16 -> player_1
     * else player_2 (calc >= dif *2 = 16 & calc < 24)
     * do ham animate() -> getAnimate() ++ => thay doi hinh anh
     */
     public Sprite changeImage() {
         switch (direction) {
             case "left": // left
                 if (InputControl.isLeft()) {
                     return Sprite.movingSprite(Sprite.player_left
                             , Sprite.player_left_1 , Sprite.player_left_2 , getAnimate() , 24);
                 }
                 return Sprite.player_left;
             case "right": // right
                 if (InputControl.isRight()) {
                     return Sprite.movingSprite(Sprite.player_right
                             , Sprite.player_right_1 , Sprite.player_right_2 , getAnimate() , 24);
                 }
                 return Sprite.player_right;
             case "up": // up
                 if (InputControl.isUp()) {
                     return Sprite.movingSprite(Sprite.player_up
                             , Sprite.player_up_1 , Sprite.player_up_2 , getAnimate() , 24);
                 }
                 return Sprite.player_up;
             case "down": // down
                 if (InputControl.isDown()) {
                     return Sprite.movingSprite(Sprite.player_down
                             , Sprite.player_down_1 , Sprite.player_down_2 , getAnimate() , 24);
                 }
                 return Sprite.player_down;
             default:
                 return null;
         }
     }

     public Sprite dieImage() {
         if (death_animate > 40) {
             return Sprite.player_dead1;
         } else if (death_animate > 20) {
             return Sprite.player_dead2;
         } else if (death_animate > 0) {
             return Sprite.player_dead3;
         } else {
             return null;
         }
     }

    /**
     * Bomber set bomb
     */
    public void setBomb() {
        bomb = new Bomb(getTileX() , getTileY() , Sprite.bomb.getFxImage(), bombQuality);
        GameManagement.bombs.add(bomb);
        GameManagement.sound("place_bomb");
    }

    /**
     *  updateStatus
     */
    public void updateStatus() {
        StatusEffect heal = status.get("heal");
        if(heal.isActive()) {
            heal.update();
        }

        StatusEffect bombLv = status.get("incrsBLv");
        if(bombLv.isActive()) {
            bombLv.update();
        }

        StatusEffect speedLv = status.get("speed");
        if(speedLv.isActive()) {
            speedLv.update();
        }

        StatusEffect invincible = status.get("invincible");
        if(invincible.isActive()) {
            invincible.update();
        }
    }

    /**
     *  getStatusEffect
     */
    public void getStatusEffect(StatusEffect statusEffect) {
        if(statusEffect.isAlive()) {
            if (statusEffect instanceof Heal) {
                status.get("heal").init();
            }
            if (statusEffect instanceof IncreaseBombLevel) {
                    status.get("incrsBLv").init();
                    status.get("incrsBLv").isActive = true;
                    bombCount++;
            }
            if (statusEffect instanceof Speed) {
                status.get("speed").init();
                status.get("speed").isActive = true;
                speedCount++;
            }
            if (statusEffect instanceof Invincibility) {
                status.get("invincible").init();
                status.get("invincible").isActive = true;
                invincibleCount++;
            }
        }
    }

    /**
     * Pick power up
     */
    public void pickPowerUp() {
        for(Entity statusi : GameManagement.statusList) {
            if (Math.abs(x  - statusi.x) <= 24 && Math.abs(y - statusi.y) <= 24) {
                getStatusEffect((StatusEffect) statusi);
                statusi.kill();
                GameManagement.sound("powerup");
            }
        }
    }

    // Bomber status effects
    public void increaseLives() {
        if(lives < 3) {
            lives++;
            System.out.println("Increase Lives" + lives);
        }
    }

    public void increaseBombLevel() {
        bombQuality++;
        System.out.println("Increase bomb quality");
    }

    public void increaseSpeed() {
        speed += 0.2;
        System.out.println("speed : " + speed);
    }

    public void beInvincible() {
       isInvl = true;
    }
}
