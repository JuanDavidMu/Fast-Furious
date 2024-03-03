package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class ThreadManage extends Thread{

    private Properties pp;

    @Override
    public void run() {
        pp = new Properties();
        try {
            pp.load(new FileInputStream("src/resources/config.properties"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int maxPoints = Integer.parseInt(pp.getProperty("pointsToWin"));
        PointsManage pm = new PointsManage();

        do{
            int point = throwDices();

            System.out.println("Punto Sacado "+ Thread.currentThread().getName() + " "+ point);
            pm.sumPoints(point);
            try {
                sleep(timeToThrow());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            System.out.println("Puntos: " + Thread.currentThread().getName() + " " + pm.sumPoints(0));
            System.out.println("Puntos Restantes: " + Thread.currentThread().getName() + " " + pm.pointsToWin(maxPoints));
        }while(pm.getPoints() < maxPoints);

        System.out.println(Thread.currentThread() + " ha terminado!!!!!");
        

    }
    
    public Integer timeToThrow(){
        Random rd = new Random();
        return rd.nextInt(5)*1000;
    }
    public Integer throwDices(){
        Random rd = new Random();
        return rd.nextInt(6)+1;
    }
    
}
