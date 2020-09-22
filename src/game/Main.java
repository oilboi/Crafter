package game;

import java.awt.*;

import static engine.GameEngine.initializeGameEngine;
import static engine.GameEngine.runGameEngine;

public class Main {
    public static void main(String[] args){
        try{
            boolean vSync = true;

            Toolkit tk = Toolkit.getDefaultToolkit();

            Dimension d = tk.getScreenSize();

            initializeGameEngine("Crafter", d.width/2,d.height/2,false);

            runGameEngine();

        } catch ( Exception excp ){
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
