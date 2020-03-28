package com.ccb.netty.server;

import java.io.IOException;

public class TestServer {
    public static void main(String[] args) throws IOException {
        new Thread(new ChatRoomServer()).start();
    }
}
