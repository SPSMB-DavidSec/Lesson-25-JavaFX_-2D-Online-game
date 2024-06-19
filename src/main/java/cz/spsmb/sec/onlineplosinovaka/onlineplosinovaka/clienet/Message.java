package cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka.clienet;

import java.nio.charset.StandardCharsets;

public class Message {

    private byte[] content;

    public Message(byte[] payloadAsBytes) {
        this.content = payloadAsBytes;
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentAsString() {
        return new String(content);
    }

    public void setContent(String mesasge) {
        this.content = mesasge.getBytes(StandardCharsets.UTF_8);
    }


}