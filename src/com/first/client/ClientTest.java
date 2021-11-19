package com.first.client;

import com.first.plug.client.AbsClientPlug;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author 原初
 * @create 2021 - 11 - 17
 */
public class ClientTest {
    @Test
    public void temptest()
    {
        Client client = new Client("1.1.1.1");
        ArrayList<AbsClientPlug> tempPlug = client.getTempPlug();
        System.out.println("==================");
        for(var i : tempPlug)
        {
            System.out.println(i.getPlugName());
        }

    }
}
