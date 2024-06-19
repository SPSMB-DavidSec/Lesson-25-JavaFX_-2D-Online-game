package cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka;

import cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.clienet.ClientEventHandler;
import cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.clienet.Message;
import cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.clienet.MqttClient;
import cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.map.Platform;
import cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.player.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HelloApplication extends ClientEventHandler {
    public static final int SCREEN_WIDTH = 1900;
    public static final int SCREEN_HEIGHT = 980;
    boolean isAnyMasterServer;

    GraphicsContext gc;
    MqttClient client = new MqttClient();

    List<Platform> platforms = new ArrayList<>();
    Map<String, Player> players = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        VBox root = new VBox();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setTitle("Online Game");
        stage.setScene(scene);
        stage.show();
        client.connect();
        client.subscribe(this);
        client.sendMessage(new Message(("Hello, I am " + MqttClient.CLIENT_ID).getBytes(StandardCharsets.UTF_8)));
        client.sendMessage("[PING] " + MqttClient.CLIENT_ID);
        Thread.sleep(5000);

        if (!isAnyMasterServer) {
            generateMap();
        }
        players.put(MqttClient.CLIENT_ID, new Player(100, 500, 20, 20));


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
        clearScreen();
        gc.setFill(Color.BLACK);
        for (Platform p : platforms) {
            gc.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
        }
        for (Map.Entry<String, Player> playerEntry : players.entrySet()) {
            String playerId = playerEntry.getKey();
            Player player = playerEntry.getValue();

            gc.setFill(player.getColor());
            gc.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
            if (playerId.equals(MqttClient.CLIENT_ID)){
                sendMyPosition(player);
            }
        }
    }



    public static void main(String[] args) {
        launch();
    }

    @Override
    public void onMessageReceived(Message message) {
        System.out.println("<---" + message.getContentAsString());
        if (message.getContentAsString().startsWith("[PING]")) {
            if (message.getContentAsString().equals("[PING] " + MqttClient.CLIENT_ID)) {
                return;
            }
            client.sendMessage("[PONG] " + MqttClient.CLIENT_ID);
            sendMap();
        }

        if (message.getContentAsString().startsWith("[PONG]")) {
            isAnyMasterServer = true;
        }

        if (message.getContentAsString().startsWith("[MAP]") && isAnyMasterServer) {
            //[MAP] [{0,880,800,20},{810,770,400,20},{1200,690,400,20}]
            platforms = parsePlatforms(message.getContentAsString());
        }

        if (message.getContentAsString().startsWith("[PLAYER_POS]")) {
            //[PLAYER_POS] David Šec,0xff0000ff,100,500
            updatePlayerPosition(message.getContentAsString());
        }

    }

    private void clearScreen() {
        gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void generateMap() {
        Platform platform1 = new Platform(0, 880, 20, 800);
        Platform platform2 = new Platform(810, 770, 20, 400);
        Platform platform3 = new Platform(1200, 690, 20, 400);
        platforms.add(platform1);
        platforms.add(platform2);
        platforms.add(platform3);
    }

    private void sendMap() {
        StringBuilder sb = new StringBuilder();
        sb.append("[MAP] ");
        for (int i = 0; i < platforms.size(); i++) {
            Platform p = platforms.get(i);
            sb.append(String.format("{%s,%s,%s,%s}", p.getX(), p.getY(), p.getHeight(), p.getWidth()));
            if (i < platforms.size() - 1) {
                sb.append(";");
            }
        }
        client.sendMessage(sb.toString());
    }

    public static List<Platform> parsePlatforms(String platformsString) {
        List<Platform> platforms = new ArrayList<>();
        platformsString = platformsString.substring(6);
        String[] platformsArray = platformsString.split(";");
        for (String s : platformsArray) {
            s = s.substring(1, s.length() - 1);
            String[] coordinates = s.split(",");
            Integer[] integers = Arrays.stream(coordinates).map(Integer::parseInt).toArray(Integer[]::new);
            if (integers.length == 4) {
                platforms.add(new Platform(integers[0], integers[1], integers[2], integers[3]));
            }

        }
        return platforms;
    }

    private void updatePlayerPosition(String contentAsString) {
        ////[PLAYER_POS] David Šec,0xff0000ff,100,500
        contentAsString = contentAsString.substring(13);
        String[] strArray = contentAsString.split(",");
        String playerId= strArray[0];
        Color color = Color.valueOf(strArray[1]);
        Integer posX = Integer.parseInt(strArray[2]);
        Integer posY = Integer.parseInt(strArray[3]);

        if (players.containsKey(playerId)){
            Player p = players.get(playerId);
            p.setX(posX);
            p.setY(posY);
            p.setColor(color);
        }
        else {
            Player player = new Player(posX, posY);
            player.setColor(color);
            players.put(playerId, player);
        }

    }

    private void sendMyPosition(Player player) {
        client.sendMessage("[PLAYER_POS] " + MqttClient.CLIENT_ID + "," + player.getColor().toString() + "," + player.getX() + "," + player.getY());
    }
}