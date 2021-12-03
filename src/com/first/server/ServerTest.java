package com.first.server;

/**
 * @author 原初
 * @create 2021 - 11 - 26
 */
public class ServerTest {
//    @Test
//    public void testManager()
//    {
//        ServerManager serm = new ServerManager();
//        serm.addCnt();
//    }

    public static void main(String[] args) {
        ServerManager serm = new ServerManager();
        while (true)
        {
            serm.addCnt();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
