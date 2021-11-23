package com.first.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * @author 原初
 * @create 2021 - 11 - 05
 * @version 0.0.1
 */
public class ClientView {
    Client client;
    private String whatCharSet = "gbk";
    public ClientView() {
        //this.client = new Client("110.42.191.58");
        if(client.isConnect())
        {
            System.out.println("连接成功!!!请输入你心仪的名字吧：");
            Scanner scan = new Scanner(System.in);
            this.client.setName(scan.next());
            System.out.println("输入成功,你的名字叫:" + client.getName());
        }
    }
    public static void main(String[] args) {
        ClientView clientView = new ClientView();
        OutputStream outputStream = null;
        try {
            outputStream = clientView.client.getCntSot().getOutputStream();
            outputStream.write((clientView.client.getName()).getBytes(clientView.whatCharSet));
            outputStream.write((byte)127);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread receSer = new Thread(new Runnable() {
            @Override
            public void run() {
                clientView.client.receive();
            }
        });
        receSer.setName("receive");
        receSer.start();
        Thread heart = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try {
                        clientView.client.getCntSot().sendUrgentData(0xFF);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        while(true)
        {
            Scanner scan = new Scanner(System.in);
            clientView.client.say(scan.next());
        }
    }
}
