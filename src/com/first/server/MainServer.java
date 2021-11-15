package com.first.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author 原初
 * @create 2021 - 11 - 04
 * @version 0.0.1
 */

//class RemoveSocket extends Thread
//{
//    Collection<CoreServer> whatServer;
//    public RemoveSocket(Collection<CoreServer> whatServer)
//    {
//        this.whatServer = whatServer;
//    }
//    @Override
//    public void run() {
//        while(true)
//        {
//            Iterator<CoreServer> it = whatServer.iterator();
//            while(it.hasNext())
//            {
//                CoreServer temp = it.next();
//                if(!temp.getThisSocket().isConnected())
//                {
//                    temp.closeSocket();
//                    it.remove();
//                }
//            }
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}

public class MainServer {
    ArrayList<CoreServer> all;
    ServerSocket sersoc;//服务器socket
    Socket soc;//服务器自身作为客户端的socket
    //RemoveSocket res;
    private String whatCharSet = "gbk";
    public MainServer(){
        try {
            sersoc = new ServerSocket(12221);
            soc = new Socket(InetAddress.getLoopbackAddress(), 12221);
            if(soc.isConnected())
            {
                all = new ArrayList<>();
                CoreServer.setOthers(all);
                all.add(new CoreServer(sersoc.accept()));
                //res = new RemoveSocket(all);
                //res.start();//开始监控
                all.get(0).setName("server");
                all.get(0).start();
                all.get(0).setClientName("server");
            }
            else
            {
                throw new RuntimeException("连接错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void say(String str){
        try
        {
            System.out.println("<server>:" + str);
            OutputStream out = soc.getOutputStream();
            out.write(( "<server>:" + str).getBytes(whatCharSet));
            out.write((byte)127);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void receive(){
        try {
            InputStreamReader ir = new InputStreamReader(soc.getInputStream(),whatCharSet);
            char[] c = new char[10];
            int len = 0;
            while((len = ir.read(c)) != -1) {
                String str = new String(c, 0, len);
                System.out.print(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sayNum()
    {
        say("聊天室有" + all.size() + "人" + "分别为:");
        StringBuffer tempName = new StringBuffer(20);
        for(CoreServer s : all)
        {
            tempName.append(s.getClientName() + "\n");
        }
        say(tempName.toString());
    }
    public void addCnt() {
        Socket accept = null;
        ArrayList<Byte> names = new ArrayList<>(10);
        Byte[] nameArr = null;
        try {
            accept = sersoc.accept();
            InputStream input =accept.getInputStream();
            byte name;
            OutputStream outputStream = soc.getOutputStream();
            while ((name = (byte)input.read()) != 127)
            {
                names.add(name);
            }
            nameArr = new Byte[names.size()];
            names.toArray(nameArr);
            for(byte i : nameArr)
            {
                outputStream.write(i);
            }
            outputStream.write("进入原初聊天室".getBytes(whatCharSet));
            outputStream.write((byte)127);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CoreServer client = new CoreServer(accept);
        byte[] temp = new byte[nameArr.length];
        int i = 0;
        for(byte t : nameArr)
        {
            temp[i] = t;
            i++;
        }
        String nameStr = null;
        try {
            nameStr = new String(temp, whatCharSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.setClientName(nameStr);
        all.add(client);//加入事件
        sayNum();
        client.start();
    }
    public static void main(String[] args) {
        MainServer mainServer = new MainServer();
        Thread accept = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mainServer.addCnt();//监听
                }
            }
        });
        accept.setName("accept");
        accept.start();

        Thread receSer = new Thread(new Runnable() {
            @Override
            public void run() {
                mainServer.receive();
            }
        });
        receSer.setName("receive");
        receSer.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    mainServer.sayNum();
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        while (true)
        {
            Scanner scan = new Scanner(System.in);
            mainServer.say(scan.next());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
