package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;

import java.io.*;
import java.util.ArrayList;

/**
 * @author 原初
 * @create 2021 - 11 - 30
 */
@ClientPlug(name = "serverPriPlug")
public class AAASerPriPlug extends ClientNormalPlug{
    String proDir;
    public AAASerPriPlug()
    {
        super();
        proDir = "/serPri_dir";
    }
    @Override
    public void whenInit(Client thisCli) {
        ArrayList<AbsClientPlug> tempPlugs = thisCli.getTempPlug();
        File props = new File(System.getProperty("user.dir") + proDir + "/pri.properties");
        if(!props.exists())
        {
            File propdir = new File(System.getProperty("user.dir") + proDir);
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
                        new InputStreamReader(new FileInputStream(props))
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
                if(!getPlugName().equals(temp.get(0).getPlugName()))
                    temp.get(0).whenInit(thisCli);
            } catch (IOException e) {
                e.printStackTrace();
            }
            thisCli.setTempPlug(temp);
        }
    }
}
