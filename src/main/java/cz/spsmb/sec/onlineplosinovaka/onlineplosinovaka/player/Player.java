package cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.player;

import cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.map.Rectangle;
import javafx.scene.paint.Color;

public class Player {

    int x;
    int y;
    int velocityX;
    int velocityY;
    int width;
    int height;
    boolean isGrounded;
    Color color = Color.RED;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.height = 20;
        this.velocityX = 0;
        this.velocityY = 10;
    }
    public Player(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 10;
    }

    public void gravitation(){
        if(isGrounded){
            velocityY = 0;
            return;
        }
        velocityY += 0.7;
    }

    public void downMovement(){
        this.y += velocityY;
    }
    public void upMovement(){
        this.y -= velocityY;
    }
    public void leftMovement(){
        this.x += velocityX;
    }
    public void rightMovement(){
        this.x -= velocityX;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }
}
