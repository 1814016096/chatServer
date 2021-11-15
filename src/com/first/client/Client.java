package com.first.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author 原初
 * @create 2021 - 11 - 04
 * @version 0.0.1
 */
public class Client {
    private Socket cntSot;
    private String name;
    private boolean isConnect;
    private String ip;
    private String whatCharSet = "gbk";
    public void receive() {
        try {
            InputStreamReader inputStream =new InputStreamReader(cntSot.getInputStream(),whatCharSet);
            char[] c= new char[10];
            int len = 0;
            while((len = inputStream.read(c)) != -1)
            {
                System.out.print(new String(c, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Socket getCntSot() {
        return cntSot;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public Client(String ip) {
        try {
            this.cntSot = new Socket(InetAddress.getByName(ip),12221);
            isConnect = true;
            this.ip = ip;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接失败");
            isConnect = false;
        }
    }
    public void say(String str) {
        try
        {
            OutputStream outputStream = cntSot.getOutputStream();
            outputStream.write(("<"  + name + ">:"  + str).getBytes(whatCharSet));
            outputStream.write((byte)127);
            System.out.println("<"  + name  + ">:" + str);
        } catch (IOException e) {
            try {
                cntSot.sendUrgentData(0xff);
            } catch (IOException ioException) {
                System.out.println("服务端崩了，程序尝试重连");
                try {
                    cntSot = new Socket(ip,12221);
                    if(cntSot.isConnected())
                    {
                        System.out.println("连接成功");
                        cntSot.getOutputStream().write(name.getBytes(whatCharSet));
                        cntSot.getOutputStream().write(127);
                    }
                } catch (IOException exception) {
                    System.out.println("连接失败，服务器真的崩了");
                    System.exit(-11);
                }
            }
        }
    }
}
