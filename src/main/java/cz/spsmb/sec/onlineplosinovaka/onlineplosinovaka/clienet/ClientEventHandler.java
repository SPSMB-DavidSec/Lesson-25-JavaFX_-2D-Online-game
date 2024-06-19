package cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.clienet;

public abstract class ClientEventHandler {

    public void onMessageReceived(Message message) {
        System.out.println("New message received: " + message.getContentAsString());
    }


}
