package com.first.server;

import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;
import com.first.plug.server.AbsServerPlug;
import com.first.plugloader.PlugProcess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author 原初
 * @create 2021 - 11 - 04
 * @version 0.1.0 一个支持插件的核心Server
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
    ArrayList<? extends AbsServerPlug> plugs;
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
    private Map<CoreServer, ObjectOutputStream> allOut;

    public Map<CoreServer, ObjectOutputStream> getAllOut() {
        return allOut;
    }

    private void addOut()
    {
        synchronized (this.allOut)
        {
            try {
                allOut.put(this, new ObjectOutputStream(this.getThisSocket().getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public CoreServer(Socket thisSocket, ArrayList<? extends AbsServerPlug> plugs
            , Map<CoreServer, ObjectOutputStream> allOut) {
        this.plugs = plugs;
        this.allOut = allOut;
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
        addOut();
    }
    public void closeSocket()
    {
        isClose = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoreServer that = (CoreServer) o;
        if (thisSocket != null ? !thisSocket.equals(that.thisSocket) : that.thisSocket != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (input != null ? !input.equals(that.input) : that.input != null) return false;
        return plugs != null ? plugs.equals(that.plugs) : that.plugs == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (input != null ? input.hashCode() : 0);
        return result;
    }
    void whenDisconnection()
    {
        for(var plug : plugs)
        {
            plug.whenClientDisconnection(this);
        }
        isClose = true;
        try {
            thisSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        others.remove(this);
        allOut.remove(this);
        return;
    }
    @Override
    public void run() {
        while (!isClose)
        {
            AbsDataPack dateCon = null;
            try {
                dateCon = (AbsDataPack) input.readObject();
                if(dateCon.getDataType() == AbsType.CLOSE)
                {
                    whenDisconnection();
                    break;
                }
            } catch (Exception e) {
                try{
                    thisSocket.sendUrgentData(0xff);
                }
                catch (IOException es)
                {
                    whenDisconnection();
                    break;
                }
            }
            ArrayList<? extends AbsServerPlug> enable =
                    PlugProcess.judgeLegalPlugByServer(dateCon, this.plugs);
            if(enable == null){
                dateCon.setDataType(AbsType.CHAT);
                dateCon.setStartWith("");
                enable = PlugProcess.judgeLegalPlugByServer(dateCon, this.plugs);
            }
            for(var plug : enable)
            {
                plug.setGettedPack(dateCon);
                dateCon = plug.afterInput(this);
            }
            try
            {
                int cnt = 0;
                for (int i = 0; i < others.size(); i++)
                {
                    CoreServer temp = others.get(i);
                    ObjectOutputStream output = null;
                    output = allOut.get(temp);
                    boolean isWriter = true;
                    for(var plug : enable)
                    {
                        if(!(isWriter = plug.filter(temp ,others)))
                        {
                            break;
                        }
                    }
                    if(isWriter)
                    {
                        for(var plug : enable)
                        {
                            plug.beforeWrite(this);
                        }
                        output.writeObject(dateCon);
                        cnt++;
                        for(var plug : enable)
                        {
                            plug.afterWriter(this);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                continue;
            }
        }
    }
}
