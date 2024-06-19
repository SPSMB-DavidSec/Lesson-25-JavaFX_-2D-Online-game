package cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final int SCREEN_WIDTH = 1900;
    public static final int SCREEN_HEIGHT = 980;

    GraphicsContext gc;
    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setTitle("Online Game");
        stage.setScene(scene);
        stage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (now - lastTick > 10000) {
                    lastTick = now;
                    tick();
                }
            }
        };
        animationTimer.start();
    }

    private void tick() {
    }

    public static void main(String[] args) {
        launch();
    }
}