package com.first.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author 原初
 * @create 2021 - 11 - 11
 */
public class JUnitObjTest {
    @Test
    public void OutPuttest() throws IOException {
    }
    @Test
    public void InpTest() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        ObjectInputStream obj = new ObjectInputStream(new FileInputStream("b.test"));
//        ObjectInputStream obj1 = new ObjectInputStream(new FileInputStream("a.test"));
//        Object o1 = obj1.readObject();
//        Class<?> aClass = o1.getClass();
//        System.out.println(aClass.getName());
        //ObjectTestSuper t = (ObjectTestSuper)obj.readObject();
        //t.call();
        //转换思路

        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{
                new URL("file:D:\\IdeaWorkspace\\day6\\out\\artifacts\\chatServe_jar3\\chatServe.jar")});
        Class<?> aClass = urlClassLoader.loadClass("com.first.test.PersonTest");
        Constructor<?> constructor = aClass.getConstructor();
        ObjectTestSuper s = (ObjectTestSuper) constructor.newInstance();
        s.call();
        //成功了
        JarFile j = new JarFile("D:\\IdeaWorkspace\\day6\\out\\artifacts\\chatServe_jar3\\chatServe.jar");
        Enumeration<JarEntry> entries = j.entries();
        while(entries.hasMoreElements())
        {
            String xs = entries.nextElement().getRealName();
            if(xs != null && !xs.isEmpty() && xs.endsWith(".class"))
            {
                Class<?> aClass1 = urlClassLoader.loadClass(xs.replace("/", ".").substring(0,xs.length() - 6));
                Annotation[] annotations = aClass1.getAnnotations();
                for(Annotation a : annotations)
                {
                    if(a.toString().contains("ServerPlug"))
                    {
                        System.out.println(aClass1);//ok
                    }
                }
            }
        }
    }
}
