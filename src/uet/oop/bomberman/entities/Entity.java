package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import uet.oop.bomberman.Graphics.Sprite;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.HelperClass.Helper;


import java.awt.*;

/**
 * Entity = grass + wall + others
 */
public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    public int x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    public int y;

    protected Image img;

    protected Rectangle boundary;

    protected boolean isAlive = true;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity( int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE; // * 32
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        this.boundary = new Rectangle();
    }

    public Entity() {}

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }
    public abstract void update();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBoundary() {
        boundary.setX(x);
        boundary.setY(y);
        boundary.setWidth(Sprite.SCALED_SIZE);
        boundary.setHeight(Sprite.SCALED_SIZE);

        return boundary;
    }

    public int getTileX() {
        return (int) (Helper.getPreciseDouble(x + 16) / 32);
    }

    public int getTileY() {
        return (int) (Helper.getPreciseDouble(y + 16) / 32);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }
}
