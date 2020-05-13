package com.jason.bootswagger2.classloader.netclassloader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class CalculatorTest {

    public static void main(String[] args) {
        new CalculatorTest().testClassIdentity2();
    }

    public void testClassIdentity1(){
        String url = "http://192.168.80.40:9090/my/b.jar!";//"http://localhost:8080/ClassloaderTest/classes";
        NetworkClassLoader ncl = new NetworkClassLoader(url);
        String basicClassName = "service.CalculatorBasic";
        String advancedClassName = "service.CalculatorAdvanced";
        try {

            Class<?> clazz = ncl.loadClass(basicClassName);  // 加载一个版本的类
            ICalculator calculator = (ICalculator) clazz.newInstance();  // 创建对象
            System.out.println(calculator.getVersion());

            clazz = ncl.loadClass(advancedClassName);  // 加载另一个版本的类
            calculator = (ICalculator) clazz.newInstance();
            System.out.println(calculator.getVersion());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void testClassIdentity2() {

        String basicClassName = "service.CalculatorBasic";
        String advancedClassName = "service.CalculatorAdvanced";
        URL[] urls = new URL[0];
        try {
            //本地文件
            File jarFile = new File("C:\\Users\\Administrator\\Desktop\\class\\a.jar");
            urls = new URL[] { jarFile.toURI().toURL() };
            //网络文件
            //urls = new URL[] { new URL("http://192.168.80.40:9090/my/b.jar") };
            URLClassLoader ucl = new URLClassLoader(urls);
            Class<?> clazz = ucl.loadClass(basicClassName);  // 加载类

            ICalculator calculator = (ICalculator) clazz.newInstance();  // 创建对象
            System.out.println(calculator.getVersion());
            System.out.println(calculator.calculate("CalculatorBasic"));


            clazz = ucl.loadClass(advancedClassName);  // 加载另一个版本的类
            calculator = (ICalculator) clazz.newInstance();
            System.out.println(calculator.getVersion());
            System.out.println(calculator.calculate("CalculatorAdvanced"));


            clazz = ucl.loadClass("com.jason.bootswagger2.Bootswagger2Application");
            Object ob = clazz.newInstance();
            Method m = clazz.getDeclaredMethod("main",String[].class);
            m.invoke(ob,(Object)new String[]{});

             /*
            clazz = ucl.loadClass("service.Main");
            Object ob = clazz.newInstance();
            Method m = clazz.getDeclaredMethod("main",String[].class);
            m.invoke(ob,(Object)new String[]{});
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
