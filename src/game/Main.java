package game;

import engine.GameEngine;
import engine.IGameLogic;

import java.awt.*;

public class Main {
    public static void main(String[] args){
        try{
            boolean vSync = true;
            IGameLogic gameLogic = new Crafter();

            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension d = tk.getScreenSize();

            GameEngine gameEng = new GameEngine("Crafter", d.width/2,d.height/2,false,gameLogic);
            gameEng.run();
        } catch ( Exception excp ){
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
