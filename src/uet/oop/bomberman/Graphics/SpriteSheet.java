package uet.oop.bomberman.Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 *  This class takes the classic.png in res/textures/classic.png which contains all of the sprites needed
 */
public class SpriteSheet {
    private String _path;
    public final int SIZE;
    public int[] _pixels;
    public BufferedImage image;

    public static SpriteSheet tiles = new SpriteSheet("/textures/classic.png", 256);

    public static SpriteSheet door = new SpriteSheet("/textures/door_spritesheet.png", 448);

    public static SpriteSheet key = new SpriteSheet("/textures/key_spritesheet.png", 96);

    public SpriteSheet(String path, int size) {
        _path = path;
        SIZE = size;
        _pixels = new int[SIZE * SIZE];
        load();
    }

    private void load() {
        try {
            URL a = SpriteSheet.class.getResource(_path);
            image = ImageIO.read(a);
            int w = image.getWidth();
            int h = image.getHeight();
            if (w >= h) {
                image.getRGB(0, 0, w, h, _pixels, 0, w);
            } else {
                image.getRGB(0, 0, w, h, _pixels, 0, h);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
