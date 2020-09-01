package engine;

import static engine.FancyMath.randomForceValue;

public class FancyMath {
    public static int randomDirInt(){
        return  -1 + ((int)(Math.random()*2f) * 2);
    }

    public static float randomDirFloat(){
        return  -1f + ((int)(Math.random()*2f) * 2);
    }

    public static float randomNumber(float x){
        return (float)Math.random() * x;
    }

    public static float randomForceValue(float x){
        return randomNumber(x) * randomDirFloat();
    }

}
