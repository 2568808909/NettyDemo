package com.ccb.netty.client;

import java.io.IOException;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) throws IOException {
        ChatRoomClient client = new ChatRoomClient();
        new Thread(client).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            client.send(msg);
            if("exit".equals(msg)){
                System.out.println("下线了.....");
                break;
            }
        }
        client.close();
        scanner.close();
    }
}
