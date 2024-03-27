package uet.oop.bomberman.entities.bombs;

import javafx.scene.image.Image;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.Graphics.Sprite;
import uet.oop.bomberman.Map.GenerateMap;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.enemies.ExplodedEnemies;
import uet.oop.bomberman.entities.obstacles.Brick;

public class Explosion extends Entity {
    public Explosion(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    /**
     * Check collision between explosion and entities
     * e.killed -> killed = true
     */
    @Override
    public void update() {
        // explosion collides with animated entities = bomber + enemies
        for(AnimatedEntity e : GameManagement.movableEntities) {
            if(e.getTileX() == getX() / Sprite.SCALED_SIZE && e.getTileY() == getY() / Sprite.SCALED_SIZE) {
                if(e instanceof Bomber) {
                    e.killed();
                } else if (e instanceof ExplodedEnemies) {

                } else {
                    e.enemykilled();
                }
            }
        }

        // explosion collides with brick
        for(Entity e : GameManagement.stillEntities) {
            if(e instanceof Brick) {
                if(e.getX() == getX() && e.getY() == getY()) {
                    e.kill();
                    GenerateMap.map[e.getTileX()][e.getTileY()] = 0;
                }
            }
        }

        // explosion collides with other bombs
        for(Entity bomb : GameManagement.bombs) {
            Bomb b = (Bomb) bomb;
            if(bomb.getX() == getX() && bomb.getY() == getY()) {
                if(!b.isExploded()) {
                    b.explode(GenerateMap.map);
                }
            }
        }
    }
}
