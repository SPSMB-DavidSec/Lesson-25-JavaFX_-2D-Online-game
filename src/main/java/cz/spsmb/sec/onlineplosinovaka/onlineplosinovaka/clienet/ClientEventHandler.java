package cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.clienet;

import javafx.application.Application;

public abstract class ClientEventHandler extends Application {

    public void onMessageReceived(Message message) {
        System.out.println("New message received: " + message.getContentAsString());
    }


}
