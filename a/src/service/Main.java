package service;

public class Main {
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public synchronized void run(){
                for (int i = 1; i < 101; i++){
                    System.out.println(Thread.currentThread().getName()+"----------:"+i);
                }
            }
        };
        Thread t2 = new Thread(){
            @Override
            public synchronized void run(){
                for (int i = 1; i < 101; i++){
                    System.out.println(Thread.currentThread().getName()+"---:"+i);
                }
            }
        };
        Thread t3 = new Thread(){
            @Override
            public synchronized void run(){
                for (int i = 1; i < 101; i++){
                    System.out.println(Thread.currentThread().getName()+"-----------------------:"+i);
                }
            }
        };

        t1.start();
        t2.start();
        t3.start();
    }
}
