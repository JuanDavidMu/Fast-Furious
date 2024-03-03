package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import model.PointsManage;
import model.ThreadManage;

public class test {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Properties pp = new Properties();
        pp.load(new FileInputStream("src/resources/config.properties"));

        ThreadManage tm = new ThreadManage();

        Thread t1 = new Thread(tm,
        pp.getProperty("namePlayer1"));
        Thread t2 = new Thread(tm,
        pp.getProperty("namePlayer2"));
        Thread t3 = new Thread(tm,
        pp.getProperty("namePlayer3"));

        

        t1.start();
        t2.start();
        t3.start();

        

    }
    
}
