package uet.oop.bomberman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BombermanGame extends Application {
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Start the game
        GameManagement.init();
        Scene scene = GameManagement.getScene();
        stage.setScene(scene);
        stage.setTitle("Bomberman.exe");
        stage.show();
    }
}