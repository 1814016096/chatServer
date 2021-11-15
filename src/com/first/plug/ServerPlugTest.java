package com.first.plug;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

/**
 * @author 原初
 * @create 2021 - 11 - 10
 */
public class ServerPlugTest {
    @Test
    public void castTest()
    {
        AbsServerPlug s = new NormalPlug();
        System.out.println(s.getCtrlplugType());//ok
        Type genericInterfaces = s.getClass().getGenericSuperclass();
        System.out.println(genericInterfaces.getTypeName());//ok
    }
}
