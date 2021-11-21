package com.first.client;

import com.first.plug.client.SetName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * @author 原初
 * @create 2021 - 11 - 17
 */
public class ClientTest {
    public static void main(String[] args) {
        Client client = new Client("1.1.1.1");
        Scanner scan = new Scanner(System.in);
        SetName.setType(System.out::println, () -> scan.next());
        client.say("woc");
    }
    @Test
    public void temptest()
    {
        Client client = new Client("1.1.1.1");
        //System.out.println(client.AnylizeType("woc!"));
        //System.out.println(client.matchPlug("/plug"));
    }
}
