package com.ccb.netty.client;

import java.io.IOException;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) throws IOException {
        ChatRoomClient client = new ChatRoomClient();
        Thread thread = new Thread(client);
        thread.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            client.send(msg);
            if("exit".equals(msg)){
                System.out.println("下线了.....");
                break;
            }
        }
        //之前在close方法中调用Thread.currentThread().interrupt()，
        // 一直都没有正常停止线程，其实调用close方法的是main线程，
        // 而并不是client的线程，所以关闭一直不成功
        thread.interrupt();
        client.close();
        scanner.close();
    }
}
