package com.first.client;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * @author 原初
 * @create 2021 - 11 - 17
 */
public class ClientTest {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Client client = new Client("1.1.1.1", System.out::println, scan::nextLine);
        //SetName.setType(System.out::println, () -> scan.next());
        client.say("/plug");
    }
    @Test
    public void temptest()
    {
    }
}
