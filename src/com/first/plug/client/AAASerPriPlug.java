package com.first.plug.client;

import com.first.Ann.ClientPlug;

/**
 * @author 原初
 * @create 2021 - 11 - 30
 */
@ClientPlug(name = "serverPriPlug")
public class AAASerPriPlug extends AAAAPriorityPlug{
    public AAASerPriPlug()
    {
        super();
        proDir = "/serPri_dir";
    }
}
