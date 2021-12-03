package com.first.view;

import com.first.client.Client;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author 原初
 * @create 2021 - 11 - 30
 */
public class ConsoleView implements AbsClientView{
    Consumer<String> printWay = System.out::println;
    Supplier<String> inputWay = new Scanner(System.in)::nextLine;
    Client client;
    @Override
    public void start() {
        new Thread(client::receive).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!client.isThisIsColse())
                {
                    client.say(inputWay.get());
                }
            }
        }).start();
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Client getClient() {
        return this.client;
    }

    @Override
    public Consumer<String> getPrintWay() {
        return this.printWay;
    }

    @Override
    public Supplier<String> getInputWay() {
        return this.inputWay;
    }
}
