package com.jason.bootswagger2.classloader;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class B extends ClassLoader{
    public static void main(String[] args) {
        try {
            Class clzz = new B().loadClass("com.jason.bootswagger2.classloader.A");
            //Class clzz = Class.forName("com.jason.bootswagger2.classloader.A");
            Object ob = clzz.newInstance();
            Method m = clzz.getDeclaredMethod("main",String[].class);
            String[] strings = new String[]{"1"};
            m.invoke(ob,(Object) strings);



            URL[] extURLs = ((URLClassLoader) ClassLoader.getSystemClassLoader().getParent()).getURLs();
            for (int i = 0; i < extURLs.length; i++) {
                System.out.println(extURLs[i]);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
