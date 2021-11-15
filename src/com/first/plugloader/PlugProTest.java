package com.first.plugloader;

import com.first.datapack.ChatDataPack;
import com.first.plug.AbsServerPlug;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @author 原初
 * @create 2021 - 11 - 10
 * 对插件加载的测试
 */
public class PlugProTest {
    @Test
    public void getPlugTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        PlugProcess p = new PlugProcess();
//        ArrayList<Class<? extends AbsServerPlug<?>>> allPathPlug = p.getAllPathPlug("D:\\IdeaWorkspace\\day6\\out\\artifacts\\chatServe_jar3\\" +
//                "chatServe.jar");
//        for(var a : allPathPlug)
//        {
//            if(!a.getName().contains("Abs"))
//            {
//                AbsServerPlug<?> absServerPlug = a.newInstance();
//                Method m = a.getMethod("filter", CoreServer.class);
//                System.out.println(m.invoke(absServerPlug, new CoreServer(new Socket())));//ok
//            }
//        }
        ArrayList<Class<? extends AbsServerPlug<?>>> al = p.getAllPathPlug("D:\\IdeaWorkspace\\" +
                "day6\\out\\artifacts\\chatServe_jar3\\chatServe.jar");
        ArrayList<Class<? extends AbsServerPlug<?>>> c = p.judgeLegalPlug(new ChatDataPack(), al);
        if(c != null)
        {
            for(var d : c)
            {
                System.out.println(d.getName());
            }
        }
        else
        {
            System.out.println("null");
        }

    }
}
