package com.first.server;

import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author 原初
 * @create 2021 - 11 - 04
 * @version 0.0.2 对内容的重构
 */
public class CoreServer extends Thread implements Serializable{
    static final long serialVersionUID = -85120105L;
    static private Consumer<String> errMsg = System.err::println;
    public static void setErrMsg(Consumer<String> out)
    {
        CoreServer.errMsg = out;
    }
    private Socket thisSocket;
    private String name;
    private String content;//数据包想要传达的某些数据
    ObjectInputStream input;
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
        AbsDataPack<?> startDate = null;
        try {
            input = new ObjectInputStream(thisSocket.getInputStream());
            startDate = (AbsDataPack<?>) input.readObject();
        } catch (Exception e) {
            errMsg.accept(e.getMessage());
        }
        if(startDate != null && startDate.getDataType() == AbsType.START)
        {
            this.name  = startDate.getName();
        }
        else
        {
            isClose = true;
            try {
                if(input != null)
                {
                    input.close();
                }
            } catch (IOException e) {
                errMsg.accept(e.getMessage());
            }

        }
    }
    public void closeSocket()
    {
        isClose = true;
    }
    class MyObjectOutputStream  extends ObjectOutputStream{

        public MyObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        public void writeStreamHeader() throws IOException{
            return;
        }
    }
    @Override
    public void run() {
        while (!isClose)
        {
            AbsDataPack dateCon = null;
            try {
                dateCon = (AbsDataPack) input.readObject();
            } catch (Exception e) {
                try{
                    thisSocket.sendUrgentData(0xff);
                }
                catch (IOException es)
                {
                    try {
                        thisSocket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    isClose = true;
                    others.remove(this);
                    break;
                }
            }
            try
            {
                int cnt = 0;
                for (int i = 0; i < others.size(); i++)
                {
                    CoreServer temp = others.get(i);
                    //System.out.println(dateCon);
                    if (this == temp)
                    {
                        continue;
                    }
                    ObjectOutputStream output = null;
                    if(cnt == 0)
                    {
                        output = new ObjectOutputStream(temp.getThisSocket().getOutputStream());
                        cnt++;
                    }
                    else
                    {
                        output = new MyObjectOutputStream(temp.getThisSocket().getOutputStream());
                    }
                    output.writeObject(dateCon);
                }
            }
            catch (Exception e)
            {
                continue;
            }
        }
    }
}
