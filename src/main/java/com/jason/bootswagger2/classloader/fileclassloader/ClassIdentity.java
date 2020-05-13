package com.jason.bootswagger2.classloader.fileclassloader;

import com.jason.bootswagger2.classloader.netclassloader.ICalculator;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassIdentity {

    public static void main(String[] args) {
        //new ClassIdentity().testClassIdentity1();
    }

    public void testClassIdentity1() {
        String classDataRootPath = "C:\\Users\\Administrator\\Desktop\\class";
        FileSystemClassLoader fscl1 = new FileSystemClassLoader(classDataRootPath);
        //FileSystemClassLoader fscl2 = new FileSystemClassLoader(classDataRootPath);
        String className = "com.example.Sample";
        try {
            Class<?> class1 = fscl1.loadClass(className);  // 加载Sample类
            Object obj1 = class1.newInstance();  // 创建对象

            Class<?> class2 = fscl1.loadClass(className);
            Object obj2 = class2.newInstance();
            Method setSampleMethod = class1.getMethod("setSample", java.lang.Object.class);
            setSampleMethod.invoke(obj1, obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testClassIdentity2() {
        String classDataRootPath = "C:\\Users\\Administrator\\Desktop\\class";
        FileSystemClassLoader fscl1 = new FileSystemClassLoader(classDataRootPath);
        //FileSystemClassLoader fscl2 = new FileSystemClassLoader(classDataRootPath);
        String basicClassName = "com.jason.bootswagger2.classloader.netclassloader.CalculatorBasic";
        String advancedClassName = "com.jason.bootswagger2.classloader.netclassloader.CalculatorAdvanced";
        try {
            Class<?> clazz = fscl1.loadClass(basicClassName);  // 加载一个版本的类
            ICalculator calculator = (ICalculator) clazz.newInstance();  // 创建对象
            System.out.println(calculator.getVersion());
            System.out.println(calculator.calculate("CalculatorBasic"));


            clazz = fscl1.loadClass(advancedClassName);  // 加载另一个版本的类
            calculator = (ICalculator) clazz.newInstance();
            System.out.println(calculator.getVersion());
            System.out.println(calculator.calculate("CalculatorAdvanced"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void testClassIdentity3() {
        String classDataRootPath = "C:\\Users\\Administrator\\Desktop\\class\\a.jar";
        FileSystemClassLoader fscl1 = new FileSystemClassLoader(classDataRootPath);
        //FileSystemClassLoader fscl2 = new FileSystemClassLoader(classDataRootPath);
        String basicClassName = "service.CalculatorBasic";
        String advancedClassName = "service.CalculatorAdvanced";
        try {
            Class<?> clazz = fscl1.loadClass(basicClassName);  // 加载一个版本的类
            ICalculator calculator = (ICalculator) clazz.newInstance();  // 创建对象
            System.out.println(calculator.getVersion());
            System.out.println(calculator.calculate("CalculatorBasic"));


            clazz = fscl1.loadClass(advancedClassName);  // 加载另一个版本的类
            calculator = (ICalculator) clazz.newInstance();
            System.out.println(calculator.getVersion());
            System.out.println(calculator.calculate("CalculatorAdvanced"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
