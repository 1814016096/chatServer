package com.first.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author 原初
 * @create 2021 - 11 - 04
 * @version 0.0.2 对内容的重构
 */
public class CoreServer extends Thread{
    private Socket thisSocket;
    private String name;
    private String content;//数据包想要传达的某些数据
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getClientName() {
        return name;
    }
    public void setClientName(String name) {
        this.name = name;
    }
    private static ArrayList<CoreServer> others;
    public Socket getThisSocket() {
        return thisSocket;
    }
    public Collection<CoreServer> getOthers() {
        return others;
    }
    public static void setOthers(ArrayList<CoreServer> others)
    {
        CoreServer.others = others;
    }
    boolean isClose = false;
    public CoreServer(Socket thisSocket) {
        this.thisSocket = thisSocket;
        this.others = others;
    }
    public void closeSocket()
    {
        isClose = true;
    }
    @Override
    public void run() {
        while (!isClose)
        {
            OutputStream out = null;
            InputStream inputStream = null;
            try{
                thisSocket.sendUrgentData(0xff);
            }
            catch (IOException e)
            {
//                try {
////                    OutputStream tempout = others.get(0).getThisSocket().getOutputStream();
////                    tempout.write("\\l ".getBytes("gbk"));
////                    tempout.write(this.name.getBytes("gbk"));
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
                try {
                    thisSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                isClose = true;
                others.remove(this);
                break;
            }
            try {
                inputStream = thisSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int what = 0;
            ArrayList<Integer> context = new ArrayList<>(10);
            try
            {
                while ((what = inputStream.read()) != 127)
                {
                    context.add(what);
                }
                for (int i = 0; i < others.size(); i++)
                {
                    CoreServer temp = others.get(i);
                    if (this == temp)
                    {
                        continue;
                    }
                    out = temp.getThisSocket().getOutputStream();
                    for(int x : context)
                    {
                        out.write(x);
                    }
                    out.write("\n".getBytes());
                    out.flush();
                }
            }
            catch (IOException e)
            {
                continue;
            }
            context.clear();
        }
    }
}
