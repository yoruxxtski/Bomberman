package uet.oop.bomberman.Menu;

import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.GameManagement;

import java.io.FileNotFoundException;

public class GameMenu extends Parent {
    public static String soundStatus = "ENABLE";

    public static String musicStatus = "ENABLE";

    public final int offset = 400;

    public int soundClicked = 0;

    public int musicClicked = 0;

    public GameMenu() {
        String path = "/textures/background.png";
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(GameManagement.CANVAS_WIDTH);
        imageView.setFitHeight(GameManagement.CANVAS_HEIGHT);

        VBox menu0 = new VBox(10);
        VBox menu1 = new VBox(10);

        menu0.setTranslateX(50);
        menu0.setTranslateY(200);

        menu1.setTranslateX(50);
        menu1.setTranslateY(200);

        menu1.setTranslateX(offset);

        MenuButton playButton = new MenuButton("PLAY");
        playButton.setOnMouseClicked(event -> {
            GameManagement.SCREEN = "game";
            try {
                GameManagement.gameInit();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        MenuButton optionButton = new MenuButton("OPTION");
        optionButton.setOnMouseClicked(event -> {
            getChildren().add(menu1);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu0);
            tt.setToX(menu0.getTranslateX() - offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
            tt1.setToX(menu0.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(menu0);
            });
        });

        MenuButton exitButton = new MenuButton("EXIT");
        exitButton.setOnMouseClicked(event -> {
            System.exit(0);
        });

        MenuButton backButton = new MenuButton("BACK");
        backButton.setOnMouseClicked(event -> {
            getChildren().add(menu0);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
            tt.setToX(menu1.getTranslateX() + offset);

            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu0);
            tt1.setToX(menu1.getTranslateX());

            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
                getChildren().remove(menu1);
            });
        });

        MenuButton soundButton = new MenuButton("SOUND" + " : " + soundStatus);
        soundButton.setOnMouseClicked(event -> {
            soundClicked++;
            if (soundClicked % 2 == 0) {
                GameManagement.SOUND = true;
                soundStatus = "ENABLE";
                soundButton.setStatus("SOUND" + " : " + soundStatus);
            } else {
                GameManagement.SOUND = false;
                soundStatus = "DISABLE";
                soundButton.setStatus("SOUND" + " : " + soundStatus);
            }
        });

        MenuButton musicButton = new MenuButton("MUSIC" + " : " + musicStatus);
        musicButton.setOnMouseClicked(event -> {
            musicClicked++;
            if (musicClicked % 2 == 0) {
                GameManagement.MUSIC = true;
                musicStatus = "ENABLE";
                musicButton.setStatus("MUSIC" + " : " + musicStatus);
            } else {
                GameManagement.MUSIC = false;
                musicStatus = "DISABLE";
                musicButton.setStatus("MUSIC" + " : " + musicStatus);
            }
        });

        menu0.getChildren().addAll(playButton, optionButton, exitButton);
        menu1.getChildren().addAll(soundButton, musicButton, backButton);


        Rectangle bg = new Rectangle(GameManagement.CANVAS_WIDTH, GameManagement.CANVAS_HEIGHT);
        bg.setFill(Color.GREY);
        bg.setOpacity(0.2);

        getChildren().addAll(imageView, bg, menu0);
    }
}