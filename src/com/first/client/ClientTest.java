package com.first.client;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * @author 原初
 * @create 2021 - 11 - 17
 */
public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        Scanner scan = new Scanner(System.in);
        Client client = new Client("127.0.0.1", System.out::println, scan::nextLine);
        client.say("/num");
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.receive();
            }
        }).start();
        while (true) {
            client.say("/num");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //SetName.setType(System.out::println, () -> scan.next());
            //client.say("/plug");
        }
    }
    @Test
    public void temptest()
    {
        System.out.println(System.getProperty("user.dir"));
    }
}
