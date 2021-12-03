package com.first.client;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * @author 原初
 * @create 2021 - 11 - 17
 *
 */
public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        Scanner scan = new Scanner(System.in);
        Client client = new Client("127.0.0.1", System.out::println, scan::nextLine);
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.receive();
            }
        }).start();
        client.say("/plug");
        client.say("/num");
        client.say("我超");
            //SetName.setType(System.out::println, () -> scan.next());
            //client.say("/plug");
    }
    @Test
    public void temptest()
    {
        System.out.println(System.getProperty("user.dir"));
    }
}
