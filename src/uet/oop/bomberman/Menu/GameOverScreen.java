package uet.oop.bomberman.Menu;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import uet.oop.bomberman.GameManagement;
import uet.oop.bomberman.entities.Bomber;

import java.io.FileNotFoundException;

public class GameOverScreen extends Parent {
    public GameOverScreen() {


        String path = "/textures/gameover.png";
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(GameManagement.CANVAS_WIDTH);
        imageView.setFitHeight(GameManagement.CANVAS_HEIGHT);

        VBox menu0 = new VBox(10);

        menu0.setTranslateX(50);
        menu0.setTranslateY(200);

        MenuButton playButton = new MenuButton("PLAY AGAIN");
        playButton.setOnMouseClicked(event -> {
            GameManagement.SCREEN = "game";
            try {
                GameManagement.gameInit();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });



        MenuButton optionButton = new MenuButton("MAIN MENU");
        optionButton.setOnMouseClicked(event -> {
            GameManagement.SCREEN = "main menu";
            if (GameMenu.musicStatus.equalsIgnoreCase("ENABLE")) {
                GameManagement.MUSIC = true;
            } else {
                GameManagement.MUSIC = false;
            }

            if (GameMenu.soundStatus.equalsIgnoreCase("ENABLE")) {
                GameManagement.SOUND = true;
            } else {
                GameManagement.SOUND = false;
            }
        });

        MenuButton exitButton = new MenuButton("EXIT");
        exitButton.setOnMouseClicked(event -> {
            System.exit(0);
        });

        menu0.getChildren().addAll( playButton, optionButton, exitButton);


        Rectangle bg = new Rectangle(GameManagement.CANVAS_WIDTH, GameManagement.CANVAS_HEIGHT);
        bg.setFill(Color.GREY);
        bg.setOpacity(0.2);

        getChildren().addAll(imageView, bg, menu0);
    }
}
