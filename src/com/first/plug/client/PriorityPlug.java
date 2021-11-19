package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.plug.AbsType;

import java.io.*;
import java.util.ArrayList;

/**
 * @author 原初
 * @create 2021 - 11 - 19
 * @version 0.0.2 为插件排序
 */
@ClientPlug(name = "priorityPlug")
public class PriorityPlug extends ClientNormalPlug {
    public PriorityPlug()
    {
        setSomting("null", "一个为插件排序的插件", AbsType.CORE);
        isLocal = true;
    }
    @Override
    public void whenInit(Client thisCli) {
        ArrayList<AbsClientPlug> tempPlugs = thisCli.getTempPlug();
        File props = new File(System.getProperty("user.dir") + "/properties_dir/pri.properties");
        if(!props.exists())
        {
            File propdir = new File(System.getProperty("user.dir") + "/properties_dir");
            propdir.mkdirs();
            try(OutputStream out = new FileOutputStream(props)){

                int i = 1;
                for(var plug : tempPlugs)
                {
                    out.write((plug.getPlugName() + "\n").getBytes(thisCli.getWhatCharSet()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            ArrayList<AbsClientPlug> temp = new ArrayList<>(tempPlugs.size());
            try(InputStreamReader input =
                        new InputStreamReader(new FileInputStream(props));
            )
            {
                BufferedReader bufferIn = new BufferedReader(input);
                String plugName = null;
                while((plugName = bufferIn.readLine()) != null)
                {
                    for(var ele : tempPlugs)
                    {
                        if(plugName.equals(ele.getPlugName()))
                        {
                            temp.add(ele);
                            tempPlugs.remove(ele);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            thisCli.setTempPlug(temp);
        }
    }
}
