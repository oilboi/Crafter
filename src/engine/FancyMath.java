package engine;

import org.joml.Vector3f;

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

    public static float getDistance(Vector3f pos1, Vector3f pos2){
        return (float)Math.hypot((pos1.x - pos2.x), Math.hypot((pos1.y - pos2.y), (pos1.z - pos2.z)));
    }

}
