package com.first.client;

import com.first.datapack.AbsDataPack;
import com.first.plug.AbsPlug;
import com.first.plug.AbsType;
import com.first.plug.client.AbsClientPlug;
import com.first.plugloader.PlugProcess;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 原初
 * @create 2021 - 11 - 04
 * @version 0.0.1
 */
public class Client {
    ArrayList<Class<? extends AbsPlug<?>>> clientPlugs;
    ArrayList<AbsClientPlug> tempPlug;
    private boolean thisIsColse = false;
    private Socket cntSot;
    private String name;
    private boolean isConnect;
    private String ip;
    private String whatCharSet = "gbk";

    public String getWhatCharSet() {
        return whatCharSet;
    }

    public ArrayList<AbsClientPlug> getTempPlug() {
        return tempPlug;
    }

    //还没测试
    private Client(){
        File plugDir = new File("clientplug");
        clientPlugs = new ArrayList<>(50);
        File[] plugs = plugDir.listFiles();
        for(var plug : plugs)
        {
            if(plug.getName().endsWith(".jar"))
            {
                clientPlugs.addAll(PlugProcess.getAllPathPlug(plug.getAbsolutePath(),"ClientPlug"));
            }
        }
        tempPlug = new ArrayList<>(50);
        for(var plugClass : clientPlugs)
        {
            AbsClientPlug clientPlug = null;
            try {
                clientPlug = (AbsClientPlug) plugClass.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            tempPlug.add(clientPlug);
        }
        for(int i = 0; i < tempPlug.size(); i++)
        {
            var inst = tempPlug.get(i);
            inst.whenInit(this);
        }
        if(tempPlug.size() != clientPlugs.size())
        {
            throw new RuntimeException("插件初始化出现错误!");
        }
    }

    public void setTempPlug(ArrayList<AbsClientPlug> tempPlug) {
        this.tempPlug = tempPlug;
    }

    //close未测试
    public void close() {
        AbsDataPack<String> normal  = new AbsDataPack<>();
        normal.setDataType(AbsType.CLOSE);
        try {
            cntSot.sendUrgentData(0xff);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        if(cntSot.isConnected())
        {
            for(var inst : tempPlug)
            {
                inst.beforeClose(normal, this);
            }
            OutputStream outputStream = null;
            ObjectOutputStream out = null;
            try {

                outputStream = cntSot.getOutputStream();
                out = new ObjectOutputStream(outputStream);
                out.writeObject(normal);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                thisIsColse = true;
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (cntSot != null && !cntSot.isClosed()) {
                    try {
                        cntSot.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.exit(0);
    }
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
        this();
//        try {
//            this.cntSot = new Socket(InetAddress.getByName(ip),12221);
//            isConnect = true;
//            this.ip = ip;
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("连接失败");
//            isConnect = false;
//        }
    }
    public List<String> split(String statement)
    {
        String[] temp = statement.split(" ");
        List<String> collect = Arrays.stream(temp).filter(x -> !("".equals(x) || " ".equals(x))).collect(Collectors.toList());
        return collect;

    }
    public String merge(List<String> fragrement)
    {
        StringBuffer temp = new StringBuffer(20);
        for(var ele : fragrement)
        {
            temp.append(ele);
        }
        return temp.toString();//ok
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
